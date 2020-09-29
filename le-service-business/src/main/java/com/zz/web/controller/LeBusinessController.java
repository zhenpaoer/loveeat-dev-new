package com.zz.web.controller;/**
 * Created by zhangzhen on 2020/5/16
 */

import com.zz.business.service.LeBusinessService;
import com.zz.framework.api.business.BusinessControllerApi;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeBusiness;
import com.zz.framework.domain.business.response.BusinessCode;
import com.zz.framework.domain.business.response.GetBusinessInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName LeBusinessController
 * @Description: TODO 商家
 * @Author zhangzhen
 * @Date 2020/5/16 
 * @Version V1.0
 **/

@RestController
@RequestMapping("business")
public class LeBusinessController implements BusinessControllerApi {

	@Autowired
	LeBusinessService leBusinessService;

	//获取某一个商家信息----
	@Override
	@GetMapping("/getBusById/{id}")
	public GetBusinessInfoResult getBusById(@PathVariable int id) {
		if (id < 0 ){
			return new GetBusinessInfoResult(BusinessCode.BUSINESS_CHECK_ID_FALSE,null);
		}
		LeBusiness leBusiness = leBusinessService.getLeBusiness(id);
		if (leBusiness != null){
			return new GetBusinessInfoResult(CommonCode.SUCCESS,leBusiness);
		}
		return new GetBusinessInfoResult(BusinessCode.BUSINESS_NOTEXIT,null);
	}

	//获取所有商家信息
	@Override
	@GetMapping("/getBusList")
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')" )
	public QueryResponseResult<LeBusiness> getBusList() {
		return leBusinessService.getAll();
	}

	//添加商家信息
	@Override
	@PostMapping("/createBusiness")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")//添加登录权限判断，登录才可以调用
	public ResponseResult createBusiness(@RequestBody LeBusiness leBusiness) {
		return leBusinessService.createLeBusiness(leBusiness);
	}



}
