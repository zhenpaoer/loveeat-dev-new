package com.zz.web.controller;/**
 * Created by zhangzhen on 2020/5/17
 */

import com.zz.business.dao.LeBusinessDetailMapper;
import com.zz.business.feign.PictureService;
import com.zz.business.service.LeBusinessDetailService;
import com.zz.framework.api.business.BusinessDetailControllerApi;
import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.exception.ExceptionCatch;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.LeBusinessDetail;
import com.zz.framework.domain.business.LeProductPicurl;
import com.zz.framework.domain.business.ext.LeProductPicMenuExt;
import com.zz.framework.domain.business.response.BusinessCode;
import com.zz.framework.domain.business.response.GetBusinessDetailResult;
import com.zz.framework.domain.business.response.ProductCode;
import com.zz.framework.domain.picture.response.PictureCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

/**
 * @ClassName LeBusinessDetailController
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/5/17 
 * @Version V1.0
 **/

@RestController
@RequestMapping("businessdetail")
public class LeBusinessDetailController implements BusinessDetailControllerApi {
	@Autowired
	LeBusinessDetailService leBusinessDetailService;
	@Autowired
	PictureService pictureService;
	@Autowired
	LeBusinessDetailMapper leBusinessDetailMapper;

	//查询商家分店信息
	@Override
	@GetMapping("/getBusDeById")
	public GetBusinessDetailResult getBusDeById(@RequestParam int id) {
		if (id < 0 ){
			ExceptionCast.cast(BusinessCode.BUSINESS_CHECK_ID_FALSE);
		}
		LeBusinessDetail leBusinessDetail = leBusinessDetailService.getLeBusinessDetail(id);
		if (leBusinessDetail == null){
			ExceptionCast.cast(BusinessCode.BUSINESS_NOTEXIT);
		}
		return new GetBusinessDetailResult(CommonCode.SUCCESS,leBusinessDetail);

	}
	//查询商家信息
	@Override
	@GetMapping("/getBusDeList")
//	@PreAuthorize(value = "hasRole('ROLE_ADMIN')" )
	public QueryResponseResult<LeBusinessDetail> getBusDeList() {
		return leBusinessDetailService.getAll();
	}

	//根据商家id 查询他下面所有商品的信息
	@Override
	public QueryResponseResult<LeProductPicMenuExt> getAllProductByBussinessId() {
		return null;
	}

	//创建商家信息
	@Override
	@PostMapping("/createBusinessDetail")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult createBusinessDetail(LeBusinessDetail leBusinessDetail) {
		return  leBusinessDetailService.createLeBusinessDetail(leBusinessDetail);
	}

	//修改商家分店信息
	@Override
	@PostMapping("/updateBusinessDetail")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult updateBusinessDetail(LeBusinessDetail leBusinessDetail) {
		return leBusinessDetailService.updateBusinessDetail(leBusinessDetail);
	}

	//对商家信息进行审核通过
	@Override
	@GetMapping("/passBusinessDetail/{id}")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN')")
	public ResponseResult passBusinessDetail(@PathVariable("id") int leBusinessDetailId) {
		return leBusinessDetailService.passBusinessDetail(leBusinessDetailId);
	}

	//对商家信息进行审核不通过
	@Override
	@PostMapping("/notPassBusinessDetail")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN')")
	public ResponseResult notPassBusinessDetail(@PathVariable("id") int id ,@PathVariable("reason") String reason ) {
		return leBusinessDetailService.notPassBusinessDetail(id,reason);
	}
	//对商家下架
	@Override
	@GetMapping("/downBusinessDetail/{id}")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN')")
	public ResponseResult downBusinessDetail(int id) {
		return leBusinessDetailService.downBusinessDetail(id);
	}

	//商家图片上传
	@Override
	@PostMapping(value = "savebusinesspic")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult saveBusinessPic(@RequestPart MultipartFile file, @RequestParam String bid) { //分店id
		if (Integer.parseInt(bid) < 0){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_PID_FALSE);
		}
		ResponseResultWithData result = pictureService.uploadBusPic(file, bid);
		if (result == null ) ExceptionCast.cast(PictureCode.PICTURE_UPLOAD_ERROR);
		int code = result.getCode();
		if (code != 10000){
			ExceptionCast.cast(PictureCode.PICTURE_UPLOAD_ERROR);
		}
		HashMap<String, Object> data = result.getData();
		String picUrl = (String)data.get("downFileUrl");
		LeBusinessDetail leBusinessDetail1 = leBusinessDetailService.getLeBusinessDetail(Integer.parseInt(bid));
		if (leBusinessDetail1 == null){
			ExceptionCast.cast(BusinessCode.BUSINESS_NOTEXIT);
		}
		leBusinessDetail1.setUrl(picUrl);
		int updateResult = leBusinessDetailMapper.updateByPrimaryKey(leBusinessDetail1);
		if (updateResult <= 0){
			ExceptionCast.cast(BusinessCode.BUSINESSDETAIL_UPDATE_FALSE);
		}
		return new ResponseResult(CommonCode.SUCCESS);

	}


	@Override
	@PostMapping(value = "delbusinesspic")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult delBusinessPic(String bid, String url) {
		if (StringUtils.isEmpty(bid) || StringUtils.isEmpty(url)){
			ExceptionCast.cast(BusinessCode.BUSINESS_CHECK_ID_FALSE);
		}
		return leBusinessDetailService.delBusinessPic(bid,url);

	}

}
