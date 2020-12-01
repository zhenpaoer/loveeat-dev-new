package com.zz.business.dao;

import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.business.LeBargainLog;
import com.zz.framework.domain.business.LeBargainRule;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeBargainRuleMapper  extends Mymapper<LeBargainRule> {
	@Select("select * from le_bargain_rule where discount = #{discount}")
	LeBargainRule getByDiscount(int discount);

	@Select("select * from le_bargain_rule ")
	List<LeBargainRule> getAll1();
}
