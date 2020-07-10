package com.zz.basezuul;/**
 * Created by zhangzhen on 2020/3/20
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @ClassName BaseZuulApplication
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/20 12:21
 * @Version V1.0
 **/
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class BaseZuulApplication {
	public static void main(String[] args) {
		SpringApplication.run(BaseZuulApplication.class,args);
	}
}
