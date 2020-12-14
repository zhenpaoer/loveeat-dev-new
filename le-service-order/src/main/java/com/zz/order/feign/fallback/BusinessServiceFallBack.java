package com.zz.order.feign.fallback;

import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.domain.business.response.GetLeProductPicMenuExtResult;
import com.zz.order.feign.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BusinessServiceFallBack implements BusinessService {


	@Override
	public GetLeProductPicMenuExtResult getProductById(int id) {
		log.info( " this is le-service-order , but request Product error");
		return new GetLeProductPicMenuExtResult(CommonCode.FAIL,null);
	}
}
