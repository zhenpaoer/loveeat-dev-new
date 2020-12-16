package com.zz.business.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.business.LeProductPicurl;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeProductPicurlMapper extends Mymapper<LeProductPicurl> {

	//根据商品id查找图片
	@Select("select id ,pid,picurl from le_product_picurl where pid = #{pid}")
	List<LeProductPicurl> getProductUrlByPid(int pid);
}