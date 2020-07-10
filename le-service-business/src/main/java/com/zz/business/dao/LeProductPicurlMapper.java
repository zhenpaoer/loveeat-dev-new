package com.zz.business.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.business.LeProductPicurl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeProductPicurlMapper extends Mymapper<LeProductPicurl> {

	//根据商品id查找图片
	List<LeProductPicurl> getProductUrlByPid(int pid);
}