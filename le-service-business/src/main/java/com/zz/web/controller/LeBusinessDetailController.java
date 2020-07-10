package com.zz.web.controller;/**
 * Created by zhangzhen on 2020/5/17
 */

import com.zz.business.dao.LeBusinessDetailMapper;
import com.zz.business.feign.PictureService;
import com.zz.business.service.LeBusinessDetailService;
import com.zz.framework.api.business.BusinessDetailControllerApi;
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
	@GetMapping("/getBusDeById/{id}")
	public GetBusinessDetailResult getBusDeById(int id) {
		if (id < 0 ){
			return new GetBusinessDetailResult(BusinessCode.BUSINESS_CHECK_ID_FALSE,null);
		}
		LeBusinessDetail leBusinessDetail = leBusinessDetailService.getLeBusinessDetail(id);
		if (leBusinessDetail != null){
			return new GetBusinessDetailResult(CommonCode.SUCCESS,leBusinessDetail);
		}
		return new GetBusinessDetailResult(BusinessCode.BUSINESS_NOTEXIT,null);
	}
	//查询商家信息
	@Override
	@GetMapping("/getBusDeList")
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
	public ResponseResult createBusinessDetail(LeBusinessDetail leBusinessDetail) {
		return  leBusinessDetailService.createLeBusinessDetail(leBusinessDetail);
	}

	//修改商家分店信息
	@Override
	@PostMapping("/updateBusinessDetail")
	public ResponseResult updateBusinessDetail(LeBusinessDetail leBusinessDetail) {
		return leBusinessDetailService.updateBusinessDetail(leBusinessDetail);
	}

	//对商家信息进行审核通过
	@Override
	@GetMapping("/passBusinessDetail/{id}")
	public ResponseResult passBusinessDetail(@PathVariable("id") int leBusinessDetailId) {
		return leBusinessDetailService.passBusinessDetail(leBusinessDetailId);
	}

	//对商家信息进行审核不通过
	@Override
	@PostMapping("/notPassBusinessDetail")
	public ResponseResult notPassBusinessDetail(@PathVariable("id") int id ,@PathVariable("reason") String reason ) {
		return leBusinessDetailService.notPassBusinessDetail(id,reason);
	}
	//对商家下架
	@Override
	@GetMapping("/downBusinessDetail/{id}")
	public ResponseResult downBusinessDetail(int id) {
		return leBusinessDetailService.downBusinessDetail(id);
	}

	@Override
	@PostMapping(value = "savebusinesspic")
	public ResponseResult saveBusinessPic(@RequestPart MultipartFile file, @RequestParam String bid) { //分店id
		if (Integer.parseInt(bid) < 0){
			return new ResponseResult(ProductCode.PRODUCT_CHECK_PID_FALSE);
		}
		ResponseResultWithData result = pictureService.uploadBusPic(file, bid);
		if (result == null ) return new ResponseResult(PictureCode.PICTURE_UPLOAD_ERROR);
		int code = result.getCode();
		if (code != 10000){
			return new ResponseResult(PictureCode.PICTURE_UPLOAD_ERROR);
		}
		HashMap<String, Object> data = result.getData();
		String picUrl = (String)data.get("downFileUrl");
		LeBusinessDetail leBusinessDetail1 = leBusinessDetailService.getLeBusinessDetail(Integer.parseInt(bid));
		if (leBusinessDetail1 != null){
			leBusinessDetail1.setUrl(picUrl);
			int updateResult = leBusinessDetailMapper.updateByPrimaryKey(leBusinessDetail1);
			if (updateResult == 1){
				return new ResponseResult(CommonCode.SUCCESS);
			}
			return  new ResponseResult(BusinessCode.BUSINESSDETAIL_UPDATE_FALSE);
		}
		return new ResponseResult(BusinessCode.BUSINESS_NOTEXIT);
	}

	@Override
	@GetMapping("delbusinesspic")
	public ResponseResult delBusinessPic(String bid, String url) {
		if (StringUtils.isNotEmpty(bid) && StringUtils.isNotEmpty(url)){
			return leBusinessDetailService.delBusinessPic(bid,url);
		}
		return new ResponseResult(BusinessCode.BUSINESS_CHECK_ID_FALSE);
	}

}
