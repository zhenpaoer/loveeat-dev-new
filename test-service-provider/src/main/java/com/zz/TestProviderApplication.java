package com.zz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
@EnableDiscoveryClient
@ComponentScan(basePackages={"com.zz.framework.api"})//扫描接口
//@ComponentScan(basePackages={"com.zz.business"})
@ComponentScan(basePackages={"com.zz.framework"})//扫描common下的所有类
public class TestProviderApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestProviderApplication.class,args);
	}

}
