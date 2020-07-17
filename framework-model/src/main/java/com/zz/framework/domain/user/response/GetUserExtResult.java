package com.zz.framework.domain.user.response;

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResultCode;
import com.zz.framework.domain.business.LeBusiness;
import com.zz.framework.domain.user.ext.LeUserExt;

/**
 * @ClassName GetUserExtResult
 * @Description: TODO 获取用户信息结果
 * @Author zhangzhen
 * @Date 2020/7/10
 * @Version V1.0
 **/
public class GetUserExtResult extends ResponseResult {

	public LeUserExt leUserExt;
	public GetUserExtResult(){}
	public GetUserExtResult(ResultCode resultCode, LeUserExt leUserExt) {
		super(resultCode);
		this.leUserExt = leUserExt;
	}

	public LeUserExt getLeUserExt() {
		return leUserExt;
	}

	public void setLeUserExt(LeUserExt leUserExt) {
		this.leUserExt = leUserExt;
	}

}
