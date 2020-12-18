package com.zz.web.controller;

import com.zz.area.service.LeAreaService;
import com.zz.framework.api.area.AreaControllerApi;
import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.area.response.AreaCode;
import com.zz.framework.domain.area.response.GetLeAreaResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName LeAreaController
 * @Description: TODO 区域服务
 * @Author zhangzhen
 * @Date 2020/11/5
 * @Version V1.0
 **/

@RestController
@RequestMapping("area")
public class LeAreaController  implements AreaControllerApi {
	@Autowired
	LeAreaService leAreaService;

	//插入区域
	@Override
	@RequestMapping("/insert")
	public ResponseResult insertArea(int parentId, String area) {
		if (parentId<0){
			ExceptionCast.cast(AreaCode.AREA_CHECK_ID_FALSE);
		}
		if (StringUtils.isEmpty(area)){
			ExceptionCast.cast(AreaCode.AREA_CHECK_NAME_FALSE);
		}
		return leAreaService.insertArea(parentId,area);
	}

	//查询区域
	@Override
	@GetMapping("/getAreaById")
	public GetLeAreaResult getLeAreasById(int id) {
		if (id<0){
			ExceptionCast.cast(AreaCode.AREA_CHECK_ID_FALSE);
		}
		return leAreaService.getAreaById(id);
	}

	//根据城市查询全部商圈及三个热点商圈
	@Override
	@GetMapping("/getAllAndHotAreasByCityId")
	public ResponseResultWithData getAllAndHotAreasByCittId(int id) {
		if (id<0){
			ExceptionCast.cast(AreaCode.AREA_CHECK_ID_FALSE);
		}
		return leAreaService.getAllAndHotAreasByCittId(id);
	}

	//查询所有城市
	@Override
	@GetMapping("/getAllCitys")
	public ResponseResultWithData getAllCitys() {
		return leAreaService.getAllCitys();
	}

	@Override
	@RequestMapping("/updateAreaSearchCountById")
	public ResponseResult updateAreaSearchCountById(int id, int searchcount) {
		if (id<0 || searchcount<0){
			ExceptionCast.cast(AreaCode.AREA_CHECK_ID_FALSE);
		}
		return leAreaService.updateAreaSearchCountById(id,searchcount);
	}
}
