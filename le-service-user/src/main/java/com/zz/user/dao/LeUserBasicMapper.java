package com.zz.user.dao;

import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.user.LeUserBasic;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface LeUserBasicMapper extends Mymapper<LeUserBasic> {

	@Update("update le_user_basic set updatetime = now(),nickname=#{nickName},avatar = #{avatarUrl},address = #{address},lon = #{lon},lat = #{lat} where openid = #{openid}")
	public Integer updateUserLogin(String openid,String nickName,String avatarUrl,String address,String lon,String lat);

	@Select("select * from le_user_basic where openid = #{openid}")
	public LeUserBasic getByOpenId(String openid);
}