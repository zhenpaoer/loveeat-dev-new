package com.zz.business.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.business.ext.LeProductMenuNode;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LeProductMenudetailMapper extends Mymapper<LeProductMenudetail> {

	//通过pid获取菜单
	/*@Select({
			" <script> ",
			" SELECT a.id aid, a.title title, b.id bid, b.item item, b.price price FROM le_product_menudetail a LEFT JOIN le_product_menudetail b ON a.id = b.parentid",
			" WHERE a.parentid = '0' AND a.pid = #{pid}",
			" ORDER BY a.id,b.id ",
			" </script> "
	})*/
	List<LeProductMenuNode> getProductMenuByPid(int pid);
}