package com.zz.business.feign.fallback;/**
 * Created by zhangzhen on 2020/12/22
 */

import com.zz.business.feign.OrderService;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.picture.response.PictureCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName OrderServiceFallBack
 * @Description: TODO 订单服务fallback
 * @Author zhangzhen
 * @Date 2020/12/22 
 * @Version V1.0
 **/
@Slf4j
public class OrderServiceFallBack implements OrderService {

	@Override
	public boolean isOrdered(int pid, int uid) {
		log.info( " this is le-service-order , but request error");
		return false;
	}
}
