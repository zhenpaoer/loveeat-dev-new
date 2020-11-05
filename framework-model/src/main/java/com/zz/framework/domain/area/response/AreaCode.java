package com.zz.framework.domain.area.response;

import com.google.common.collect.ImmutableMap;
import com.zz.framework.common.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * Created by admin on 2020/5/15.
 * 1000开头
 */
@ToString
public enum AreaCode implements ResultCode {
    AREA_CREATE_FALSE(false,1001,"添加区域失败！"),
    AREA_CHECK_ID_FALSE(false,1002,"id校验失败，请重试！"),
    AREA_CHECK_NAME_FALSE(false,1002,"区域名称校验失败，请重试！"),
    AREA_NOTEXIT(false,1003,"区域不存在，请重试！"),
    AREA_UPDATE_FALSE(false,1006,"修改区域失败！");

    //操作代码
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
    String message;
    private AreaCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, AreaCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, AreaCode> builder = ImmutableMap.builder();
        for (AreaCode commonCode : values()) {
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
