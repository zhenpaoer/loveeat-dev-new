package com.zz.business.service.impl;/**
 * Created by zhangzhen on 2020/5/17
 */

import com.zz.business.dao.LeProductMapper;
import com.zz.business.dao.LeProductPicurlMapper;
import com.zz.business.feign.PictureService;
import com.zz.business.service.LeBusinessDetailService;
import com.zz.business.service.LeProductMenudetailService;
import com.zz.business.service.LeProductService;
import com.zz.business.service.LeProductUrlService;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.QueryResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeBusinessDetail;
import com.zz.framework.domain.business.LeProduct;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.business.LeProductPicurl;
import com.zz.framework.domain.business.ext.LeProductMenuNode;
import com.zz.framework.domain.business.ext.LeProductPicMenuExt;
import com.zz.framework.domain.business.response.BusinessCode;
import com.zz.framework.domain.business.response.GetLeProductPicMenuExtResult;
import com.zz.framework.domain.business.response.ProductCode;
import jdk.nashorn.internal.runtime.options.LoggingOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName LeProductServiceImpl
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/5/17 
 * @Version V1.0
 **/
@Service
@Slf4j
public class LeProductServiceImpl implements LeProductService {
	@Autowired
	LeProductMapper leProductMapper;
	@Autowired
	LeProductUrlService leProductUrlService;
	@Autowired
	LeProductMenudetailService leProductMenudetailService;
	@Autowired
	LeBusinessDetailService leBusinessDetailService;
	@Autowired
	PictureService pictureService;
	@Autowired
	LeProductPicurlMapper leProductPicurlMapper;


	//查找某一个商品所有的信息 包括图片 菜单 商品信息
	@Override
	public GetLeProductPicMenuExtResult getLeProduct(int id) {
		LeProduct leProduct = leProductMapper.selectByPrimaryKey(id);
		if (leProduct == null ){
			return  new GetLeProductPicMenuExtResult(ProductCode.PRODUCT_NOTCOMPLETE,null);
		}
		List<LeProductMenuNode> productMenuByPid = leProductMenudetailService.getProductMenuByPid(id);
		List<LeProductPicurl> productUrlByPid = leProductUrlService.getProductUrlByPid(id);
		if ( productMenuByPid.size() == 0 || productUrlByPid.size() == 0){
			return  new GetLeProductPicMenuExtResult(ProductCode.PRODUCT_NOTEXIT,null);
		}
		LeProductPicMenuExt leProductPicMenuExt = new LeProductPicMenuExt();
		leProductPicMenuExt.setLeProduct(leProduct);
		leProductPicMenuExt.setProductMenuNodes(productMenuByPid);
		leProductPicMenuExt.setProductPicurls(productUrlByPid);
		GetLeProductPicMenuExtResult productDetail = new GetLeProductPicMenuExtResult(CommonCode.SUCCESS,leProductPicMenuExt);
		return productDetail;
	}

	//查找所有商品
	@Override
	public QueryResponseResult<LeProduct> getAll() {
		List<LeProduct> allProductForUser = leProductMapper.getAllProductForUser();
		QueryResult<LeProduct> QueryResult = new QueryResult<LeProduct>();
		QueryResult.setList(allProductForUser);
		QueryResult.setTotal(allProductForUser.size());
		QueryResponseResult<LeProduct> responseResult = new QueryResponseResult<>(CommonCode.SUCCESS,QueryResult);
		return responseResult;
	}

	//给首页查找所有商品
	@Override
	public QueryResponseResult<LeProduct> getAllForHome() {
		List<LeProduct> leProducts = leProductMapper.selectAll();
		//筛选审核通过和启用状态的
		List<LeProduct> filterLeProducts = leProducts.parallelStream().filter(leProduct -> leProduct.getState() != null && leProduct.getState() == 1 && "审核通过".equals(leProduct.getStatus())).collect(Collectors.toList());
		QueryResult<LeProduct> QueryResult = new QueryResult<LeProduct>();
		QueryResult.setList(filterLeProducts);
		QueryResult.setTotal(filterLeProducts.size());
		QueryResponseResult<LeProduct> responseResult = new QueryResponseResult<>(CommonCode.SUCCESS,QueryResult);

		return responseResult;
	}

	//创建商品信息
	@Override
	public ResponseResult createLeProduct(LeProduct leProduct) {
		int result = leProductMapper.insert(leProduct);
		if (result == 1){
			return new ResponseResult(CommonCode.SUCCESS);
		}
		return new ResponseResult(ProductCode.PRODUCT_CREATE_FALSE);
	}

