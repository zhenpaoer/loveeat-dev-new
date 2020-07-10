package com.zz.business.service;

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.business.ext.LeProductMenuNode;

import java.util.List;

/**
 * Created by zhangzhen on 2020/5/18
 */
public interface LeProductMenudetailService {
	//通过pid获取菜单
	List<LeProductMenuNode> getProductMenuByPid(int pid);

	//保存菜单
	public ResponseResult saveProductMenu(List<LeProductMenudetail> leProductMenudetails);
}
