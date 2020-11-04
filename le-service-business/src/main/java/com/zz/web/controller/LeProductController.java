package com.zz.web.controller;

import com.zz.business.feign.PictureService;
import com.zz.business.feign.ProviderService;
import com.zz.business.service.LeProductService;
import com.zz.framework.api.business.ProductControllerApi;
import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.LeProduct;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.business.LeProductPicurl;
import com.zz.framework.domain.business.response.BusinessCode;
import com.zz.framework.domain.business.response.GetLeProductPicMenuExtResult;
import com.zz.framework.domain.business.response.ProductCode;
import com.zz.framework.domain.picture.response.PictureCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @ClassName LeBusinessDetailController
 * @Description: TODO 产品服务
 * @Author zhangzhen
 * @Date 2020/5/17
 * @Version V1.0
 **/

@RestController
@RequestMapping("product")
public class LeProductController implements ProductControllerApi {
	@Autowired
	LeProductService leProductService;
	@Autowired
	PictureService pictureService;
	@Autowired
	public ProviderService providerService;
	private Pattern lonPattern = Pattern.compile("^[\\-\\+]?(0?\\d{1,2}\\.\\d{1,5}|1[0-7]?\\d{1}\\.\\d{1,5}|180\\.0{1,5})$");
	private Pattern latPattern = Pattern.compile("^[\\-\\+]?([0-8]?\\d{1}\\.\\d{1,5}|90\\.0{1,5})$");
	//查找某一个商品所有的信息 包括图片 菜单 商品信息
	@Override
	@GetMapping("/getbyid")
	public GetLeProductPicMenuExtResult getProductById(@RequestParam("id") int id) {
		return leProductService.getLeProduct(id);
	}

	//查询所有商品信息
	@Override
	@GetMapping("/all")
	@PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
	public QueryResponseResult<LeProduct> getAllProduct() {
		return leProductService.getAll();
	}

	//查询首页商品信息
	@Override
	@GetMapping("/allforhome")
	public QueryResponseResult<LeProduct> getAllForHome(int pageSize,int pageNo,String lon,String lat,String distance) {
		if (pageSize <= 0 ){
			pageSize = 1;
		}
		if (pageNo <= 0 ){
			pageSize = 1;
		}
		if (!StringUtils.isNotEmpty(lon)){
			return new QueryResponseResult(ProductCode.PRODUCT_CHECK_LOCATION_ERROR,null);
		}
		if (!StringUtils.isNotEmpty(lat)){
			return new QueryResponseResult(ProductCode.PRODUCT_CHECK_LOCATION_ERROR,null);
		}
		return leProductService.getAllForHome(pageSize,pageNo,lon,lat,distance);
	}

	@Override
	@PostMapping("saveproduct")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult saveProduct(LeProduct leProduct, int bid) {
		if (bid < 0 ){
			return new ResponseResult(ProductCode.PRODUCT_CHECK_BID_FALSE);
		}
		if (leProduct == null){
			return new ResponseResult(ProductCode.PRODUCT_PARAM_NULL);
		}
		if ("".equals(leProduct.getDescrib())) {
			return new ResponseResult(ProductCode.PRODUCT_DISCRI_NULL);
		}
		if ("".equals(leProduct.getBusinessname())) {
			return new ResponseResult(ProductCode.PRODUCT_BUSINESSNAME_NULL);
		}
		if ("".equals(leProduct.getProductname())) {
			return new ResponseResult(ProductCode.PRODUCT_NAME_NULL);
		}
		if (leProduct.getOriginalprice().equals(new BigDecimal(0))) {
			return new ResponseResult(ProductCode.PRODUCT_PRICE_NULL);
		}
		if (leProduct.getFoodtype() == 0) {
			return new ResponseResult(ProductCode.PRODUCT_FOODTYPE_NULL);
		}
		return leProductService.saveProduct(leProduct,bid);
	}

	@Override
	@PostMapping("saveproductmenu")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult saveProductMenu(List<LeProductMenudetail> leProductMenudetails, int pid) {
		if (pid < 0){
			return new ResponseResult(ProductCode.PRODUCT_CHECK_PID_FALSE);
		}
		return leProductService.saveProductMenu(leProductMenudetails,pid);
	}
//, consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	@Override
	@PostMapping(value = "saveproductpic")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult saveProductPic(@RequestPart MultipartFile file,@RequestParam String pid) {
//		String pid = request.getParameter("pid");
		if (Integer.parseInt(pid) < 0){
			return new ResponseResult(ProductCode.PRODUCT_CHECK_PID_FALSE);
		}
		ResponseResultWithData result = pictureService.uploadProPic(file, pid);
		if (result == null ) return new ResponseResult(PictureCode.PICTURE_UPLOAD_ERROR);
		int code = result.getCode();
		if (code != 10000){
			return new ResponseResult(PictureCode.PICTURE_UPLOAD_ERROR);
		}
		HashMap<String, Object> data = result.getData();
		String picUrl = (String)data.get("downFileUrl");
		LeProductPicurl leProductPicurl = new LeProductPicurl();
		leProductPicurl.setPicurl(picUrl);
		leProductPicurl.setPid(Integer.parseInt(pid));
		return leProductService.saveProductPic(leProductPicurl,Integer.parseInt(pid) );
	}

	@Override
	@GetMapping("delproductpic")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult delProductPic(String pid, String url) {
		if (StringUtils.isNotEmpty(pid) && StringUtils.isNotEmpty(url)) {
			return leProductService.delBusinessPic(pid, url);
		}
		return new ResponseResult(BusinessCode.BUSINESS_CHECK_ID_FALSE);
	}

	//测试服务提供者
	@GetMapping("/hi")
	public String hi(@RequestParam("msg") String msg){
		System.out.println(11);
		String hi = providerService.hi(msg);
		if (StringUtils.isNotEmpty(hi)) return hi;
		return "111";
	}

	/**
	 * 校验经度 对则返回true 错返回false
	 *
	 * @param lon
	 * @return
	 */
	private boolean checkLon(String lon) {
		return (lonPattern.matcher(lon).matches());
	}

	/**
	 * 校验纬度 对则返回true 错返回false
	 *
	 * @param lat
	 * @return
	 */
	private boolean checkLat(String lat) {
		return (latPattern.matcher(lat).matches());
	}

}
