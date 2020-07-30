package com.zz.framework.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by zhangzhen on 2019/7/14
 */
public class FeignClientInterceptor implements RequestInterceptor {
	@Override
	public void apply(RequestTemplate template) {

		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null){
			HttpServletRequest request = requestAttributes.getRequest();
			Enumeration<String> headerNames = request.getHeaderNames();
			if (headerNames != null){
				while (headerNames.hasMoreElements()){
					String headerName = headerNames.nextElement();
					String values = request.getHeader(headerName);
					if (headerName.equals("authrization")){
						template.header(headerName,values);
					}
					if (headerName.equals("Authrization")){
						template.header(headerName,values);
					}
				}
			}
		}

	}
}
