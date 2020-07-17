package com.zz.test.feign.service.fallback;

import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.domain.user.response.GetUserExtResult;
import com.zz.framework.domain.user.response.UserCode;
import com.zz.test.feign.service.UserClient;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {
	@Override
	public GetUserExtResult getUserext(String username) {
		String S = "get your username =" + username + ", this is test-service-consumer , but request error";
		System.out.println(S);
		return new GetUserExtResult(CommonCode.FAIL,null) ;
	}
}
