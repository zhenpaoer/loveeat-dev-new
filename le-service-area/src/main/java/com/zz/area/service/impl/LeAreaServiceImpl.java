package com.zz.area.service.impl;

import com.zz.area.dao.LeAreaMapper;
import com.zz.area.service.LeAreaService;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.area.LeArea;
import com.zz.framework.domain.area.ext.LeAreaNode;
import com.zz.framework.domain.area.response.AreaCode;
import com.zz.framework.domain.area.response.GetLeAreaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
		leArea.setCreatetime(LocalDateTime.now());
		leArea.setUpdatetime(LocalDateTime.now());
		int insert = leAreaMapper.insert(leArea);
		if (insert > 0){
			return new ResponseResult(CommonCode.SUCCESS);
		}
		return new ResponseResult(AreaCode.AREA_CREATE_FALSE);
	}

	//根据城市id查询，如果是0则查询所有
	@Override
	public GetLeAreaResult getAreaById(int id) {
		List<LeAreaNode> areas = leAreaMapper.getAreaById(id);
		return new GetLeAreaResult(CommonCode.SUCCESS,areas);
	}
}
