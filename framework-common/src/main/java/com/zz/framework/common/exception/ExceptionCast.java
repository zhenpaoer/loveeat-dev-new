package com.zz.framework.common.exception;


import com.zz.framework.common.model.response.ResultCode;

/**
 * Created by zhangzhen on 2019/6/12
 */
public class ExceptionCast  {
	//使用此静态方法抛出自定义异常
	public static void cast(ResultCode resultCode){
		throw new CustomException(resultCode);
	}
}
