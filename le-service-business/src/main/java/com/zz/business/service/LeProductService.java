package com.zz.business.service;

import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.LeProduct;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.business.LeProductPicurl;
import com.zz.framework.domain.business.response.GetLeProductPicMenuExtResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zhangzhen on 2020/5/17
 */
public interface LeProductService {
	//根据id获取商品信息
	public GetLeProductPicMenuExtResult getLeProduct(int id);

	//给用户展示的商品
	public  QueryResponseResult<LeProduct> getAll();

	//获取所有商品信息
	public QueryResponseResult<LeProduct> getAllForHome(int pageSize,int pageNo,String lon,String lat,String distance,int cityId,int regionId,int areaId,String productType,String priceType, String sortType,int uid);

	//创建商品信息
	public ResponseResult createLeProduct(LeProduct leProduct);

	//保存商家的商品主要信息
	ResponseResult saveProduct(LeProduct leProduct, int bid);

	//保存商品菜单
	ResponseResult saveProductMenu(List<LeProductMenudetail> leProductMenudetails, int pid);

	//保存商品url
	ResponseResult saveProductPic(LeProductPicurl leProductPicurl, int pid);

	//删除商品图片url
	ResponseResult delBusinessPic(String pid, String url);

	//砍价
	ResponseResultWithData bargain(int pid,int uid);

	ResponseResult updateProductIsSaleByPid(int pid, int issale);
}
