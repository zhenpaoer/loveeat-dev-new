package com.zz.framework.common.model.response;

import lombok.Data;
import lombok.ToString;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;

@Data
@ToString
public class ResponseResultWithData extends ResponseResult{
	HashMap<String,Object> data;

	public ResponseResultWithData(){}
	
	public ResponseResultWithData(ResultCode resultCode,HashMap<String,Object> data){
		super(resultCode);
		this.data = data;
	}
}
