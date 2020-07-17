package com.zz.auth.feign;

import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.GetUserExtResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zhangzhen on 2019/7/12
 */
@FeignClient(value = "le-service-user")
public interface UserClient {

	@GetMapping("/user/getuser")
	public GetUserExtResult getUserext(@RequestParam("username") String username);
}
