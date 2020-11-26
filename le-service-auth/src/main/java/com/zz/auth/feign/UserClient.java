package com.zz.auth.feign;

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.GetUserExtResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zhangzhen on 2019/7/12
 */
@FeignClient(value = "le-service-user")
public interface UserClient {

	@GetMapping("/user/getuser")
	public GetUserExtResult getUserext(@RequestParam("username") String username);

	@GetMapping("/user/getuserbyopenid")
	public GetUserExtResult getUserextByOpenid(@RequestParam("openid") String openid);

	@RequestMapping("/user/updateuserlogin")
	public ResponseResult updateUserLogin(@RequestParam("openid") String openid);

	@PostMapping("/user/reguser")
	public ResponseResult registerUser(@RequestParam("openid") String openid,@RequestParam("sessionKey") String sessionKey,
									   @RequestParam("phone") String phone,
									   @RequestParam("nickname") String nickname,@RequestParam("avator") String avator,
									   @RequestParam("userpic") String userpic,	@RequestParam("address") String address,
									   @RequestParam("lon") String lon,	@RequestParam("lat") String lat);
}
