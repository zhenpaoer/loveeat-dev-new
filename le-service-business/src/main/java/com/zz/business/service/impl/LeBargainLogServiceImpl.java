package com.zz.business.service.impl;

import com.zz.business.dao.LeBargainLogMapper;
import com.zz.business.service.LeBargainLogService;
import com.zz.framework.domain.business.LeBargainLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeBargainLogServiceImpl implements LeBargainLogService {
	@Autowired
	LeBargainLogMapper leBargainLogMapper;

	@Override
	public int insert(LeBargainLog log) {
		return leBargainLogMapper.insertLog(log);
	}

	@Override
	public List<LeBargainLog> getListByPidUid(int pid, int uid) {
		return null;
	}

	@Override
	public LeBargainLog getByPidUidDate(int pid, int uid, LocalDateTime localDateTime) {
		return null;
	}

	//传入多个pid 1个uid 今天的 参数查询有没有日志
	@Override
	public List<LeBargainLog> getListByPidUidDate(List<Integer> pids, int uid, String today) {
		return leBargainLogMapper.getListByPidUidDate(pids, uid, today);
	}
}
