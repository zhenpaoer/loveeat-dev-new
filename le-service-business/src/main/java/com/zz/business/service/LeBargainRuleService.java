package com.zz.business.service;
import com.zz.framework.domain.business.LeBargainRule;

public interface LeBargainRuleService {
	//根据discount查询对象
	LeBargainRule getByDiscount(int discount);
}
