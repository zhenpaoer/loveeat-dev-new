package com.zz.web.controller;/**
 * Created by zhangzhen on 2020/3/20
 */

import com.zz.test.feign.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ConsumerController
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/20 :
 * @Version V1.0
 **/
@RestController
public class ConsumerController {

	@Value("${spring.application.name}")
	public String hostname;

	@Value("${server.port}")
	public String port;

	@Autowired
	public ProviderService providerService;

	@GetMapping("/me")
	public String getHostnameAndPort(){
		return "我是"+hostname +", 端口是：" + port;
	}

	@GetMapping("/hi")
	public String hi(@RequestParam("msg") String msg){
		return providerService.hi(msg);
	}


}
