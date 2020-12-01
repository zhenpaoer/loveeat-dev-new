package com.zz.business.service;

import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeBusinessDetail;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * Created by zhangzhen on 2020/5/17
 */
public interface LeBusinessDetailService {
	//根据id获取商家信息
	public LeBusinessDetail getLeBusinessDetail(int id);

	//获取所有商家信息
	public QueryResponseResult<LeBusinessDetail> getAll();

	//创建商家信息
	public ResponseResult createLeBusinessDetail(LeBusinessDetail LeBusinessDetail);

	//修改商家分店信息
	public ResponseResult updateBusinessDetail(LeBusinessDetail leBusinessDetail);

	//对商家信息进行审核通过
	ResponseResult passBusinessDetail(int leBusinessDetailId);

	//对商家信息进行审核不通过
	ResponseResult notPassBusinessDetail(int leBusinessDetailId,String reason);

	//对商家下架处理
	ResponseResult downBusinessDetail(int id);

	//删除商家图片
	ResponseResult delBusinessPic(String bid, String url);

	//根据区域筛选商家
	List<LeBusinessDetail> getBusinessByAreaConditions(int cityId,int regionId,int areaId);
}
