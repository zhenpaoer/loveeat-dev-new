package com.zz.user.service.impl;

import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.user.*;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.GetUserExtResult;
import com.zz.framework.domain.user.response.UserCode;
import com.zz.user.dao.*;
import com.zz.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	LeUserBasicMapper leUserBasicMapper;
	@Autowired
	LeUserRoleMapper leUserRoleMapper;
	@Autowired
	LeRoleMapper leRoleMapper;
	@Autowired
	LeRolePermissionMapper leRolePermissionMapper;
	@Autowired
	LePermissionMapper lePermissionMapper;

	@Override
	public GetUserExtResult getUserExt(String userName) {
		Example example = new Example(LeUserBasic.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("username", userName);
		LeUserBasic leUserBasic = leUserBasicMapper.selectOneByExample(example);
		if (leUserBasic == null) {
			return new GetUserExtResult(UserCode.USER_ACCOUNT_NOTEXISTS,null);
		}

		LeUserExt leUserExt = new LeUserExt();
		BeanUtils.copyProperties(leUserBasic, leUserExt);

		//用户ID
		Integer id = leUserBasic.getId();
		List<Integer> roleIds = leUserRoleMapper.getRoleIdByUserId(id);
		if (roleIds.size() == 0){
			//如果用户没角色，则赋予用户权限
			LeUserRole leUserRole = new LeUserRole();
			leUserRole.setUserId(id);
			leUserRole.setRoleId(2);
			leUserRoleMapper.insert(leUserRole);
			this.getUserExt(userName);
		}
		List<Integer> permissionIdsByRoleids = leRolePermissionMapper.getPermissionIdsByRoleids(roleIds);
		//去重的权限id
		List<Integer> permissionIds = permissionIdsByRoleids.stream().distinct().collect(Collectors.toList());
		//拿权限
		List<LePermission> permissions = lePermissionMapper.getPermissionByIds(permissionIds);
		if (permissions.size() != 0){
			leUserExt.setPermissions(permissions);
		}
		//拿角色
		List<LeRole> roles = leRoleMapper.getRoleByIds(roleIds);
		if (roles.size() !=0 ){
			leUserExt.setRoles(roles);
		}
		return new GetUserExtResult(CommonCode.SUCCESS,leUserExt);
	}

	@Override
	public List<Integer> getLeUserRole(int id) {

		List<Integer> roleIds= leUserRoleMapper.getRoleIdByUserId(id);
		System.out.println("roleIds=="+roleIds);
		return roleIds;
	}
}
