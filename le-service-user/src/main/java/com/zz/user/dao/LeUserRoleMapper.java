package com.zz.user.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.user.LeUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeUserRoleMapper extends Mymapper<LeUserRole> {

	//根据userid获取roleid
	@Select("select role_id from le_user_role where user_id = #{userId}")
	public List<Integer> getRoleIdByUserId(int userId);
}