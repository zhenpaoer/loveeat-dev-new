package com.zz.framework.api.user;

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.request.LoginRequest;
import com.zz.framework.domain.user.response.GetUserExtResult;
import com.zz.framework.domain.user.response.LoginResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangzhen
 */
@Api(value = "用户中心",description = "用户中心管理")
public interface UserControllerApi {
	@ApiOperation("根据账号查询用户信息")
	public GetUserExtResult getUserext(String username);

	@ApiOperation("注册账号")
	public ResponseResult registerUser(String openid, String sessionKey,String phone,String nickname,String avator,String userpic, String address, String lon, String lat);

	@ApiOperation("根据openid查询用户")
	public GetUserExtResult getUserextByOpenid(String openid);

	@ApiOperation("更新用户登陆")
	public ResponseResult updateUserLogin(String nickName, String avatarUrl, String address, String lon, String lat, HttpServletRequest request);

}
