package com.zz.framework.domain.business.response;

import com.google.common.collect.ImmutableMap;
import com.zz.framework.common.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * Created by admin on 2020/5/15.
 * 1000开头
 */
@ToString
public enum BusinessCode implements ResultCode {
    BUSINESS_CREATE_FALSE(false,1001,"添加商家失败！"),
    BUSINESS_CHECK_ID_FALSE(false,1002,"id校验失败，请重试！"),
    BUSINESS_NOTEXIT(false,1003,"商家信息不存在，请重试！"),
    BUSINESSDETAIL_CREATE_FALSE(false,1004,"添加商家分店信息失败！"),
    BUSINESSDETAIL_DELETE_PICTURE_FALSE(false,1005,"删除商家图片失败！"),
    BUSINESSDETAIL_UPDATE_FALSE(false,1006,"修改商家分店信息失败！");

    //操作代码
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
    String message;
    private BusinessCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, BusinessCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, BusinessCode> builder = ImmutableMap.builder();
        for (BusinessCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
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
