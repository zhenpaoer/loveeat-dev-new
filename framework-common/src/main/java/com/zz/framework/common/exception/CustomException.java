package com.zz.framework.common.exception;


import com.zz.framework.common.model.response.ResultCode;

/**
 * Created by zhangzhen on 2019/6/12
 */
public class CustomException extends RuntimeException {

	//错误代码
	ResultCode resultCode;

	public CustomException(ResultCode resultCode){
		//异常信息为错误代码+异常信息
		super("错误代码："+resultCode.code()+"错误信息："+resultCode.message());
		this.resultCode = resultCode;
	}
	public ResultCode getResultCode(){
		return this.resultCode;
	}
}
