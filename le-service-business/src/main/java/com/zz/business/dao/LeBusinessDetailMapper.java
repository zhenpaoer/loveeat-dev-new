package com.zz.business.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.business.LeBusinessDetail;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeBusinessDetailMapper extends Mymapper<LeBusinessDetail> {

	//查商圈
	@Select("SELECT * FROM `le_business_detail` WHERE areaId = #{areaId} and state = 2 and `status` = '审核通过'")
	List<LeBusinessDetail> getByAreaId(int areaId);
	//查行政区
	@Select("SELECT * FROM `le_business_detail` WHERE regionId = #{regionId} and state = 2 and `status` = '审核通过'")
	List<LeBusinessDetail> getByRegionId(int regionId);
	//查城市
	@Select("SELECT * FROM `le_business_detail` WHERE cityId = #{cityId} and state = 2 and `status` = '审核通过'")
	List<LeBusinessDetail> getByCityId(int cityId);
}