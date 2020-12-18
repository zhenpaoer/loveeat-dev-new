package com.zz.order.dao;

import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.order.LeOrder;
import com.zz.framework.domain.order.LeOrderOperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
public interface LeOrderOperateLogMapper extends Mymapper<LeOrderOperateLog> {

	@Insert("insert into le_order_operate_log (uid,oid,content,createtime,updatetime) values"
			+ " ("
			+ " #{uid},"
			+ " #{oid},"
			+ " #{content},"
			+ " #{createTime},"
			+ " #{updateTime}"
			+ ")")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int insertOrderLog(LeOrderOperateLog orderlog);
}
