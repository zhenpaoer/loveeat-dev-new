package com.zz.web.controller;/**
 * Created by zhangzhen on 2020/3/19
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @ClassName ServiceProviderController
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/19 21:33
 * @Version V1.0
 **/
@RestController
public class ServiceProviderController {

	@Value("${server.port}")
	private String myport ;

	@Value("${spring.application.name}")
	public String hostname;

	@GetMapping("/hi")
	public String hi(@RequestParam("msg") String msg)  {
		//睡眠3s
//		System.out.println("start");
//		Thread.sleep(3000);
//		System.out.println("end");
/*
		ArrayList<Integer> a = new ArrayList<>();
		Integer integer = a.get(5);*/
		return "我是"+hostname +", 端口是：" + myport+", 消息是：" + msg ;
	}

	@GetMapping("/me")
	public String me(){
		String a = "我是"+hostname +", 端口是：" + myport ;
		return a;
	}
}
