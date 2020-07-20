package com.zz.user.service;

import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.user.LeUserRole;
import com.zz.framework.domain.user.ext.AuthToken;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.GetUserExtResult;

import java.util.List;

public interface UserService {
	public GetUserExtResult getUserExt(String userName);

	public List<Integer> getLeUserRole(int id);

}
