package com.zz.business.service;


import com.zz.framework.domain.business.LeBargainLog;
import com.zz.framework.domain.business.LeBargainRule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface LeBargainLogService {
	int insert(LeBargainLog log);
	List<LeBargainLog> getListByPidUid(int pid,int uid);//很多时间
	LeBargainLog getByPidUidDate(int pid, int uid, LocalDateTime localDateTime);
	List<LeBargainLog> getListByPidUidDate(List<Integer> pids, int uid, String today);
}
