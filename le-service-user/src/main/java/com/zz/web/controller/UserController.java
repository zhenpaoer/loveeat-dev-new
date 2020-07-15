package com.zz.web.controller;

import com.zz.framework.api.user.UserControllerApi;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.response.BusinessCode;
import com.zz.framework.domain.business.response.GetBusinessInfoResult;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.GetUserExtResult;
import com.zz.framework.domain.user.response.UserCode;
import com.zz.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController implements UserControllerApi {
	@Autowired
	UserService userService;

	@Override
	@GetMapping("getuser")
	public GetUserExtResult getUserext(@RequestParam("username") String userName) {
		if (StringUtils.isEmpty(userName)){
			return new GetUserExtResult(UserCode.USER_USERNAME_CHECK_ERROR,null);
		}
		return userService.getUserExt(userName);
	}
}
