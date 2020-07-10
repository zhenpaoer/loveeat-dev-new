package com.zz.minio.service;

import com.zz.framework.common.model.response.*;
import com.zz.framework.domain.picture.response.PictureCode;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @ClassName Minio
 * @Description Minio文件存储云服务相关工具类  api文档：https://docs.min.io/cn/java-client-api-reference.html
 * @Author sya
 * @Date 2019/8/1 20:05
 **/
@Component
@Slf4j
public class MinioPictureService {

	/**
	 * 服务器地址
	 */
	@Value("${minio.ip}")
	private String ip;

	/**
	 * 登录账号
	 */
	@Value("${minio.accessKey}")
	private String accessKey;

	/**
	 * 登录密码
	 */
	@Value("${minio.secretKey}")
	private String secretKey;

/*
	*/
/**
	 * 缩略图大小
	 *//*

	@Value("${minio.thumbor.width}")
	private String thumborWidth;

*/

	/**
	 * Minio文件上传
	 * @param file 文件实体
	 * @param fileName 修饰过的文件名 非源文件名
	 * @param bucketName 所存文件夹（桶名）
	 * @return
	 */
	public ResponseResultWithData minioUpload(MultipartFile file, String fileName, String bucketName) {
		try {
			MinioClient minioClient = new MinioClient("http://" + ip, accessKey, secretKey);
			/*boolean bucketExists = minioClient.bucketExists(bucketName);
			if (bucketExists) {
				log.info("仓库" + bucketName + "已经存在，可直接上传文件。");
			} else {
				minioClient.makeBucket(bucketName);
			}*/
			if (file.getSize() <= 20971520) {
				// fileName为空，说明要使用源文件名上传
				/*if (fileName == null) {
					fileName = file.getOriginalFilename();
					fileName = fileName.replaceAll(" ", "_");
				}*/

				// minio仓库名
				InputStream in = file.getInputStream();
				// MediaType.IMAGE_JPEG_VALUE
				PutObjectOptions putObjectOptions = new PutObjectOptions(in.available(), -1);
				putObjectOptions.setContentType(MediaType.IMAGE_JPEG_VALUE);
//				System.out.println("upload"+System.currentTimeMillis());
				minioClient.putObject(bucketName, fileName, file.getInputStream(),putObjectOptions);
				log.info("成功上传文件 " + fileName + " 至 " + bucketName + " "+System.currentTimeMillis());
//				http://149.248.8.106:9000/test/1593661280830_869813536068864155.jpg
				String fileUri = bucketName + "/" + fileName;
				String downFileUrl ="http://" + ip + "/" + fileUri;
				String prewFileUrl ="http://localhost:8080/preview"  + "/" + fileUri;
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("downFileUrl", downFileUrl);
				map.put("prewFileUrl", prewFileUrl);
				map.put("fileUri", fileUri);
				map.put("bucketName", bucketName);
				map.put("originFileName", fileName);
				return new ResponseResultWithData(CommonCode.SUCCESS,map);
			} else {
				throw new Exception("请上传小于20mb的文件");
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().contains("ORA")) {
				return new ResponseResultWithData(PictureCode.PICTURE_UPLOAD_WRONGPARAMS,null);//上传失败:【查询参数错误】
			}
			return new ResponseResultWithData(PictureCode.PICTURE_UPLOAD_ERROR,null);
		}
	}

	/**
	 * 判断文件是否存在
	 * @param fileName 文件名
	 * @param bucketName 桶名（文件夹）
	 * @return
	 */
	public boolean isFileExisted(String fileName, String bucketName) {
		InputStream inputStream = null;
		try {
			MinioClient minioClient = new MinioClient("http://" + ip, accessKey, secretKey);
			inputStream = minioClient.getObject(bucketName, fileName);
			if (inputStream != null) {
				return true;
			}
		} catch (Exception e) {
			return false;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 删除文件
	 * @param bucketName 桶名（文件夹）
	 * @param fileName 文件名
	 * @return
	 */
	public boolean delete(String bucketName,String fileName) {
		try {
			MinioClient minioClient = new MinioClient("http://" + ip, accessKey, secretKey);
			minioClient.removeObject(bucketName,fileName);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 下载文件
	 * @param objectName 文件名
	 * @param bucketName 桶名（文件夹）
	 * @param response
	 * @return
	 */
	public ResponseResultWithData downloadFile(String objectName,String bucketName, HttpServletResponse response) {
		try {
			MinioClient minioClient = new MinioClient("http://" + ip, accessKey, secretKey);
			InputStream file = minioClient.getObject(bucketName,objectName);
			String filename = new String(objectName.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			int len;
			byte[] buffer = new byte[1024];
			while((len=file.read(buffer)) > 0){
				servletOutputStream.write(buffer, 0, len);
			}
			servletOutputStream.flush();
			file.close();
			servletOutputStream.close();
			return new ResponseResultWithData(CommonCode.SUCCESS,null);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().contains("ORA")) {
				return new ResponseResultWithData(PictureCode.PICTURE_DOWNLOAD_WRONGPARAMS,null);//"下载失败:【查询参数错误】
			}
			return new ResponseResultWithData(PictureCode.PICTURE_DOWNLOAD_ERROR,null);
		}
	}


	/**
	 * 获取文件流
	 * @param objectName 文件名
	 * @param bucketName 桶名（文件夹）
	 * @return
	 */
	public InputStream getFileInputStream(String objectName,String bucketName) {
		try {
			MinioClient minioClient = new MinioClient("http://" + ip, accessKey, secretKey);
			return minioClient.getObject(bucketName,objectName);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}




}