package com.zz.baseeureka;/**
 * Created by zhangzhen on 2020/3/18
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ClassName BaseEurekaApplication
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/18 20:51
 * @Version V1.0
 **/
@EnableEurekaServer
@SpringBootApplication
public class BaseEurekaApplication {
	public static void main(String[] args) {
		SpringApplication.run(BaseEurekaApplication.class,args);
	}
}
