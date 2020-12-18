package com.zz.order.feign.fallback;

import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.response.GetLeProductPicMenuExtResult;
import com.zz.order.feign.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class BusinessServiceFallBack implements BusinessService {


	@Override
	public GetLeProductPicMenuExtResult getProductById(int id) {
		log.info( " this is le-service-order , but request Product error");
		return new GetLeProductPicMenuExtResult(CommonCode.FAIL,null);
	}

	@Override
	public ResponseResult updateProductIsSaleByPid(int pid, int issale) { //, HttpServletRequest request
		log.info( " this is le-service-order , but request Product error");
		return new GetLeProductPicMenuExtResult(CommonCode.FAIL,null);
	}

	@Override
	public ResponseResult updateProductIsSaleToOne(int pid) {
		log.info( " this is le-service-order , but request Product error");
		return new ResponseResult(CommonCode.FAIL);
	}
}
