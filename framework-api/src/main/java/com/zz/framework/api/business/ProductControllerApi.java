package com.zz.framework.api.business;

import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeProduct;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.business.LeProductPicurl;
import com.zz.framework.domain.business.response.GetLeProductPicMenuExtResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zhangzhen on 2020/6/11
 */

@Api(value = "商品管理",description = "商家商品管理接口，提供商品的增、删、改、查",tags = {"商品管理"})
public interface ProductControllerApi {

	//查询某一个商品信息
	@ApiOperation("查询某一个商品")
	public GetLeProductPicMenuExtResult getProductById(int id);

	//查询所有商品信息
	@ApiOperation("查询所有商品")
	public QueryResponseResult<LeProduct> getAllProduct();

	//查询首页商品信息
	@ApiOperation("查询首页商品信息")
	public QueryResponseResult<LeProduct> getAllForHome(int pageSize,int pageNo,String lon,String lat,String distance,int cityId,int regionId,int areaId);

	//保存商品主要信息
	@ApiOperation("保存商品主要信息")
	public ResponseResult saveProduct(LeProduct leProduct, int bid);

	//保存商品菜单信息
	@ApiOperation("保存商品菜单信息")
	public ResponseResult saveProductMenu(List<LeProductMenudetail> leProductMenudetails, int pid);

	//保存商品图片信息
	@ApiOperation("保存商品图片信息")
	public ResponseResult saveProductPic( MultipartFile file,  String pid);

	//商品图片删除
	@ApiOperation("商品图片删除")
	public ResponseResult delProductPic(String pid , String url);
}
