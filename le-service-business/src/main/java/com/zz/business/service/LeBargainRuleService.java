package com.zz.business.service;
import com.zz.framework.domain.business.LeBargainRule;

import java.util.List;

public interface LeBargainRuleService {
	//根据discount查询对象
	LeBargainRule getByDiscount(int discount);

	//查询所有
	List<LeBargainRule> getAll();
}
