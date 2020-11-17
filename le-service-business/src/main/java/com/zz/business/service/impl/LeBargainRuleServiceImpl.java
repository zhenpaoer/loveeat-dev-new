package com.zz.business.service.impl;

import com.zz.business.dao.LeBargainRuleMapper;
import com.zz.business.service.LeBargainRuleService;
import com.zz.framework.domain.business.LeBargainRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeBargainRuleServiceImpl implements LeBargainRuleService {
	@Autowired
	LeBargainRuleMapper leBargainRuleMapper;

	@Override
	public LeBargainRule getByDiscount(int discount) {
		if (discount <= 0){
			discount = 1;
		}
		if (discount >= 10){
			discount = 9;
		}

		return leBargainRuleMapper.getByDiscount(discount);
	}
}
