package com.zz.framework.api.business;

import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeBusiness;
import com.zz.framework.domain.business.response.GetBusinessInfoResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by zhangzhen on 2020/5/16
 */


@Api(value = "商家管理",description = "商家管理接口，提供商家的增、删、改、查",tags = {"商家管理"})
public interface BusinessControllerApi {
	//查询商家信息
	@ApiOperation("查询某一个商家")
	public GetBusinessInfoResult getBusById(int id);

	//查询商家信息
	@ApiOperation("查询所有商家")
	public QueryResponseResult<LeBusiness> getBusList();

	//查询商家信息
	@ApiOperation("创建商家")
	public ResponseResult createBusiness(LeBusiness leBusiness);

}
