package com.zz.business.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.business.LeProduct;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Repository
public interface LeProductMapper extends Mymapper<LeProduct> {

	//用户展示的商品列表
	List<LeProduct> getAllProductForUser();
	//首页展示的商品列表 包括计算坐标
	List<LeProduct> getHomeProduct(HashMap<String,Object> map);

	@Update("update le_product set bargainprice = #{bargainPrice},bargainpersonsum = bargainpersonsum + 1 where id = #{id}")
	int updateBargainPrice(int id, BigDecimal bargainPrice);

	@Update("update le_product set issale = #{issale} where id = #{pid}")
	int updateIssale(int pid,int issale);
}