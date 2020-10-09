package com.zz.web.controller;

import com.alibaba.fastjson.JSON;
import com.zz.framework.api.user.UserControllerApi;
import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.response.BusinessCode;
import com.zz.framework.domain.business.response.GetBusinessInfoResult;
import com.zz.framework.domain.user.LeUserBasic;
import com.zz.framework.domain.user.LeUserRole;
import com.zz.framework.domain.user.ext.AuthToken;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.request.LoginRequest;
import com.zz.framework.domain.user.response.AuthCode;
import com.zz.framework.domain.user.response.GetUserExtResult;
import com.zz.framework.domain.user.response.LoginResult;
import com.zz.framework.domain.user.response.UserCode;
import com.zz.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("user")
public class UserController implements UserControllerApi {
	@Autowired
	UserService userService;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	LoadBalancerClient loadBalancerClient;

	@Autowired
	StringRedisTemplate stringRedisTemplate;


	@Override
	@GetMapping("getuser")
	public GetUserExtResult getUserext(@RequestParam("username") String userName) {
		if (StringUtils.isEmpty(userName)){
			return new GetUserExtResult(UserCode.USER_USERNAME_CHECK_ERROR,null);
		}
		return userService.getUserExt(userName);
	}

	@Override
	@PostMapping("reguser")
	public ResponseResult registerUser(@RequestParam("username") String username,@RequestParam("phone") String phone,
									   @RequestParam("nickname") String nickname) {
		if (StringUtils.isEmpty(username)){
			return new ResponseResult(UserCode.USER_USERNAME_NONE);
		}
		if (StringUtils.isEmpty(phone)){
			return new ResponseResult(UserCode.USER_PHONE_NONE);
		}
		if (StringUtils.isEmpty(nickname)){
			return new ResponseResult(UserCode.USER_NICKNAME_NONE);
		}
		LeUserBasic user = new LeUserBasic();
		user.setNickname(nickname);
		user.setUsername(username);
		user.setPhone(phone);
		GetUserExtResult userExt = userService.getUserExt(username);
		if (userExt != null){
			return new ResponseResult(UserCode.USER_ACCOUNT_EXISTS);
		}
		Integer result = userService.createUser(user);
		if (result > 0 ){
			//授权
			LeUserRole role = new LeUserRole();
			userExt = userService.getUserExt(username);
			role.setUserId(userExt.getLeUserExt().getId());
			role.setRoleId(2); // ROLE_USER 2
			userService.createUserRole(role);
			return new ResponseResult(CommonCode.SUCCESS);
		}

		return  new ResponseResult(UserCode.USER_RIG_CHECK_ERROR);
	}


	/*@Override
	@GetMapping("createuser")
	public GetUserExtResult createUser(@RequestParam("username") String userName) {
		if (StringUtils.isEmpty(userName)){
			return new GetUserExtResult(UserCode.USER_USERNAME_CHECK_ERROR,null);
		}
		return userService.getUserExt(userName);
	}*/

}
