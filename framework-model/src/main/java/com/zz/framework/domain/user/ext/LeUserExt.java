package com.zz.framework.domain.user.ext;

import com.zz.framework.domain.user.LePermission;
import com.zz.framework.domain.user.LeRole;
import com.zz.framework.domain.user.LeUserBasic;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class LeUserExt extends LeUserBasic {
	//权限信息
	private List<LePermission> permissions;
	//角色信息
	private List<LeRole> roles;

}
