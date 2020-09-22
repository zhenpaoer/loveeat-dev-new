package com.zz.framework.api.auth;

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.user.request.LoginRequest;
import com.zz.framework.domain.user.response.JwtResult;
import com.zz.framework.domain.user.response.LoginResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangzhen on 2019/7/11
 */
@Api(value = "用户认证",description = "用户认证接口")
public interface AuthControllerApi {
	@ApiOperation("登录")
	public LoginResult login(LoginRequest loginRequest);

	@ApiOperation("退出")
	public ResponseResult logout(HttpServletRequest request);

	@ApiOperation("查询userjwt令牌")
	public JwtResult userjwt(HttpServletRequest request);


}
