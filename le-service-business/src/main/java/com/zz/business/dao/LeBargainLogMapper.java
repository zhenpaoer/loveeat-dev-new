package com.zz.business.dao;

import com.zz.framework.domain.business.LeBargainLog;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeBargainLogMapper {
	List<LeBargainLog> getListByPidUid(int pid, int uid);//很多时间
	LeBargainLog getByPidUidDate(int pid, int uid, LocalDateTime today);

	//传入多个pid 1个uid 今天的 参数查询有没有日志
	@Select({
			" <script> ",
			" select id,pid,uid,price,date_format(createtime, '%Y-%m-%d') AS createtime,updatetime",
			" from le_bargain_log ",
			"  where  uid = #{uid} and pid in ",
			" <foreach collection='pids' item='id' open='(' separator=',' close=')'> ",
			" #{id} ",
			" </foreach> ",
			" having createtime = #{today} ",
			" </script> "
	})
	List<LeBargainLog> getListByPidUidDate(List<Integer> pids, int uid, String today);
}
