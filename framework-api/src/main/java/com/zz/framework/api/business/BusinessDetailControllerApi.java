package com.zz.framework.api.business;

import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeBusinessDetail;
import com.zz.framework.domain.business.ext.LeProductPicMenuExt;
import com.zz.framework.domain.business.response.GetBusinessDetailResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by zhangzhen on 2020/5/17
 */

@Api(value = "商家分店管理",description = "商家分店管理接口，提供商家分店的增、删、改、查",tags = {"商家分店管理"})
public interface BusinessDetailControllerApi {
	//查询商家分店信息
	@ApiOperation("查询某一个商家分店")
	public GetBusinessDetailResult getBusDeById(int id);

	//查询商家信息
	@ApiOperation("查询所有商家分店")
	public QueryResponseResult<LeBusinessDetail> getBusDeList();

	//根据商家id 查询他下面所有商品的信息
	@ApiOperation("根据商家id 查询他下面所有商品的信息")
	public QueryResponseResult<LeProductPicMenuExt> getAllProductByBussinessId();

	//创建商家信息
	@ApiOperation("创建商家分店")
	public ResponseResult createBusinessDetail(LeBusinessDetail leBusinessDetail);

	//修改商家分店信息
	@ApiOperation("修改商家分店")
	public ResponseResult updateBusinessDetail(LeBusinessDetail leBusinessDetail);

	//对商家信息进行审核通过
	@ApiOperation("审核通过商家分店信息")
	public ResponseResult passBusinessDetail(int leBusinessDetailId);

	//对商家信息进行审核不通过
	@ApiOperation("审核不通过商家分店信息")
	public ResponseResult notPassBusinessDetail(int id,String reason);

	//商家下架
	@ApiOperation("下架商家分店")
	public ResponseResult downBusinessDetail(int id);

	//商家图片上传
	@ApiOperation("商家图片上传")
	public ResponseResult saveBusinessPic( MultipartFile file,  String bdid);

	//商家图片删除
	@ApiOperation("商家图片删除")
	public ResponseResult delBusinessPic(String bid , String url);
}
