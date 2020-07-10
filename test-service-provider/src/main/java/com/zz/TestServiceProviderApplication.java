/*
package com.zz;*/
/**
 * Created by zhangzhen on 2020/3/19
 *//*


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

*/
/**
 * @ClassName TestServiceProviderApplication
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/19 21:31
 * @Version V1.0
 **//*

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScan(basePackages={"com.zz.framework.api"})//扫描接口
//@ComponentScan(basePackages={"com.zz.business"})
@ComponentScan(basePackages={"com.zz.framework"})//扫描common下的所有类
public class TestServiceProviderApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestServiceProviderApplication.class,args);
	}

}
*/
