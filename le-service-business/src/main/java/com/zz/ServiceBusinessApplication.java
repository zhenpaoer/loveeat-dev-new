package com.zz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Administrator
 * @version 1.0
 **/
@EnableHystrixDashboard
@EnableFeignClients //开启feignClient
@EnableDiscoveryClient
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan(basePackages={"com.zz.framework.api"})//扫描接口
@ComponentScan(basePackages={"com.zz"})
@ComponentScan(basePackages={"com.zz.framework"})//扫描common下的所有类
//@MapperScan(annotationClass = Repository.class, basePackages = "com.xxx.dao")
@MapperScan(annotationClass = Repository.class,basePackages = "com.zz.business.dao")
public class ServiceBusinessApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ServiceBusinessApplication.class, args);
    }
}
