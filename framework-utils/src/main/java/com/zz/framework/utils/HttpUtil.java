package com.zz.framework.utils;

import org.apache.http.HttpHeaders;

public class HttpUtil {
	/**
	 * 向目的URL发送post请求
	 * @param url       目的url
	 * @param params    发送的参数
	 * @return  ResultVO
	 */
/*	public static ResultVO sendPostRequest(String url, MultiValueMap<String, String> params){
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpMethod method = HttpMethod.POST;
		// 以表单的方式提交
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		//将请求头部和参数合成一个请求
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
		//执行HTTP请求，将返回的结构使用ResultVO类格式化
		ResponseEntity<ResultVO> response = client.exchange(url, method, requestEntity, ResultVO.class);

		return response.getBody();
	}*/
}
