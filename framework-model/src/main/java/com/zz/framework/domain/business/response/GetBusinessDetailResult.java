package com.zz.framework.domain.business.response;/**
 * Created by zhangzhen on 2020/5/17
 */

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResultCode;
import com.zz.framework.domain.business.LeBusinessDetail;
import lombok.Data;

/**
 * @ClassName GetBusinessDetailResult
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/5/17 
 * @Version V1.0
 **/
@Data
public class GetBusinessDetailResult extends ResponseResult {
	public LeBusinessDetail leBusinessDetail;
	public GetBusinessDetailResult(ResultCode resultCode, LeBusinessDetail leBusinessDetail) {
		super(resultCode);
		this.leBusinessDetail = leBusinessDetail;
	}
}
