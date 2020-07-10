package com.zz.framework.domain.business.request;/**
 * Created by zhangzhen on 2020/5/16
 */

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName LeBusinessListRequest
 * @Description: TODO 请求的商家信息
 * @Author zhangzhen
 * @Date 2020/5/16 
 * @Version V1.0
 **/
@Data
@ToString
public class LeBusinessListRequest {
	//商家Id
	private String bid;
}
