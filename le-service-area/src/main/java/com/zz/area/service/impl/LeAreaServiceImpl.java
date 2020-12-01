package com.zz.area.service.impl;

import com.zz.area.dao.LeAreaMapper;
import com.zz.area.service.LeAreaService;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.area.LeArea;
import com.zz.framework.domain.area.ext.LeAreaNode;
import com.zz.framework.domain.area.response.AreaCode;
import com.zz.framework.domain.area.response.GetLeAreaResult;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class LeAreaServiceImpl  implements LeAreaService {
	@Autowired
	LeAreaMapper leAreaMapper;

	//插入区域
	@Override
	public ResponseResult insertArea(int parentId, String area) {
		LeArea leArea = new LeArea();
		leArea.setArea(area);
		leArea.setParentId(parentId);
		leArea.setSearchcount(0);
		leArea.setCreatetime(LocalDateTime.now());
		leArea.setUpdatetime(LocalDateTime.now());

		int insert = leAreaMapper.insertArea(leArea);
//		int insert = leAreaMapper.insertUseGeneratedKeys(leArea);
		if (insert > 0){
			return new ResponseResult(CommonCode.SUCCESS);
		}
		return new ResponseResult(AreaCode.AREA_CREATE_FALSE);
	}

	//根据城市id查询，如果是0则查询所有
	@Override
	public GetLeAreaResult getAreaById(int id) {
		List<LeAreaNode> areas = leAreaMapper.getCityAreaById(id);
		return new GetLeAreaResult(CommonCode.SUCCESS,areas);
	}

	//根据城市查询全部商圈及三个热点商圈
	@Override
	public ResponseResultWithData getAllAndHotAreasByCittId(int id) {
		List<LeAreaNode> allAreas = leAreaMapper.getAllAndHotAreasByCittId(id);
		List<LeAreaNode> hotAreas = new ArrayList<>();
		if (allAreas.size() > 3){
			LeAreaNode leAreaNode1 = allAreas.get(0);
			LeAreaNode leAreaNode2 = allAreas.get(1);
			LeAreaNode leAreaNode3 = allAreas.get(2);
			hotAreas.add(leAreaNode1);
			hotAreas.add(leAreaNode2);
			hotAreas.add(leAreaNode3);
		}else {
			hotAreas = allAreas;
		}
		HashMap<String, Object> data = new HashMap<>();
		data.put("allAreas",allAreas);
		data.put("hotAreas",hotAreas);
		ResponseResultWithData responseResultWithData = new ResponseResultWithData(CommonCode.SUCCESS, data);
		return responseResultWithData;
	}

	@Override
	public ResponseResultWithData getAllCitys() {
		List<LeArea> allCitys = leAreaMapper.getAllCitys();
		HashMap<String, Object> data = new HashMap<>();
		data.put("allCitys",allCitys);
		return new ResponseResultWithData(CommonCode.SUCCESS, data);
	}

	@Override
	public ResponseResult updateAreaSearchCountById(int id, int searchcount) {
		leAreaMapper.updateAreaSearchCountById(id, searchcount);
		return new ResponseResult(CommonCode.SUCCESS);

	}
}
