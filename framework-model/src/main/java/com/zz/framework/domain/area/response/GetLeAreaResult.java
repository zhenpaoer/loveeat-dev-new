package com.zz.framework.domain.area.response;/**
 * Created by zhangzhen on 2020/5/17
 */

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResultCode;
import com.zz.framework.domain.area.ext.LeAreaNode;
import com.zz.framework.domain.business.ext.LeBusinessProductExt;
import lombok.Data;

import java.util.List;

/**
 * @ClassName GetLeAreaResult
 * @Description: TODO 返回区域信息
 * @Author zhangzhen
 * @Date 2020/11/5
 * @Version V1.0
 **/
@Data
public class GetLeAreaResult extends ResponseResult {
	private List<LeAreaNode> LeAreaNode;
	public GetLeAreaResult(ResultCode resultCode, List<LeAreaNode> LeAreaNode) {
		super(resultCode);
		this.LeAreaNode = LeAreaNode;
	}
}
