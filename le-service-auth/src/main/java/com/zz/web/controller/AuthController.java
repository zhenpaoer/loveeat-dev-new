package com.zz.web.controller;

import com.alibaba.fastjson.JSON;
import com.zz.auth.service.AuthService;
import com.zz.framework.api.auth.AuthControllerApi;
import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.user.ext.AuthToken;
import com.zz.framework.domain.user.request.LoginRequest;
import com.zz.framework.domain.user.response.AuthCode;
import com.zz.framework.domain.user.response.JwtResult;
import com.zz.framework.domain.user.response.LoginResult;
import com.zz.framework.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangzhen on 2019/7/11
 */
@RestController
@RequestMapping("/")
public class AuthController implements AuthControllerApi {

	@Autowired
	AuthService authService;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	LoadBalancerClient loadBalancerClient;
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Value("${auth.tokenValiditySeconds}")
	int tokenValiditySeconds;

	@Value("${auth.clientId}")
	String clientId;

	@Value("${auth.clientSecret}")
	String clientSecret;

	@Value("${auth.cookieDomain}")
	String cookieDomain;

	@Value("${auth.cookieMaxAge}")
	int cookieMaxAge;

	@Override
	@PostMapping("/userlogin")
	public LoginResult login(LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		if (username == null || username.equals("")){
			ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
		}
		if (password == null || password.equals("")){
			ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
		}

		//拿到令牌，存到redis
		AuthToken authToken = login(username,password,clientId,clientSecret);
		//将用户身份令牌存到cookie
		this.saveCookie(authToken.getAccess_token());
		return new LoginResult(CommonCode.SUCCESS,authToken.getAccess_token());
	}

	private void saveCookie(String token){

		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		//HttpServletResponse response,String domain,String path, String name,String value, int maxAge,boolean httpOnly
		CookieUtil.addCookie(response,cookieDomain,"/","uid",token,cookieMaxAge,false);
	}

	@Override
	@PostMapping("/userlogout")
	public ResponseResult logout() {
		//取出身份令牌
		String tokenFormCookie = getTokenFormCookie();
		//删除redis中token
		authService.delToken(tokenFormCookie);
		//清除cookie中的token
		clearCookie(tokenFormCookie);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	@Override
	@GetMapping("/userjwt")
	public JwtResult userjwt() {
		//获取cookie中的令牌
		String tokenFormCookie = getTokenFormCookie();
		//根据令牌从redis查询jwt
		AuthToken userToken = authService.getUserToken(tokenFormCookie);
		if (userToken == null){
			return new JwtResult(CommonCode.FAIL,null);
		}

		return new JwtResult(CommonCode.SUCCESS,userToken.getJwt_token());
	}

	private String getTokenFormCookie(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Map<String, String> map = CookieUtil.readCookie(request,"uid");
		String access_token = map.get("uid");
		return access_token;
	}

	private void clearCookie(String token){
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, 0, false);
	}

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
