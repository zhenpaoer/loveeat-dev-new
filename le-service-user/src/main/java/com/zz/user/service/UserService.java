package com.zz.user.service;

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.user.LeUserBasic;
import com.zz.framework.domain.user.LeUserRole;
import com.zz.framework.domain.user.ext.AuthToken;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.GetUserExtResult;

import java.util.List;

public interface UserService {
	public GetUserExtResult getUserExt(String userName);
	public GetUserExtResult getUserextByOpenid(String openid);
	public LeUserBasic getUserBasicByOpenId(String openid);

	public List<Integer> getLeUserRole(int id);

	public Integer createUser(LeUserBasic user);

	public Integer createUserRole(LeUserRole leUserRole);

	public ResponseResult updateUserLogin(String openid,String nickName,String avatarUrl,String address,String lon,String lat);
}
