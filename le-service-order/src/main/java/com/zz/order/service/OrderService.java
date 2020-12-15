package com.zz.order.service;

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {
	public ResponseResult createOrder(int uid, int pid, HttpServletRequest request);
	//查询用户的订单
	public ResponseResultWithData getByUid(int uid);
}
