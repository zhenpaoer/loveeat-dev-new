package com.zz;/**
 * Created by zhangzhen on 2020/3/19
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName TestServiceConsumerApplication
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/19 21:40
 * @Version V1.0
 **/
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableHystrixDashboard
@EnableFeignClients
public class TestServiceConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestServiceConsumerApplication.class,args);
	}

	//这里没有加 @Bean 和 @LoadBalanced
//	@Bean
//	@LoadBalanced//开启负载均衡
//	public RestTemplate restTemplate() {
//		return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
//	}
}
