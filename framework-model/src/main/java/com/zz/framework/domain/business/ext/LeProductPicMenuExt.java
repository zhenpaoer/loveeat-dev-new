package com.zz.framework.domain.business.ext;/**
 * Created by zhangzhen on 2020/5/16
 */

import com.zz.framework.domain.business.LeProduct;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.business.LeProductPicurl;
import lombok.Data;

import java.util.List;

/**
 * @ClassName LeProductPicMenuExt
 * @Description: TODO 商品基础信息扩展 图片和菜单信息
 * @Author zhangzhen
 * @Date 2020/5/16 
 * @Version V1.0
 **/
@Data
public class LeProductPicMenuExt  {
	LeProduct leProduct ;						//商品主要信息
	List<LeProductPicurl> productPicurls;		//商品图片url
	List<LeProductMenuNode> productMenuNodes; 	//商品菜单明细 每个菜单里面包括多个子项，是树形的，可以有多个菜单 构成一个大菜单
}
