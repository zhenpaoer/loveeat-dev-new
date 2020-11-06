package com.zz.area.service;

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.area.response.GetLeAreaResult;


public interface LeAreaService {
	//根据父节点id插入
	ResponseResult insertArea(int parentId,String area);

	//根据城市id查询树形的城市-行政区-商圈
	GetLeAreaResult getAreaById(int id);

	//根据城市查询全部商圈及三个热点商圈
	public ResponseResultWithData getAllAndHotAreasByCittId(int id);

	//查询所有城市
	public ResponseResultWithData getAllCitys();
}
