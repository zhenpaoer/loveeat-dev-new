package com.zz.framework.api.user;

import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.GetUserExtResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by zhangzhen
 */
@Api(value = "用户中心",description = "用户中心管理")
public interface UserControllerApi {
	@ApiOperation("根据账号查询用户信息")
	public GetUserExtResult getUserext(String username);

}
