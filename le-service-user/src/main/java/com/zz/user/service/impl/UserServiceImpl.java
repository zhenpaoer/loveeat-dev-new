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
		if (leUserBasic == null) return new GetUserExtResult(UserCode.USER_ACCOUNT_NOTEXISTS,null);

		LeUserExt leUserExt = new LeUserExt();
		BeanUtils.copyProperties(leUserBasic, leUserExt);

		//用户ID
		Integer id = leUserBasic.getId();
		Example example1 = new Example(LeUserRole.class);
		Example.Criteria criteria1 = example1.createCriteria();
		criteria1.andEqualTo("userId", id);
		List<LeUserRole> leUserRoles = leUserRoleMapper.selectByExample(example1);
		if (leUserRoles.size() == 1) {
			LeRole leRole = leRoleMapper.selectByPrimaryKey(leUserRoles.get(0).getRoleId());
			ArrayList<LeRole> leRoles = new ArrayList<>();
			leRoles.add(leRole);
			leUserExt.setRoles(leRoles);
		} else {
			List<Integer> roleIds = leUserRoles.stream().map(LeUserRole::getRoleId).collect(Collectors.toList());
			List<LeRole> roleByIds = leRoleMapper.getRoleByIds(roleIds);
			leUserExt.setRoles(roleByIds);
		}
		ArrayList<LePermission> arrayList = new ArrayList<>();
		leUserExt.getRoles().stream().forEach(leRole -> {
			Example example2 = new Example(LeRolePermission.class);
			Example.Criteria criteria2 = example2.createCriteria();
			criteria2.andEqualTo("roleId", leRole.getId());
			List<LeRolePermission> leRolePermissions = leRolePermissionMapper.selectByExample(example2);
			List<Integer> permissinIds = leRolePermissions.stream().map(LeRolePermission::getPermissionId).distinct().collect(Collectors.toList());
			List<LePermission> permissions = lePermissionMapper.getPermissionByIds(permissinIds);
			arrayList.addAll(permissions);
		});
		leUserExt.setPermissions(arrayList);
		return new GetUserExtResult(CommonCode.SUCCESS,leUserExt);
	}

	@Override
	public List<Integer> getLeUserRole(int id) {

		List<Integer> roleIds= leUserRoleMapper.getRoleIdByUserId(id);
		System.out.println("roleIds=="+roleIds);
		return roleIds;
	}
}
