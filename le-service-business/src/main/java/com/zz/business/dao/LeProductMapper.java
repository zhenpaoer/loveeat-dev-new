package com.zz.business.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.business.LeProduct;
import org.apache.ibatis.annotations.Select;
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
	@Select({
			" <script> ",
			"select id,businessname,productname,bid,createtime,updatetime,state,status,foodtype,describ,originalprice,bargainprice,bargainpersonsum,issale,version,homepicture,rule,usetime,lon,lat,(st_distance (point (lon, lat),point(#{lon,jdbcType=VARCHAR},#{lat,jdbcType=VARCHAR}) ) / 0.0111) as distance",
			"FROM le_product",
			" <where>",
			" state = 1 and `status` = '审核通过' and bid in ",
			" <foreach collection='ids' item='id' open='(' separator=',' close=')'> ",
			" #{id} ",
			" </foreach> ",
			"<if test= 'productType !=null  '>",
			"	and foodtype = #{productType}",
			"</if>",
			"<if test='priceStart != null and priceEnd != null  '>",
			" 	and bargainprice between #{priceStart} and #{priceEnd}",
			"</if>",
			" </where>",
			"<if test='distance != null  '>",
			"	HAVING distance &lt;= #{distance,jdbcType=VARCHAR}",
			"</if>",
			"ORDER BY ${sortType}",
			" </script> "
	})
	List<LeProduct> getHomeProduct(HashMap<String,Object> map);

	@Update("update le_product set bargainprice = #{bargainPrice},bargainpersonsum = bargainpersonsum + 1 where id = #{id}")
	int updateBargainPrice(int id, BigDecimal bargainPrice);

	@Update("update le_product set issale = #{issale} where id = #{pid}")
	int updateIssale(int pid,int issale);

	@Update("update le_product set issale = 1 where id = #{pid}")
	int updateIssaleToOne(int pid);
}