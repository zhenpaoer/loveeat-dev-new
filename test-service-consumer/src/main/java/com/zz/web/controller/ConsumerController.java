package com.zz.web.controller;/**
 * Created by zhangzhen on 2020/3/20
 */

import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.GetUserExtResult;
import com.zz.test.feign.service.ProviderService;
import com.zz.test.feign.service.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName ConsumerController
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/20 :
 * @Version V1.0
 **/
@RestController
public class ConsumerController {

	@Value("${spring.application.name}")
	public String hostname;

	@Value("${server.port}")
	public String port;

	@Autowired
	public ProviderService providerService;

	@Autowired
	UserClient userClient;

	@Autowired
	LoadBalancerClient loadBalancerClient;
	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/me")
	public String getHostnameAndPort(){
		return "我是"+hostname +", 端口是：" + port;
	}

	@GetMapping("/hi")
	public String hi(@RequestParam("msg") String msg){
		return providerService.hi(msg);
	}

	@GetMapping("/getuser")
	public GetUserExtResult getUserExtResult(@RequestParam("username") String username) {
		GetUserExtResult userext = userClient.getUserext(username);
		if (userext .getCode() != 10000 ) return userext;
		LeUserExt leUserExt = userext.getLeUserExt();
		System.out.println("leUserExt == "+leUserExt.toString());
		return userext;
	}

	@GetMapping("/restTemplate")
	public String dc() {
		ServiceInstance serviceInstance = loadBalancerClient.choose("test-service-consumer");
		String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/me";
		System.out.println(url);
		return restTemplate.getForObject(url, String.class);

	}
	@GetMapping("/gettoken")
	public Map getToken() {
		ServiceInstance choose = loadBalancerClient.choose("le-service-auth");
		String URL = choose.getUri() + "/oauth/token";
		//header
		String httpBasic = httpBasic("LeWebapp", "LeWebapp");
		LinkedMultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
		multiValueMap.add("Authorization",httpBasic);
		//body
		LinkedMultiValueMap<String,String> body = new LinkedMultiValueMap<>();
		body.add("grant_type","password");
		body.add("username","zhangsan");
		body.add("password","123");

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
		String urlStr = "http://localhost:8070/oauth/token";

		//申请令牌
		ResponseEntity<Map> exchange = restTemplate.exchange(URL, HttpMethod.POST, httpEntity, Map.class);
		System.out.println("url == "+URL);
		System.out.println("url == "+urlStr);
		Map map = exchange.getBody();
		return map;
	}
	private String httpBasic(String clientId,String clientSecret){

		//将客户端id和客户端密码拼接，按“客户端id:客户端密码”
		String s = clientId +":"+ clientSecret;
		//进行base64编码
		byte[] encode = Base64Utils.encode(s.getBytes());
		return "Basic "+new String(encode);
	}
}
