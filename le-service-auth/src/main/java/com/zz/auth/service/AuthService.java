package com.zz.auth.service;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.http4.NFHttpClient;
import com.zz.auth.feign.UserClient;
import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResultCode;
import com.zz.framework.domain.user.LeUserBasic;
import com.zz.framework.domain.user.ext.AuthToken;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.AuthCode;
import com.zz.framework.domain.user.response.GetUserExtResult;
import com.zz.framework.domain.user.response.LoginResult;
import com.zz.framework.utils.Jcode2SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangzhen on 2019/7/11
 */
@Slf4j
@Service
public class AuthService {
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	LoadBalancerClient loadBalancerClient;
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Value("${auth.tokenValiditySeconds}")
	int tokenValiditySeconds;
	@Value("${weChat.appid}")
	private String appid;

	@Value("${weChat.secret}")
	private String secret;
	@Autowired
	UserClient userClient;
	private static HttpClientContext context = HttpClientContext.create();

	public AuthToken login(String username, String password, String clientId, String clientSecret) {
		//申请令牌
		AuthToken authToken = applyToken(username, password, clientId, clientSecret);
		if (authToken == null) {
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
		}
		//保存到redis
		String content = JSON.toJSONString(authToken);
		boolean saveTokenResult = saveToken(authToken.getAccess_token(), content, tokenValiditySeconds);
		if (!saveTokenResult){
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
		}

		return authToken;
	}

