package com.zz.user.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.user.LeRole;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface LeRoleMapper extends Mymapper<LeRole> {
	public List<LeRole> getRoleByIds(List<Integer> ids);
	public LeRole getRoleById(int id);

}