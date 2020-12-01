package com.zz.framework.api.picturl;

import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.LeBusiness;
import com.zz.framework.domain.business.response.GetBusinessInfoResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangzhen on 2020/5/16
 */


@Api(value = "图片服务",description = "图片服务接口，提供上传，下载，预览，删除图片",tags = {"图片服务"})
public interface PicturlControllerApi {

	//上传商家图片
	@ApiOperation("上传图片")
	public ResponseResultWithData uploadBusPic(MultipartFile file,  String bid);

	//上传商品图片
	@ApiOperation("上传商品图片")
	public ResponseResultWithData uploadProPic(MultipartFile file,  String pid);

	//批量上传商品图片
	@ApiOperation("批量上传商品图片")
	public ResponseResultWithData uploadProPicList(HttpServletRequest request, HttpServletResponse response);

	//预览图片
	@ApiOperation("预览图片")
	public void previewPicture(HttpServletRequest request, HttpServletResponse response);

	public boolean removePic(String fileName);
}

