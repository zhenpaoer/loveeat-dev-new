package com.zz.framework.common.exception;

import com.google.common.collect.ImmutableMap;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangzhen on 2019/6/12
 */
@Component
@ControllerAdvice //控制增强器
public class ExceptionCatch {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionCatch.class);
	//使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点的一旦创建不可改变，并且线程安全
	private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
	//使用builder来构建一个异常类型和错误代码的异常
	protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder =
			ImmutableMap.builder();

	//捕获 CustomException异常
	@ExceptionHandler(CustomException.class)
	@ResponseBody //对象转Json
	public ResponseResult CatchEec(CustomException e) {
		logger.error("catch exception : {}\r\nexception: ", e.getMessage(), e);
		ResultCode resultCode = e.getResultCode();
		return new ResponseResult(resultCode);
	}

	//捕获Exception异常
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseResult exception(Exception e) {
		logger.error("catch exception : {}\r\nexception: ", e.getMessage(), e);
		if (EXCEPTIONS == null)
			EXCEPTIONS = builder.build();
		final ResultCode resultCode = EXCEPTIONS.get(e.getClass());
		final ResponseResult responseResult;
		if (resultCode != null) {
			responseResult = new ResponseResult(resultCode);
		} else {
			responseResult = new ResponseResult(CommonCode.SERVER_ERROR);
		}
		return responseResult;
	}

	static {
		//在这里加入一些基础的异常类型判断
		builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
//		builder.put(AccessDeniedException.class,CommonCode.UNAUTHORISE);
	}
}
