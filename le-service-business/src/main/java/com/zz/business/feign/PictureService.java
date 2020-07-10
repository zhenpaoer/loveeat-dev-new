package com.zz.business.feign;

import com.zz.business.feign.fallback.PictureServiceFallBack;
import com.zz.framework.common.model.response.ResponseResultWithData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangzhen on 2020/3/19
 * Feign远程调用接口，@FeignClient配置了远程调用其他应用接口的应用名称
 */
@FeignClient(value = "le-service-picture",path = "picture",fallback = PictureServiceFallBack.class)
public interface PictureService {
	/**
	 * 这里暴露一个Feign接口地址，其中`@GetMapping`中的地址一定对应了`template-admin`服务中某个Controller中的请求地址（如果`template-admin`服务中没有这个接口地址就会404）
	 * 如果其他地方调用了AuthFeignService接口的hello方法，FeignClient将类似通过转发的方式去请求调用`template-admin`服务中符合的接口地址的方法
	 * 如果请求传递了参数，需要加@RequestParam注解标识。如果URL中有动态参数，要添加@PathVariable注解
	 */

	@PostMapping(value = "uploadbuspic",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseResultWithData uploadBusPic(@RequestPart MultipartFile file, @RequestParam String bid);

//	@PostMapping(value = "picture/uploadpropic")
	@PostMapping(value = "uploadpropic",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseResultWithData uploadProPic(@RequestPart MultipartFile file, @RequestParam String pid);

	@GetMapping(value = "delpic")
	public boolean removePic(@RequestParam String url);
//	@GetMapping(value = "preview/**")
//	public void previewPicture(@RequestParam HttpServletRequest request,@RequestParam HttpServletResponse response);
}
