package com.zz.business.feign.fallback;/**
 * Created by zhangzhen on 2020/3/19
 */

import com.zz.business.feign.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName ProviderServiceFallback
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/19 21:56
 * @Version V1.0
 **/
@Component
@Slf4j
public class ProviderServiceFallback implements ProviderService {


	@Override
	public String hi(String msg) {
		log.info( " this is le-service-business , but request error");
		return "get your msg =" + msg + ", this is le-service-business , but request error";
	}
}
