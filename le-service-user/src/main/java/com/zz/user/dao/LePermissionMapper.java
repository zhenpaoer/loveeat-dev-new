package com.zz.user.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.user.LePermission;
import com.zz.framework.domain.user.LeRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LePermissionMapper extends Mymapper<LePermission> {
	@Select({
			" <script> ",
			" select ",
			" *  ",
			" from le_permission ",
			"  where  id in ",
			" <foreach collection='ids' item='id' open='(' separator=',' close=')'> ",
			" #{id} ",
			" </foreach> ",
			" </script> "
	})
	public List<LePermission> getPermissionByIds(@Param("ids")List<Integer> ids);
}