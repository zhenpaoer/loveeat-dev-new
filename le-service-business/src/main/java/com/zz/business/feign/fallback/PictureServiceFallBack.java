package com.zz.business.feign.fallback;

import com.zz.business.feign.PictureService;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.picture.response.PictureCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class PictureServiceFallBack implements PictureService {

	@Override
	public ResponseResultWithData uploadBusPic( MultipartFile file, String bid) {
		log.info( " this is le-service-business , but request error");
		return new ResponseResultWithData(PictureCode.PICTURE_UPLOAD_ERROR,null);
	}

	@Override
	public ResponseResultWithData uploadProPic( MultipartFile file,  String pid) {
		log.info( " this is le-service-business , but request error");
		return new ResponseResultWithData(PictureCode.PICTURE_UPLOAD_ERROR,null);
	}

	@Override
	public boolean removePic(String url) {
		log.info( " this is le-service-business , but request error");
		return false;
	}

	/*@Override
	public void previewPicture(HttpServletRequest request, HttpServletResponse response) {
		log.info( " this is le-service-business , but request error");
	}*/
}
