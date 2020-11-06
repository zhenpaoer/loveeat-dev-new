package com.zz.business.service.impl;/**
 * Created by zhangzhen on 2020/5/17
 */

import com.zz.business.dao.LeBusinessDetailMapper;
import com.zz.business.feign.PictureService;
import com.zz.business.service.LeBusinessDetailService;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.QueryResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeBusinessDetail;
import com.zz.framework.domain.business.response.BusinessCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName LeBusinessDetailServiceImpl
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/5/17 
 * @Version V1.0
 **/
@Service
public class LeBusinessDetailServiceImpl implements LeBusinessDetailService {
	@Autowired
	LeBusinessDetailMapper leBusinessDetailMapper;
	@Autowired
	PictureService pictureService;

	//根据id查找
	@Override
	public LeBusinessDetail getLeBusinessDetail(int id) {
		return leBusinessDetailMapper.selectByPrimaryKey(id);
	}

	//查找所有
	@Override
	public QueryResponseResult<LeBusinessDetail> getAll() {
		List<LeBusinessDetail> leBusinessDetails = leBusinessDetailMapper.selectAll();
		QueryResult<LeBusinessDetail> QueryResult = new QueryResult<LeBusinessDetail>();
		QueryResult.setList(leBusinessDetails);
		QueryResult.setTotal(leBusinessDetails.size());
		QueryResponseResult<LeBusinessDetail> responseResult = new QueryResponseResult<>(CommonCode.SUCCESS,QueryResult);

		return responseResult;
	}

	//创建商家
	@Override
	public ResponseResult createLeBusinessDetail(LeBusinessDetail LeBusinessDetail) {

		LeBusinessDetail.setState(2); //禁用
		LeBusinessDetail.setStatus("1"); //审核中

		int result = leBusinessDetailMapper.insert(LeBusinessDetail);
		if (result == 1){
			return new ResponseResult(CommonCode.SUCCESS);
		}
		return new ResponseResult(BusinessCode.BUSINESSDETAIL_CREATE_FALSE);
	}

	//修改商家信息
	@Override
	public ResponseResult updateBusinessDetail(LeBusinessDetail leBusinessDetail) {
		LeBusinessDetail leBusinessDetail1 = getLeBusinessDetail(leBusinessDetail.getId());
		if (leBusinessDetail1 == null){
			new ResponseResult(BusinessCode.BUSINESS_NOTEXIT);
		}
		leBusinessDetail.setState(2); //禁用
		leBusinessDetail.setStatus("审核中"); //审核中
		leBusinessDetail.setModifitime(LocalDateTime.now());
		int updateResult = leBusinessDetailMapper.updateByPrimaryKey(leBusinessDetail);
		if (updateResult == 1){
			return new ResponseResult(CommonCode.SUCCESS);
		}
		return new ResponseResult(BusinessCode.BUSINESSDETAIL_CREATE_FALSE);
	}

	//对商家信息进行审核通过
	@Override
	public ResponseResult passBusinessDetail(int leBusinessDetailId) {
		LeBusinessDetail leBusinessDetail1 = getLeBusinessDetail(leBusinessDetailId);
		if (leBusinessDetail1 == null){
			new ResponseResult(BusinessCode.BUSINESS_NOTEXIT);
		}
		leBusinessDetail1.setState(1);//启用
		leBusinessDetail1.setStatus("审核通过");
		leBusinessDetail1.setModifitime(LocalDateTime.now());
		int updateResult = leBusinessDetailMapper.updateByPrimaryKey(leBusinessDetail1);
		if (updateResult == 1){
			return new ResponseResult(CommonCode.SUCCESS);
		}
		return new ResponseResult(BusinessCode.BUSINESSDETAIL_UPDATE_FALSE);
	}

	//对商家信息进行审核不通过
	@Override
	public ResponseResult notPassBusinessDetail(int leBusinessDetailId, String reason) {
		LeBusinessDetail leBusinessDetail1 = getLeBusinessDetail(leBusinessDetailId);
		if (leBusinessDetail1 == null){
			new ResponseResult(BusinessCode.BUSINESS_NOTEXIT);
		}
		leBusinessDetail1.setState(2);//禁用
		leBusinessDetail1.setStatus("审核不通过（" + reason + ")");
		leBusinessDetail1.setModifitime(LocalDateTime.now());
		int updateResult = leBusinessDetailMapper.updateByPrimaryKey(leBusinessDetail1);
		if (updateResult == 1){
			return new ResponseResult(CommonCode.SUCCESS);
		}
		return new ResponseResult(BusinessCode.BUSINESSDETAIL_UPDATE_FALSE);
	}

	//对商家下架处理
	@Override
	public ResponseResult downBusinessDetail(int id) {
		LeBusinessDetail leBusinessDetail1 = getLeBusinessDetail(id);
		if (leBusinessDetail1 == null){
			new ResponseResult(BusinessCode.BUSINESS_NOTEXIT);
		}
		leBusinessDetail1.setState(3);//下架
		leBusinessDetail1.setStatus("下架处理");
		leBusinessDetail1.setModifitime(LocalDateTime.now());
		int updateResult = leBusinessDetailMapper.updateByPrimaryKey(leBusinessDetail1);
		if (updateResult == 1){
			return new ResponseResult(CommonCode.SUCCESS);
		}
		return new ResponseResult(BusinessCode.BUSINESSDETAIL_UPDATE_FALSE);
	}

	//删除商家图片
	@Override
	public ResponseResult delBusinessPic(String bid, String url) {
		LeBusinessDetail leBusinessDetail = leBusinessDetailMapper.selectByPrimaryKey(bid);
		if (leBusinessDetail != null && StringUtils.isNotEmpty(leBusinessDetail.getUrl())){
			if (url.equals(leBusinessDetail.getUrl())){
				boolean delResult = pictureService.removePic(url);
				leBusinessDetail.setUrl("");
				int updateResult = leBusinessDetailMapper.updateByPrimaryKey(leBusinessDetail);
				if (delResult && updateResult > 0) return new ResponseResult(CommonCode.SUCCESS);
			}
		}
		return new ResponseResult(BusinessCode.BUSINESSDETAIL_DELETE_PICTURE_FALSE);
	}

	//根据区域筛选商家
	@Override
	public List<LeBusinessDetail> getBusinessByAreaConditions(int cityId, int regionId, int areaId){
		List<LeBusinessDetail> getBusinessbyId = new ArrayList<>();
		//查商圈 areid是参数
		if (areaId != 0){
			getBusinessbyId = leBusinessDetailMapper.getByAreaId(areaId);
		}
		//查区域 regionId是参数
		if (areaId == 0){
			getBusinessbyId = leBusinessDetailMapper.getByRegionId(regionId);
		}
		//查城市 cityid是参数
		if(regionId == 0){
			getBusinessbyId = leBusinessDetailMapper.getByCityId(cityId);
		}
		return getBusinessbyId;
	}
}
