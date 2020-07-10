package com.zz.business.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.business.ext.LeProductMenuNode;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LeProductMenudetailMapper extends Mymapper<LeProductMenudetail> {

	//通过pid获取菜单
	List<LeProductMenuNode> getProductMenuByPid(int pid);
}