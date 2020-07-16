package com.zz.user.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.user.LeRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface LeRoleMapper extends Mymapper<LeRole> {

	@Select({
			" <script> ",
			" select ",
			" *  ",
			" from le_role ",
			"  where  id in ",
			" <foreach collection='ids' item='id' open='(' separator=',' close=')'> ",
			" #{id} ",
			" </foreach> ",
			" </script> "
	})
	public List<LeRole> getRoleByIds(@Param("ids") List<Integer> ids);
}