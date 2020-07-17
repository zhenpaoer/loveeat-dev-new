package com.zz.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.user.*;
import com.zz.framework.domain.user.ext.AuthToken;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.AuthCode;
import com.zz.framework.domain.user.response.GetUserExtResult;
import com.zz.framework.domain.user.response.UserCode;
import com.zz.user.dao.*;
import com.zz.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	LeUserBasicMapper leUserBasicMapper;
	@Autowired
	LeUserRoleMapper leUserRoleMapper;
	@Autowired
	LeRoleMapper leRoleMapper;
	@Autowired
	LeRolePermissionMapper leRolePermissionMapper;
	@Autowired
	LePermissionMapper lePermissionMapper;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	LoadBalancerClient loadBalancerClient;
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Value("${auth.tokenValiditySeconds}")
	int tokenValiditySeconds;

	@Override
	public GetUserExtResult getUserExt(String userName) {
		Example example = new Example(LeUserBasic.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("username", userName);
		LeUserBasic leUserBasic = leUserBasicMapper.selectOneByExample(example);
		if (leUserBasic == null) {
			return new GetUserExtResult(UserCode.USER_ACCOUNT_NOTEXISTS,null);
		}

		LeUserExt leUserExt = new LeUserExt();
		BeanUtils.copyProperties(leUserBasic, leUserExt);

		//用户ID
		Integer id = leUserBasic.getId();
		List<Integer> roleIds = leUserRoleMapper.getRoleIdByUserId(id);
		if (roleIds.size() == 0){
			//如果用户没角色，则赋予用户权限
			LeUserRole leUserRole = new LeUserRole();
			leUserRole.setUserId(id);
			leUserRole.setRoleId(2);
			leUserRoleMapper.insert(leUserRole);
			this.getUserExt(userName);
		}
		List<Integer> permissionIdsByRoleids = leRolePermissionMapper.getPermissionIdsByRoleids(roleIds);
		//去重的权限id
		List<Integer> permissionIds = permissionIdsByRoleids.stream().distinct().collect(Collectors.toList());
		//拿权限
		List<LePermission> permissions = lePermissionMapper.getPermissionByIds(permissionIds);
		if (permissions.size() != 0){
			leUserExt.setPermissions(permissions);
		}
		//拿角色
		List<LeRole> roles = leRoleMapper.getRoleByIds(roleIds);
		if (roles.size() !=0 ){
			leUserExt.setRoles(roles);
		}
		return new GetUserExtResult(CommonCode.SUCCESS,leUserExt);
	}

	@Override
	public List<Integer> getLeUserRole(int id) {

		List<Integer> roleIds= leUserRoleMapper.getRoleIdByUserId(id);
		System.out.println("roleIds=="+roleIds);
		return roleIds;
	}

	@Override
	public AuthToken login(String username, String password, String clientId, String clientSecret) {
		//申请令牌
		AuthToken authToken = applyToken(username, password, clientId, clientSecret);
		if(authToken == null){ ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL); }
		//保存到redis
		String content = JSON.toJSONString(authToken);
		boolean saveTokenResult = saveToken(authToken.getAccess_token(), content, tokenValiditySeconds);
		if (!saveTokenResult){
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
		}

		return authToken;
	}
	//申请令牌
	private AuthToken applyToken(String username, String password, String clientId, String clientSecret){

		ServiceInstance choose = loadBalancerClient.choose("le-service-auth");
		String URL = choose.getUri() + "/oauth/token";
		//header
		String httpBasic = httpBasic(clientId, clientSecret);
		LinkedMultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("Authorization",httpBasic);
		//body
		LinkedMultiValueMap<String,String> body = new LinkedMultiValueMap<>();
		body.add("grant_type","password");
		body.add("username",username);
		body.add("password",password);

		HttpEntity<LinkedMultiValueMap<String,String>> httpEntity = new HttpEntity<>(body,multiValueMap);

		//指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
			@Override public void handleError(ClientHttpResponse response) throws IOException {
				//当响应的值为400或401时候也要正常响应，不要抛出异常
				if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
					super.handleError(response);
				}
			}
		});

		//申请令牌
		ResponseEntity<Map> exchange = restTemplate.exchange(URL, HttpMethod.POST, httpEntity, Map.class);
		Map map = exchange.getBody();
		if (map == null || (String) map.get("access_token") == null || (String)map.get("refresh_token") == null || (String)map.get("jti") == null){
			////获取spring security返回的错误信息
			if (map != null && map.get("error_description") != null){
				if (map.get("error_description").equals("坏的凭证")){
					//账号或密码错误
					ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
				}else if (map.get("error_description").toString().indexOf("UserDetailsService returned null")>=0){
					//账号不存在
					ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
				}
			}
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
		}
		String access_token = (String) map.get("access_token");
		String refresh_token = (String)map.get("refresh_token");
		String jti = (String)map.get("jti"); //jti是jwt令牌的唯一标识作为用户身份令牌

		//返回authToken对象
		AuthToken authToken = new AuthToken();
		authToken.setAccess_token(jti);
		authToken.setJwt_token(access_token);
		authToken.setRefresh_token(refresh_token);
		return authToken;
	}
	private String httpBasic(String clientId,String clientSecret){

		//将客户端id和客户端密码拼接，按“客户端id:客户端密码”
		String s = clientId +":"+ clientSecret;
		//进行base64编码
		byte[] encode = Base64Utils.encode(s.getBytes());
		return "Basic "+new String(encode);
	}
	//存储令牌到redis
	private boolean saveToken(String access_token,String content,long ttl){
		//令牌名称 也是redis的key
		String key = "user_token:" + access_token;
		//保存令牌到redis
		stringRedisTemplate.boundValueOps(key).set(content,ttl, TimeUnit.SECONDS);
		//获取过期时间
		Long expire = stringRedisTemplate.getExpire(key);
		return expire>0;
	}

	//从redis查询令牌
	public AuthToken getUserToken(String tokenFormCookie) {
		String key = "user_token:"+tokenFormCookie;
		String value = stringRedisTemplate.opsForValue().get(key);
		if(value !=null){
			AuthToken authToken =  JSON.parseObject(value,AuthToken.class);
			return authToken;
		}

		return null;
	}

	public boolean delToken(String tokenFormCookie) {
		String key = "user_token:" + tokenFormCookie;
		stringRedisTemplate.delete(key);
		return true;
	}
}
