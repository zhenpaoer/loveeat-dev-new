package com.zz.framework.domain.order.response;/**
 * Created by zhangzhen on 2020/5/17
 */

import com.google.common.collect.ImmutableMap;
import com.zz.framework.common.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

/**
 * @ClassName ProductCode
 * @Description: TODO 商品参数
 * @Author zhangzhen
 * @Date 2020/5/17 
 * @Version V1.0
 **/
@ToString
public enum OrderCode implements ResultCode {
	ORDER_NULL(false,1001,"订单不存在"),
	ORDER_PROCESSING(false,1002,"其他人正在购买中，再看看别的吧"),
	ORDER_SALED(false,1003,"商品已售，明日再来"),
	PRODUCT_CHECK_PID_FALSE(false,1003,"商家id校验失败，请重试！"),
	PRODUCT_OTHER_ERROR(false,1099,"其他异常，请重试！");

	//操作代码
	@ApiModelProperty(value = "操作是否成功", example = "true", required = true)
	boolean success;

	//操作代码
	@ApiModelProperty(value = "操作代码", example = "22001", required = true)
	int code;
	//提示信息
	@ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
	String message;
	private OrderCode(boolean success, int code, String message){
		this.success = success;
		this.code = code;
		this.message = message;
	}
	private static final ImmutableMap<Integer, OrderCode> CACHE;

	static {
		final ImmutableMap.Builder<Integer, OrderCode> builder = ImmutableMap.builder();
		for (OrderCode commonCode : values()) {
			builder.put(commonCode.code(),commonCode);
		}
		CACHE = builder.build();
	}

	@Override
	public boolean success() {
		return success;
	}

	@Override
	public int code() {
		return code;
	}

	@Override
	public String message() {
		return message;
	}
}
