package com.zz.framework.domain.user.response;

import com.google.common.collect.ImmutableMap;
import com.zz.framework.common.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@ToString
public enum UserCode implements ResultCode {
	USER_USERNAME_NONE(false,23001,"请输入账号！"),
	USER_PASSWORD_NONE(false,23002,"请输入密码！"),
	USER_VERIFYCODE_NONE(false,23003,"请输入验证码！"),
	USER_ACCOUNT_NOTEXISTS(false,23004,"账号不存在！"),
	USER_CREDENTIAL_ERROR(false,23005,"账号或密码错误！"),
	USER_USERNAME_CHECK_ERROR(false,23006,"用户名校验错误！"),
	USER_LOGIN_ERROR(false,23006,"登陆过程出现异常请尝试重新操作！");

	//操作代码
	@ApiModelProperty(value = "操作是否成功", example = "true", required = true)
	boolean success;

	//操作代码
	@ApiModelProperty(value = "操作代码", example = "22001", required = true)
	int code;
	//提示信息
	@ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
	String message;
	private UserCode(boolean success, int code, String message){
		this.success = success;
		this.code = code;
		this.message = message;
	}
	private static final ImmutableMap<Integer, UserCode> CACHE;

	static {
		final ImmutableMap.Builder<Integer, UserCode> builder = ImmutableMap.builder();
		for (UserCode code : values()) {
			builder.put(code.code(), code);
		}
		CACHE = builder.build();
	}

	@Override
	public boolean success() {
		return success;
	}

	@Override
	public int code() {
		return code;
	}

	@Override
	public String message() {
		return message;
	}
}
