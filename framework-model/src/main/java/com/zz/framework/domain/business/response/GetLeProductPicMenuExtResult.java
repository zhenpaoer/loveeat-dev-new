package com.zz.framework.domain.business.response;/**
 * Created by zhangzhen on 2020/5/17
 */

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResultCode;
import com.zz.framework.domain.business.ext.LeProductPicMenuExt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName GetLeProductPicMenuExtResult
 * @Description: TODO  返回商品信息 包括菜单 图片
 * @Author zhangzhen
 * @Date 2020/5/17 
 * @Version V1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetLeProductPicMenuExtResult extends ResponseResult {
	private LeProductPicMenuExt leProductPicMenuExt;
	public GetLeProductPicMenuExtResult(ResultCode resultCode, LeProductPicMenuExt leProductPicMenuExt) {
		super(resultCode);
		this.leProductPicMenuExt = leProductPicMenuExt;
	}

}
