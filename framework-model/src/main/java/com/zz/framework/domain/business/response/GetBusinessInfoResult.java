package com.zz.framework.domain.business.response;/**
 * Created by zhangzhen on 2020/5/16
 */

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResultCode;
import com.zz.framework.domain.business.LeBusiness;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName GetBusinessInfoResult
 * @Description: TODO 获取商家信息结果
 * @Author zhangzhen
 * @Date 2020/5/16 
 * @Version V1.0
 **/
@Data
public class GetBusinessInfoResult extends ResponseResult {

	public LeBusiness leBusiness;
	public GetBusinessInfoResult(ResultCode resultCode, LeBusiness leBusiness) {
		super(resultCode);
		this.leBusiness = leBusiness;
	}
}
