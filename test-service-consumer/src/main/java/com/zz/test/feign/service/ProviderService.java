package com.zz.test.feign.service;

import com.zz.test.feign.service.fallback.ProviderServiceFallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zhangzhen on 2020/3/19
 * Feign远程调用接口，@FeignClient配置了远程调用其他应用接口的应用名称
 */
@FeignClient(value = "test-service-provider",fallback = ProviderServiceFallback.class)
public interface ProviderService {

	/**
	 * 这里暴露一个Feign接口地址，其中`@GetMapping`中的地址一定对应了`template-admin`服务中某个Controller中的请求地址（如果`template-admin`服务中没有这个接口地址就会404）
	 * 如果其他地方调用了AuthFeignService接口的hello方法，FeignClient将类似通过转发的方式去请求调用`template-admin`服务中符合的接口地址的方法
	 * 如果请求传递了参数，需要加@RequestParam注解标识。如果URL中有动态参数，要添加@PathVariable注解
	 *
	 * @param msg
	 * @return
	 */

	@GetMapping("/hi")
	public String hi(@RequestParam("msg") String msg);


}
