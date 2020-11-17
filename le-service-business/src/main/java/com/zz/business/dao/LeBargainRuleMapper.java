package com.zz.business.dao;

import com.zz.framework.domain.business.LeBargainRule;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

@Repository
public interface LeBargainRuleMapper {
	@Select("select * from le_bargain_rule where discount = #{discount}")
	LeBargainRule getByDiscount(int discount);
}
