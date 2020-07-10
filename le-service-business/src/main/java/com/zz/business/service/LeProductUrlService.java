package com.zz.business.service;

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeProductPicurl;

import java.util.List;

/**
 * Created by zhangzhen on 2020/5/18
 */
public interface LeProductUrlService {
	//根据商品id查找图片
	List<LeProductPicurl> getProductUrlByPid(int pid);

	//保存商品图片
	ResponseResult saveLeProductPicurls(List<LeProductPicurl> leProductPicurls);

	//保存某一个商品图片
	ResponseResult saveLeProductPicurl(LeProductPicurl leProductPicurl);
}
