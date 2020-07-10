package com.zz.basezipkin;/**
 * Created by zhangzhen on 2020/3/20
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.internal.EnableZipkinServer;

/**
 * @ClassName BaseZipkinApplication
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/20 22:58
 * @Version V1.0
 **/
@EnableZipkinServer
@EnableDiscoveryClient
@SpringBootApplication
public class BaseZipkinApplication {
	public static void main(String[] args) {
		SpringApplication.run(BaseZipkinApplication.class,args);
	}
}
