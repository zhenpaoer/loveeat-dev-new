package com.zz.business.service.impl;/**
 * Created by zhangzhen on 2020/5/16
 */

import com.zz.business.dao.LeBusinessMapper;
import com.zz.business.service.LeBusinessService;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.QueryResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeBusiness;
import com.zz.framework.domain.business.response.BusinessCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName LeBusinessServiceImpl
 * @Description: TODO 商家服务实现
 * @Author zhangzhen
 * @Date 2020/5/16 
 * @Version V1.0
 **/

@Service
public class LeBusinessServiceImpl implements LeBusinessService {

	@Autowired
	LeBusinessMapper leBusinessMapper;

	//根据id获取商家信息
	@Override
	public LeBusiness getLeBusiness(int id) {
		return leBusinessMapper.selectByPrimaryKey(id);
	}

	//获取所有商家信息
	@Override
	public QueryResponseResult<LeBusiness> getAll() {
		List<LeBusiness> leBusinesses = leBusinessMapper.selectAll();
		QueryResult<LeBusiness> QueryResult = new QueryResult<LeBusiness>();
		QueryResult.setList(leBusinesses);
		QueryResult.setTotal(leBusinesses.size());
		QueryResponseResult<LeBusiness> responseResult = new QueryResponseResult<>(CommonCode.SUCCESS,QueryResult);

		return responseResult;
	}

	//创建商家信息
	@Override
	public ResponseResult createLeBusiness(LeBusiness leBusiness) {
		int result = leBusinessMapper.insert(leBusiness);
		if (result == 1){
			return new ResponseResult(CommonCode.SUCCESS);
		}
		return new ResponseResult(BusinessCode.BUSINESS_CREATE_FALSE);
	}
}
