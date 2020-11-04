package com.zz.business.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.business.LeProduct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface LeProductMapper extends Mymapper<LeProduct> {

	//用户展示的商品列表
	List<LeProduct> getAllProductForUser();
	//首页展示的商品列表 包括计算坐标
	List<LeProduct> getHomeProduct(HashMap<String,String> map);
}