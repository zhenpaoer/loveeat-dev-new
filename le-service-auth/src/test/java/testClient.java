import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * Created by zhangzhen on 2019/7/10
 *
 * 为了不破坏Spring Security的代码，
 * 我们在Service方法中通过RestTemplate请求Spring Security所暴露的申请令牌接口来申请令牌，下边是测试代码：
 */
@SpringBootTest(classes = testClient.class)
@RunWith(SpringRunner.class)
public class testClient {

	@Autowired
	RestTemplate restTemplate;

//	@Autowired
//	LoadBalancerClient loadBalancerClient;

	@Test
	public void testClient() {

		//准备URL
		//采用客户端负载均衡，从eureka获取认证服务的ip 和端口
//		ServiceInstance choose = loadBalancerClient.choose("le-service-auth");
		// url就是 申请令牌的url /oauth/token //method http的方法类型
		// requestEntity请求内容
		// responseType，将响应的结果生成的类型
//		URI uri = choose.getUri();
//		String authUrl = uri + "/oauth/token";
		String authUrl = "http://localhost:8070/oauth/token";

		//请求的内容分两部分
		// 1、header信息，包括了http basic认证信息
		LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		String httpbasic = Httpbasic("LeWebapp", "LeWebapp");
		headers.add("Authorization",httpbasic);
		//2、包括：grant_type、username、passowrd
		LinkedMultiValueMap<String,String> body = new LinkedMultiValueMap<>();
		body.add("grant_type","password");
		body.add("username","zhangsan");
		body.add("password","123");

		HttpEntity<LinkedMultiValueMap<String,String>> multiValueMapHttpEntity = new HttpEntity<>(body,headers);

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
		ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
		Map body1 = exchange.getBody();
		System.out.println("body1=="+body1);
	}

	private String Httpbasic(String s, String s1) {
		//将客户端id和客户端密码拼接，按“客户端id:客户端密码”
		String string = s +":"+ s1;
		//进行base64编码
		byte[] encode = Base64Utils.encode(string.getBytes());
		return "Basic "+new String(encode);
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Test
	public void test111(){
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String encode = bCryptPasswordEncoder.encode("123");
		System.out.println("encode = "+encode);
	}
}

