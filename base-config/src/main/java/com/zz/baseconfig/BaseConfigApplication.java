package com.zz.baseconfig;/**
 * Created by zhangzhen on 2020/3/20
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @ClassName BaseConfigApplication
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/20 16:10
 * @Version V1.0
 **/
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class BaseConfigApplication {
	public static void main(String[] args) {
		SpringApplication.run(BaseConfigApplication.class,args);
	}
}
