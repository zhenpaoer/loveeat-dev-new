package com.zz.test.feign.service;

import com.zz.framework.domain.user.response.GetUserExtResult;
import com.zz.test.feign.service.fallback.ProviderServiceFallback;
import com.zz.test.feign.service.fallback.UserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zhangzhen on 2019/7/12
 */
@FeignClient(value = "le-service-user" ,fallback = UserClientFallback.class)
public interface UserClient {

	@GetMapping("/user/getuser")
	public GetUserExtResult getUserext(@RequestParam("username") String username);
}