	//保存商家的商品主要信息
	@Override
	public ResponseResult saveProduct(LeProduct leProduct, int bid) {
		try{
			//根据商家id查询是否存在
			LeBusinessDetail leBusinessDetail = leBusinessDetailService.getLeBusinessDetail(bid);
			if (leBusinessDetail == null){
				return new ResponseResult(ProductCode.PRODUCT_BID_NOTEXIT);
			}
			leProduct.setBid(bid);
			if ( leProduct.getId()== null || leProduct.getId() == 0){
				//根据商品名称查询 此商品是否存在了
				Example example = new Example(LeProduct.class);
				Example.Criteria criteria = example.createCriteria();
				criteria.andEqualTo("productname",leProduct.getProductname());
				List<LeProduct> leProducts = leProductMapper.selectByExample(example);
				//商品名称存在
				if(leProducts.size() > 0){
					return new ResponseResult(ProductCode.PRODUCT_NAME_EXIT);
				}
				//商品名称不存在，创建新商品
				return createLeProduct(leProduct);
			} else {
				//商品存在需要更新，先查库
				LeProduct localLeProduct = leProductMapper.selectByPrimaryKey(leProduct.getId());
				localLeProduct.setBusinessname(leProduct.getBusinessname());
				localLeProduct.setProductname(leProduct.getProductname());
				localLeProduct.setDescrib(leProduct.getDescrib());
				localLeProduct.setFoodtype(leProduct.getFoodtype());
				localLeProduct.setOriginalprice(leProduct.getOriginalprice());
				localLeProduct.setUpdatetime(LocalDateTime.now());
				localLeProduct.setState(2);
				localLeProduct.setStatus("审核中");
				int update = leProductMapper.updateByPrimaryKey(localLeProduct);
				if (update == 0){
					return new ResponseResult(ProductCode.PRODUCT_UPDATE_ERROR);
				}
				return new ResponseResult(CommonCode.SUCCESS);
			}
		}catch (Exception e){
			System.out.println(e);
		}
		return new ResponseResult(ProductCode.PRODUCT_OTHER_ERROR);
	}
	//保存商家的商品菜单信息
	@Override
	public ResponseResult saveProductMenu(List<LeProductMenudetail> leProductMenudetails, int pid) {
		if (leProductMenudetails.size() != 0 && leProductMenudetails.size() != 1){
			//先查pid
			LeProduct leProduct = leProductMapper.selectByPrimaryKey(pid);
			if (leProduct == null){
				return new ResponseResult(ProductCode.PRODUCT_NOTEXIT);
			}
			leProductMenudetails.parallelStream().forEach(item -> item.setPid(pid));
			return leProductMenudetailService.saveProductMenu(leProductMenudetails);
		}else {
			return  new ResponseResult(ProductCode.PRODUCT_CHECK_MENU_ERROR);
		}

	}

	@Override
	public ResponseResult saveProductPic(LeProductPicurl leProductPicurl, int pid) {
		if (leProductPicurl  != null ){
			//先查pid
			LeProduct leProduct = leProductMapper.selectByPrimaryKey(pid);
			if (leProduct == null){
				return new ResponseResult(ProductCode.PRODUCT_NOTEXIT);
			}
			//批量更新图片记录
//			return leProductUrlService.saveLeProductPicurls(leProductPicurls);
			//更新某一个图片记录
			return leProductUrlService.saveLeProductPicurl(leProductPicurl);
		}else {
			return  new ResponseResult(ProductCode.PRODUCT_CHECK_MENU_ERROR);
		}
	}

	//删除商品图片
	@Override
	public ResponseResult delBusinessPic(String pid, String url) {

		LeProductPicurl leProductPicurl = leProductPicurlMapper.selectByPrimaryKey(pid);
		if (leProductPicurl != null && StringUtils.isNotEmpty(leProductPicurl.getPicurl())){
			if (url.equals(leProductPicurl.getPicurl())){
				boolean delResult = pictureService.removePic(url);
				int delPicResult = leProductPicurlMapper.deleteByPrimaryKey(pid);
				if (delResult && delPicResult > 0) return new ResponseResult(CommonCode.SUCCESS);
			}
		}
		return new ResponseResult(ProductCode.PRODUCT_DELETE_PICTURE_FALSE);
	}
}
