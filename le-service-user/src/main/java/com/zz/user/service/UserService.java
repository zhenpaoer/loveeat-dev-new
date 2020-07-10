package com.zz.user.service;

import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.GetUserExtResult;

public interface UserService {
	public GetUserExtResult getUserExt(String userName);
}
