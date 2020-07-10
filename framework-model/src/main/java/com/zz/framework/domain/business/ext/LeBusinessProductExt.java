package com.zz.framework.domain.business.ext;/**
 * Created by zhangzhen on 2020/5/16
 */

import com.zz.framework.domain.business.LeBusinessDetail;
import com.zz.framework.domain.business.LeProduct;

import java.util.List;

/**
 * @ClassName LeBusinessProductExt
 * @Description: TODO 商家信息扩展商品信息
 * @Author zhangzhen
 * @Date 2020/5/16 
 * @Version V1.0
 **/
public class LeBusinessProductExt extends LeBusinessDetail {
	List<LeProductPicMenuExt> products;
}
