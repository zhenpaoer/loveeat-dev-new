package com.zz.user.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.user.LePermission;
import com.zz.framework.domain.user.LeRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LePermissionMapper extends Mymapper<LePermission> {
	public List<LePermission> getPermissionByIds(List<Integer> ids);
}