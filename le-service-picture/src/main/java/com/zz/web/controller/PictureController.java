package com.zz.web.controller;

import com.zz.framework.api.business.PicturlControllerApi;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.picture.response.PictureCode;
import com.zz.minio.service.MinioPictureService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@RestController
@RequestMapping("picture")
public class PictureController implements PicturlControllerApi {
	@Resource
	private MinioPictureService minioPictureService;

	/**
	 * 桶名
	 */
	@Value("${minio.bucketName.busBucket}")
	private String busBucket;
	@Value("${minio.bucketName.proBucket}")
	private String proBucket;

	//上传商家图片
	@Override
	@PostMapping(value = "uploadbuspic",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResultWithData uploadBusPic(@RequestParam MultipartFile file,@RequestParam String bid) {
		ResponseResultWithData responseResultWithData = null;
		try {
			String orgName = "";
			String fileName = "";
			if (file != null){
				orgName = file.getOriginalFilename();// 获取文件名
				//想加个序号
				fileName = "bid_" + bid + "_"  + orgName.replaceAll(" ", "_");
			}
			// 步骤一、判断文件是否存在过 存在则不能上传（Minio服务器上传同样位置的同样文件名的文件时，新的文件会把旧的文件覆盖掉）
			/*boolean exist = minioPictureService.isFileExisted(fileName, busBucket);
			if (exist) {
				log.error("文件 " + fileName + " 已经存在");
				return new ResponseResultWithData(PictureCode.PICTURE_HAS_EXIT,null);
			}*/
			// 步骤二、上传文件
			responseResultWithData = minioPictureService.minioUpload(file, fileName, busBucket);
			return responseResultWithData;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return responseResultWithData;
	}

	//上传商品图片
	@Override
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	@PostMapping(value = "uploadpropic",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseResultWithData uploadProPic(@RequestPart MultipartFile file,@RequestParam String pid) {
		ResponseResultWithData responseResultWithData = null;
		try {
//			MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//			MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
//			MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
//			String pid = request.getParameter("pid");// 获取上传文件对象
			String orgName = "";
			String fileName = "";
			if (file != null){
				orgName = file.getOriginalFilename();// 获取文件名
				//想加个序号
				fileName = "pid_" + pid + "_"  + System.currentTimeMillis()+ "_" + orgName.replaceAll(" ", "_");
			}
			// 步骤一、判断文件是否存在过 存在则不能上传（Minio服务器上传同样位置的同样文件名的文件时，新的文件会把旧的文件覆盖掉）
			boolean exist = minioPictureService.isFileExisted(fileName, proBucket);
			if (exist) {
				log.error("文件 " + fileName + " 已经存在");
				return new ResponseResultWithData(PictureCode.PICTURE_HAS_EXIT,null);
			}
			// 步骤二、上传文件
			responseResultWithData = minioPictureService.minioUpload(file, fileName, proBucket);
			return responseResultWithData;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return responseResultWithData;
	}

	//批量上传商品图片
	//todo 暂时先不用
	@Override
	@PostMapping(value = "uploadpropiclist")
	@ResponseBody
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResultWithData uploadProPicList(HttpServletRequest request, HttpServletResponse response) {
		ResponseResultWithData responseResultWithData = null;
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("files");// 获取上传文件对象
			String pid = request.getParameter("pid");// 获取上传文件对象
			String orgName = "";
			String fileName = "";
			if (file != null){
				orgName = file.getOriginalFilename();// 获取文件名
				//想加个序号
				fileName = "pid_" + pid + "_" + orgName.replaceAll(" ", "_");
			}
			// 步骤一、判断文件是否存在过 存在则不能上传（Minio服务器上传同样位置的同样文件名的文件时，新的文件会把旧的文件覆盖掉）
			boolean exist = minioPictureService.isFileExisted(fileName, proBucket);
			if (exist) {
				log.error("文件 " + fileName + " 已经存在");
				return new ResponseResultWithData(PictureCode.PICTURE_HAS_EXIT,null);
			}
			// 步骤二、上传文件
			responseResultWithData = minioPictureService.minioUpload(file, fileName, proBucket);
			return responseResultWithData;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return responseResultWithData;
	}




	/**
	 * 预览图片
	 * 请求地址：http://localhost:8080/picture/preview/{user/20190119/e1fe9925bc315c60addea1b98eb1cb1349547719_1547866868179.jpg}
	 */
	@Override
	@GetMapping(value = "preview/**")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public void previewPicture(HttpServletRequest request, HttpServletResponse response) {
		// ISO-8859-1 ==> UTF-8 进行编码转换
		String imgPath = extractPathFromPattern(request);
		// 其余处理略
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			String bucketName = "";
			String fileName = "";
			response.setContentType("image/jpeg;charset=utf-8");
			if (StringUtils.isNotEmpty(imgPath)){
				String[] split = imgPath.split("/");
				bucketName = split[0];
				fileName = split[1];
			}
			inputStream = minioPictureService.getFileInputStream(fileName,bucketName);
			outputStream = response.getOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0) {
				outputStream.write(buf, 0, len);
			}
			response.flushBuffer();
		} catch (IOException e) {
			log.error("预览图片失败" + e.getMessage());
			// e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	@GetMapping(value = "delpic")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public boolean removePic(@RequestParam String url) {
		if (url.isEmpty()){
			return false;
		}else {
			String fileName = "";
			String bucketName = "";
			String[] split = url.split("/");
			for (String s : split) {
				if (s.startsWith("loveeat")) {
					bucketName = s;
				}
				if (s.endsWith(".jpg")) {
					fileName = s;
				}
			}
			return minioPictureService.delete(bucketName,fileName);
		}
	}

	/**
	 *  把指定URL后的字符串全部截断当成参数
	 *  这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
	 * @param request
	 * @return
	 */
	private static String extractPathFromPattern(final HttpServletRequest request) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
	}

}
