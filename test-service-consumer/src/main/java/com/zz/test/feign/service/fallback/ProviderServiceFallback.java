package com.zz.test.feign.service.fallback;/**
 * Created by zhangzhen on 2020/3/19
 */

import com.zz.test.feign.service.ProviderService;
import org.springframework.stereotype.Component;

/**
 * @ClassName ProviderServiceFallback
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/3/19 21:56
 * @Version V1.0
 **/
@Component
public class ProviderServiceFallback implements ProviderService {


	@Override
	public String hi(String msg) {
		return "get your msg =" + msg + ", this is test-service-consumer , but request error";
	}
}