	//申请令牌
	private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {

		//准备URL
		//采用客户端负载均衡，从eureka获取认证服务的ip 和端口
		ServiceInstance choose = loadBalancerClient.choose("le-service-auth");
		// url就是 申请令牌的url /oauth/token //method http的方法类型
		// requestEntity请求内容
		// responseType，将响应的结果生成的类型
		URI uri = choose.getUri();
		String authUrl = uri + "/oauth/token";
//		String authUrl = "http://localhost:8070/oauth/token";

		//请求的内容分两部分
		// 1、header信息，包括了http basic认证信息
		LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String httpbasic = httpBasic("LeWebapp", "LeWebapp");
		headers.add("Authorization",httpbasic);
		//2、包括：grant_type、username、passowrd
		LinkedMultiValueMap<String,String> body = new LinkedMultiValueMap<>();
		body.add("grant_type","password");
		body.add("username",username);
		body.add("password",password);

		HttpEntity<LinkedMultiValueMap<String,String>> httpEntity = new HttpEntity<>(body,headers);

		//指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
			@Override public void handleError(ClientHttpResponse response) throws IOException {
				//当响应的值为400或401时候也要正常响应，不要抛出异常
				if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
					super.handleError(response);
				}
			}
		});
		//远程调用申请令牌
		ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
		Map map = exchange.getBody();
		if (map == null || (String) map.get("access_token") == null || (String) map.get("refresh_token") == null || (String) map.get("jti") == null) {
			////获取spring security返回的错误信息
			if (map != null && map.get("error_description") != null) {
				if (map.get("error_description").equals("坏的凭证")) {
					//账号或密码错误
					ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
				} else if (map.get("error_description").toString().indexOf("UserDetailsService returned null") >= 0) {
					//账号不存在
					ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
				}
			}
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
		}
		String access_token = (String) map.get("access_token");
		String refresh_token = (String) map.get("refresh_token");
		String jti = (String) map.get("jti"); //jti是jwt令牌的唯一标识作为用户身份令牌

		//返回authToken对象
		AuthToken authToken = new AuthToken();
		authToken.setAccess_token(jti);
		authToken.setJwt_token(access_token);
		authToken.setRefresh_token(refresh_token);
		return authToken;

}

	private String httpBasic(String clientId, String clientSecret) {
		//将客户端id和客户端密码拼接，按“客户端id:客户端密码”
		String s = clientId + ":" + clientSecret;
		//进行base64编码
		byte[] encode = Base64Utils.encode(s.getBytes());
		return "Basic " + new String(encode);
	}

	//存储令牌到redis
	private boolean saveToken(String access_token,String content,long ttl){
		Long expire = null;
		try {
			//令牌名称 也是redis的key
			String key = "token:" + access_token;
			//保存令牌到redis
			stringRedisTemplate.boundValueOps(key).set(content,ttl, TimeUnit.SECONDS);
			//获取过期时间
			expire = stringRedisTemplate.getExpire(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
		}
		if (expire != null){
			return  expire > 0;
		}
		return false;
	}
	//从redis查询令牌
	public AuthToken getUserToken(String tokenFormCookie) {
		try {
			String key = "token:"+tokenFormCookie;
			String value = stringRedisTemplate.opsForValue().get(key);
			if(value !=null){
				AuthToken authToken =  JSON.parseObject(value,AuthToken.class);
				return authToken;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
		}
		return null;
	}
	public boolean delToken(String token) {
		try {
			String key = "token:" + token;
			return stringRedisTemplate.delete(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
		}
		return false;
	}


	public LoginResult wxLogin(String code){
		JSONObject sessionInfo = null;
		try {
			sessionInfo = JSONObject.parseObject(jcode2Session(code));
		} catch (Exception e) {
			log.info("微信登陆异常 {}",e.getMessage());
			e.printStackTrace();
		}
		String openid = (String)sessionInfo.get("openid");
		String sessionKey = (String)sessionInfo.get("session_key");
		if(StringUtils.isEmpty(openid)){
			ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
		}
		log.info("openid={}",openid);
		log.info("session_key={}",sessionKey);
		GetUserExtResult userextByOpenid = userClient.getUserextByOpenid(openid);

		LeUserExt user = userextByOpenid.getLeUserExt();
//		responseResult = userClient.updateUserLogin(openid);
		if(user == null){
			//根据openid获取用户信息
//			String userInfo = getUserInfo(openid);
//			JSONObject jsonUserInfo = JSONObject.parseObject(userInfo);
			String phone = "";
			String nickname= "";
			String avator= "";
			String userpic= "";
			String address= "";
			String lon = "";
			String lat ="";
			String password = new BCryptPasswordEncoder().encode(openid);
			// 添加到数据库
			ResponseResult responseResult = userClient.registerUser(openid,sessionKey, phone, nickname, avator, userpic, address, lon, lat);
			if(responseResult.getCode() != 10000){
				ExceptionCast.cast(AuthCode.AUTH_WXLOGIN_FAIL);
			}
		}

		//申请令牌
		AuthToken authToken = applyToken(openid, openid, "LeWebApp", "LeWebApp");
		if (authToken == null) {
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
		}
		//保存到redis
		String content = JSON.toJSONString(authToken);
		boolean saveTokenResult = saveToken(authToken.getAccess_token(), content, tokenValiditySeconds);
		if (!saveTokenResult){
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
		}
		return new LoginResult(CommonCode.SUCCESS,authToken.getAccess_token());
	}
	/**
	 * 微信登录凭证校验
	 * @param code
	 * @return
	 * @throws Exception
	 */
	private String jcode2Session(String code)throws Exception{
		String sessionInfo = Jcode2SessionUtil.jscode2session(appid,secret,code,"authorization_code");//登录grantType固定
		log.info(sessionInfo);
		return sessionInfo;
	}

	/**
	 * 获取用户信息
	 * @param openid
	 * @return
	 */
	private String getUserInfo(String openid){
		String accessToken = getAccessToken();
		String userInfo = Jcode2SessionUtil.getUserInfo(openid, accessToken);
		if (userInfo.contains("errcode")){
			String token = requestAndSaveAccessToken();
			return Jcode2SessionUtil.getUserInfo(openid, token);
		}else {
			return userInfo;
		}
	}
	/**
	 * 请求微信accesstoken
	 * @return
	 */
	private String getAccessToken(){
		try {
			String key = "wxAccessToken";
			String value = stringRedisTemplate.opsForValue().get(key);
			if(!StringUtils.isEmpty(value)){
				log.info("wxAccessToken={}",value);
				return value;
			}else {
				value = requestAndSaveAccessToken();
				log.info("wxAccessToken={}",value);
				return value;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
		}
		return null;
	}

	/**
	 * 请求accesstoken和存储到redis
	 * @return
	 */
	private String requestAndSaveAccessToken(){
		String accessToken = Jcode2SessionUtil.getAccessToken(appid, secret);
		JSONObject jsonObject = JSONObject.parseObject(accessToken);
		String wxAccessToken = jsonObject.getString("access_token");
		Integer wxAccessTokenExpiresIn = jsonObject.getInteger("expires_in");
		log.info("wxAccessToken:"+wxAccessToken);
		try {
			//令牌名称 也是redis的key
			String key = "wxAccessToken";
			//保存令牌到redis
			stringRedisTemplate.boundValueOps(key).set(wxAccessToken,wxAccessTokenExpiresIn, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
		}
		return wxAccessToken;
	}
}
