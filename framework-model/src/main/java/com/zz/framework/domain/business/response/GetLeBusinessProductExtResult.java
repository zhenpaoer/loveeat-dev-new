package com.zz.framework.domain.business.response;/**
 * Created by zhangzhen on 2020/5/17
 */

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResultCode;
import com.zz.framework.domain.business.ext.LeBusinessProductExt;
import lombok.Data;

/**
 * @ClassName GetLeBusinessProductExtResult
 * @Description: TODO 商家包含商品信息
 * @Author zhangzhen
 * @Date 2020/5/17 
 * @Version V1.0
 **/
@Data
public class GetLeBusinessProductExtResult extends ResponseResult {
	private LeBusinessProductExt leBusinessProductExt;
	public GetLeBusinessProductExtResult(ResultCode resultCode, LeBusinessProductExt leBusinessProductExt) {
		super(resultCode);
		this.leBusinessProductExt = leBusinessProductExt;
	}
}
