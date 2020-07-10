package com.zz;

import com.zz.minio.service.MinioPictureService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Administrator
 * @version 1.0
 **/

@EnableFeignClients //开启feignClient
@EnableDiscoveryClient
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan(basePackages={"com.zz.framework.api"})//扫描接口
@ComponentScan(basePackages={"com.zz"})
@ComponentScan(basePackages={"com.zz.framework"})//扫描common下的所有类
public class ServicePictureApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ServicePictureApplication.class, args);

    }
    @Bean
    public MinioPictureService MinioConfig() {
        return new MinioPictureService();
    }

}
