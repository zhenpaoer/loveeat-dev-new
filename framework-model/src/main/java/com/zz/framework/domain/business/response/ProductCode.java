package com.zz.framework.domain.business.response;/**
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
public enum ProductCode implements ResultCode {
	PRODUCT_CREATE_FALSE(false,1001,"添加商品失败！"),
	PRODUCT_CHECK_ID_FALSE(false,1002,"id校验失败，请重试！"),
	PRODUCT_CHECK_BID_FALSE(false,1003,"商家id校验失败，请重试！"),
	PRODUCT_CHECK_PID_FALSE(false,1003,"商家id校验失败，请重试！"),
	PRODUCT_NOTCOMPLETE(false,1004,"商品信息不完整，请重试！"),
	PRODUCT_NOTEXIT(false,1005,"商品信息不存在，请重试！"),
	PRODUCT_PARAM_NULL(false,1006,"商品主要信息不能为空，请重试！"),
	PRODUCT_DISCRI_NULL(false,1007,"商品描述信息不能为空，请重试！"),
	PRODUCT_BUSINESSNAME_NULL(false,1008,"商家分店名称不能为空，请重试！"),
	PRODUCT_NAME_NULL(false,1009,"商品名称不能为空，请重试！"),
	PRODUCT_FOODTYPE_NULL(false,1010,"商品食品类型信息不能为空，请重试！"),
	PRODUCT_PRICE_NULL(false,1011,"商品价格信息不能为空，请重试！"),
	PRODUCT_BID_NOTEXIT(false,1012,"商家信息不存在，请重试！"),
	PRODUCT_NAME_EXIT(false,1013,"商品名称已存在，请重试！"),
	PRODUCT_UPDATE_ERROR(false,1014,"商品更新失败，请重试！"),
	PRODUCT_SAVE_ERROR(false,1015,"商品保存失败，请重试！"),
	PRODUCT_DELETE_PICTURE_FALSE(false,1016,"删除商品图片失败！"),
	PRODUCT_CHECK_MENU_ERROR(false,1017,"商品菜单数量校验失败，请重试！"),
	PRODUCT_CHECK_LOCATION_ERROR(false,1018,"获取用户位置失败，请重试！"),
	PRODUCT_CHECK_AREA_ERROR(false,1018,"查询位置失败，请重试！"),
	PRODUCT_BARGAIN_AGAIN_ERROR(false,1018,"已砍价！"),
	PRODUCT_ISSALE_ERROR(false,1018,"商品状态校验失败"),
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
	private ProductCode(boolean success, int code, String message){
		this.success = success;
		this.code = code;
		this.message = message;
	}
	private static final ImmutableMap<Integer, ProductCode> CACHE;

	static {
		final ImmutableMap.Builder<Integer, ProductCode> builder = ImmutableMap.builder();
		for (ProductCode commonCode : values()) {
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
