package com.zz.business.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.business.LeProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeProductMapper extends Mymapper<LeProduct> {

	//用户展示的商品列表
	List<LeProduct> getAllProductForUser();
}