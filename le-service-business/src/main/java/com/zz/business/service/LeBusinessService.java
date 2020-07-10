package com.zz.business.service;

import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeBusiness;
import org.springframework.stereotype.Service;

/**
 * Created by zhangzhen on 2020/5/16
 */
@Service
public interface LeBusinessService  {

	//根据id获取商家信息
	public LeBusiness getLeBusiness(int id);

	//获取所有商家信息
	public QueryResponseResult<LeBusiness> getAll();

	//创建商家信息
	public ResponseResult createLeBusiness(LeBusiness leBusiness);
}
