package com.zz.order.feign;

import com.zz.framework.common.interceptor.FeignClientInterceptor;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.response.GetBusinessDetailResult;
import com.zz.framework.domain.business.response.GetLeProductPicMenuExtResult;
import com.zz.order.feign.fallback.BusinessServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangzhen on 2020/3/19
 * Feign远程调用接口，@FeignClient配置了远程调用其他应用接口的应用名称
 */
@FeignClient(value = "le-service-business",path = "product",fallback = BusinessServiceFallBack.class,configuration = FeignClientInterceptor.class)
public interface BusinessService {
	/**
	 * 这里暴露一个Feign接口地址，其中`@GetMapping`中的地址一定对应了`template-admin`服务中某个Controller中的请求地址（如果`template-admin`服务中没有这个接口地址就会404）
	 * 如果其他地方调用了AuthFeignService接口的hello方法，FeignClient将类似通过转发的方式去请求调用`template-admin`服务中符合的接口地址的方法
	 * 如果请求传递了参数，需要加@RequestParam注解标识。如果URL中有动态参数，要添加@PathVariable注解
	 */
	@GetMapping("/getbyid")
	public GetLeProductPicMenuExtResult getProductById(@RequestParam("id") int id);

	@PostMapping("/updateproductissale")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseResult updateProductIsSaleByPid(@RequestParam("pid")int pid, @RequestParam("issale")int issale);

	@GetMapping("/updateProductIsSaleToOne")
	public ResponseResult updateProductIsSaleToOne(@RequestParam("pid")int pid);
}
