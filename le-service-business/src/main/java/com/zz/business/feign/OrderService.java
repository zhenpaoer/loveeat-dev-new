package com.zz.business.feign;

import com.zz.business.feign.fallback.OrderServiceFallBack;
import com.zz.business.feign.fallback.ProviderServiceFallback;
import com.zz.framework.common.interceptor.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zhangzhen on 2020/12/22
 */

@FeignClient(value = "le-service-order",path = "order",fallback = OrderServiceFallBack.class,configuration = FeignClientInterceptor.class)
public interface OrderService {

	@GetMapping("/isOrdered")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS','ROLE_USER')")
	public boolean isOrdered(@RequestParam("pid")int pid,@RequestParam("uid") int uid);
}
