package com.zz.order.dao;

import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.area.LeArea;
import com.zz.framework.domain.business.LeBargainLog;
import com.zz.framework.domain.order.LeOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeOrderMapper extends Mymapper<LeOrder> {

	@Select("select * from le_order where uid = #{uid} and pid = #{pid} and createdate = #{createDate} and status != -1")
	public LeOrder getOne(int uid , int pid , LocalDate createDate);

	@Select("select * from le_order where id = #{id}")
	public LeOrder getByOrderId(int id);

	@Select("select * from le_order where pid = #{pid} and createdate = #{createDate} and status != -1")
	public LeOrder getByPidAndDate(int pid,LocalDate createDate);

	@Select("select * from le_order where uid = #{uid} order by createtime desc")
	public List<LeOrder> getByUid(int uid);

	@Update("update le_order set status = -1 where id = #{id}")
	public int closeOrder(int id);

	@Insert("insert into le_order (bid,tradeno,pid,uid,status,price,createdate,createtime,updatetime) values"
			+ " ("
			+ " #{bid},"
			+ " #{tradeNo},"
			+ " #{pid},"
			+ " #{uid},"
			+ " #{status},"
			+ " #{price},"
			+ " #{createDate},"
			+ " #{createTime},"
			+ " #{updateTime}"
			+ ")")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int insertOrder(LeOrder order);
}
