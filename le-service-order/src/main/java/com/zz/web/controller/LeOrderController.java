package com.zz.web.controller;

import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.user.response.AuthCode;
import com.zz.framework.utils.LeOauth2Util;
import com.zz.order.service.OrderService;
import com.zz.order.service.impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("order")
public class LeOrderController  {
	@Autowired
	OrderService orderService;
	//查询用户的订单
	@GetMapping("/getordersbyuid")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS','ROLE_USER')")
	public ResponseResultWithData getOrdersByUid(int uid, HttpServletRequest request){
		LeOauth2Util.UserJwt userJwt = LeOauth2Util.getUserJwtFromHeader(request);
		if (userJwt == null){
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
		}
		int userId;
		if (uid == 0){
			userId = Integer.parseInt(userJwt.getId());
		}else {
			userId = uid;
		}
		return orderService.getByUid(userId);
	}

	//此场景不适合
	@PostMapping("/createorder")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseResult createOrder(@RequestParam(value = "pid",required = true,defaultValue = "0")Integer pid, HttpServletRequest request){
		LeOauth2Util.UserJwt userJwt = LeOauth2Util.getUserJwtFromHeader(request);
		if (userJwt == null){
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
		}
		int userId = Integer.parseInt(userJwt.getId());
		return orderService.createOrder(userId,pid,request);
	}
}
