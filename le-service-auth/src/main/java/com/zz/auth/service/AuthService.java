package com.zz.auth.service;

import com.alibaba.fastjson.JSON;
import com.netflix.http4.NFHttpClient;
import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.domain.user.ext.AuthToken;
import com.zz.framework.domain.user.response.AuthCode;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangzhen on 2019/7/11
 */
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
	private static HttpClientContext context = HttpClientContext.create();

	public AuthToken login(String username, String password, String clientId, String clientSecret) {
		//申请令牌
		AuthToken authToken = applyToken(username, password, clientId, clientSecret);
		if (authToken == null) {
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
		}
		//保存到redis


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
}
