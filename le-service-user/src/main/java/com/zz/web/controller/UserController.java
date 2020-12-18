package com.zz.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zz.framework.api.user.UserControllerApi;
import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.response.BusinessCode;
import com.zz.framework.domain.business.response.GetBusinessInfoResult;
import com.zz.framework.domain.user.LeUserBasic;
import com.zz.framework.domain.user.LeUserRole;
import com.zz.framework.domain.user.ext.AuthToken;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.request.LoginRequest;
import com.zz.framework.domain.user.response.AuthCode;
import com.zz.framework.domain.user.response.GetUserExtResult;
import com.zz.framework.domain.user.response.LoginResult;
import com.zz.framework.domain.user.response.UserCode;
import com.zz.user.dao.LeUserBasicMapper;
import com.zz.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController implements UserControllerApi {
	@Autowired
	UserService userService;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	LoadBalancerClient loadBalancerClient;

	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Autowired
	LeUserBasicMapper leUserBasicMapper;
	@Value("")
	public String publicKey;
	//公钥
	private static final String PUBLIC_KEY = "publickey.txt";

/*	其实在用户第一次授权的同时 把用户头像昵称还有获取到的手机号之类的信息直接存放到服务器就好了
	下次用户进来的时候 直接传过去code  后台通过这个code去判断是否为新用户（即已授权过的用户）
	如果是的话 直接把该用户信息返回即可
	如果不是新用户（即已授权过的用户）那么再看看什么时候让用户授权 加个判断 就不用每次都授权了
	*/

	@Override
	@GetMapping("getuser")
	public GetUserExtResult getUserext(@RequestParam("username") String userName) {
		if (StringUtils.isEmpty(userName)){
			ExceptionCast.cast(UserCode.USER_USERNAME_CHECK_ERROR);
		}
		return userService.getUserExt(userName);
	}

	@Override
	@PostMapping("reguser")
	public ResponseResult registerUser(@RequestParam("openid") String openid,@RequestParam("sessionKey") String sessionKey,@RequestParam("phone") String phone,
									   @RequestParam("nickname") String nickname,@RequestParam("avator") String avator,
									   @RequestParam("userpic") String userpic,	@RequestParam("address") String address,
									   @RequestParam("lon") String lon,	@RequestParam("lat") String lat) {
		if (StringUtils.isEmpty(openid)){
			ExceptionCast.cast(UserCode.USER_OPENID_NONE);
		}
		LeUserBasic user = new LeUserBasic();
		user.setOpenid(openid);
		user.setSessionkey(sessionKey);
		user.setNickname(nickname);
		user.setUsername(openid);
		user.setAvatar(avator);
		user.setAddress(address);
		user.setUserpic(userpic);
		user.setPhone(phone);
		user.setLat(lat);
		user.setLon(lon);
		user.setCreatetime(LocalDateTime.now());
		user.setUpdatetime(LocalDateTime.now());
		user.setPassword( new BCryptPasswordEncoder().encode(openid) );
		Integer result = userService.createUser(user);
		if (result > 0 ){
			//授权
			LeUserRole role = new LeUserRole();
			LeUserBasic userBasic = userService.getUserBasicByOpenId(openid);
			role.setUserId(userBasic.getId());
			role.setRoleId(2); // ROLE_USER 2
			userService.createUserRole(role);
			return new ResponseResult(CommonCode.SUCCESS);
		}

		return  new ResponseResult(UserCode.USER_RIG_CHECK_ERROR);
	}

	@Override
	@GetMapping("getuserbyopenid")
	public GetUserExtResult getUserextByOpenid(@RequestParam("openid") String openid) {
		if (StringUtils.isEmpty(openid)){
			ExceptionCast.cast(UserCode.USER_USERNAME_CHECK_ERROR);
		}
		return userService.getUserextByOpenid(openid);
	}

	@Override
	@RequestMapping("updateuserlogin")
	@PreAuthorize(value = "hasAnyRole('ROLE_USER')")
	public ResponseResult updateUserLogin(@RequestParam("nickName") String nickName, @RequestParam("avatarUrl")String avatarUrl,
										  @RequestParam("address")String address,@RequestParam("lon") String lon, @RequestParam("lat")String lat,HttpServletRequest request) {
		AuthToken authToken = new AuthToken();
		String token = request.getHeader("token");
		//根据令牌从redis查询jwt
		try {
			String key = "token:"+token;
			String value = stringRedisTemplate.opsForValue().get(key);
			if(value !=null){
				authToken =  JSON.parseObject(value,AuthToken.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisConnectionUtils.unbindConnection(Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()));
		}
		String jwt_token = authToken.getJwt_token();
		Jwt jwt = JwtHelper.decodeAndVerify(jwt_token, new RsaVerifier(Objects.requireNonNull(getPubKey())));
		String claims = jwt.getClaims();
		JSONObject jsonObject = JSONObject.parseObject(claims);
		String userid = jsonObject.getString("id");
		LeUserBasic leUserBasic = leUserBasicMapper.selectByPrimaryKey(Integer.parseInt(userid));
		if (leUserBasic == null){
			ExceptionCast.cast(UserCode.USER_LOGIN_ERROR);
		}
		log.info("更新用户信息id={}",userid);
		return userService.updateUserLogin(leUserBasic.getOpenid(),nickName,avatarUrl,address,lon,lat);
	}

	public String getTokenFromHeader(HttpServletRequest request) {
		try {
			//取出头信息
			String token = request.getHeader("token");
			if(StringUtils.isEmpty(token)){
				return null;
			}
			//取到access_token
			return token;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
		}
		return null;
	}
	/*** 获取非对称加密公钥 Key * @return 公钥 Key */
	private String getPubKey() { Resource resource = new ClassPathResource(PUBLIC_KEY);
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
			BufferedReader br = new BufferedReader(inputStreamReader);
			return br.lines().collect(Collectors.joining("\n"));
		} catch (IOException ioe) {
			return null;
		}
	}
/*	@Override
	@GetMapping("createuser")
	public GetUserExtResult createUser(@RequestParam("username") String userName) {
		if (StringUtils.isEmpty(userName)){
			return new GetUserExtResult(UserCode.USER_USERNAME_CHECK_ERROR,null);
		}
		return userService.getUserExt(userName);
	}*/

}
