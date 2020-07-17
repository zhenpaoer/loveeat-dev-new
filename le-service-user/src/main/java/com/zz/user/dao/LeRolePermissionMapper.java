package com.zz.user.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.user.LeRolePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeRolePermissionMapper extends Mymapper<LeRolePermission> {


//	SELECT permission_id FROM `le_role_permission` where role_id in (1,2);
	@Select({
			" <script> ",
			" select ",
			" permission_id",
			" from le_role_permission ",
			"  where  role_id in ",
			" <foreach collection='ids' item='id' open='(' separator=',' close=')'> ",
			" #{id} ",
			" </foreach> ",
			" </script> "
	})
	public List<Integer> getPermissionIdsByRoleids(@Param("ids") List<Integer> ids);
}