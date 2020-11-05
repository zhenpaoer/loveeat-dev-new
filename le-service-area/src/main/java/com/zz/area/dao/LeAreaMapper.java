package com.zz.area.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.area.LeArea;
import com.zz.framework.domain.area.ext.LeAreaNode;
import com.zz.framework.domain.business.ext.LeProductMenuNode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeAreaMapper extends Mymapper<LeArea> {
	//根据城市id查询树形的城市-行政区-商圈
	List<LeAreaNode> getAreaById(int id);



}