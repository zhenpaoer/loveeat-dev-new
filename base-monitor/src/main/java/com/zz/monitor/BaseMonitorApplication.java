package com.zz.monitor;/**
 * Created by zhangzhen on 2020/3/20
 */

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName MonitorApplication
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/20 23:47
 * @Version V1.0
 **/
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class BaseMonitorApplication {
	public static void main(String[] args) {
		SpringApplication.run(BaseMonitorApplication.class,args);
	}
}
