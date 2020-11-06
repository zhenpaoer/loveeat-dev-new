package com.zz.area.dao;


import com.zz.framework.common.dao.Mymapper;
import com.zz.framework.domain.area.LeArea;
import com.zz.framework.domain.area.ext.LeAreaNode;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeAreaMapper extends Mymapper<LeArea> {

	//查询所有城市
	@Select("SELECT id,area,parentId FROM le_area WHERE parentId = 0")
	List<LeArea> getAllCitys();



	//根据城市id查询树形的城市-行政区-商圈
	@Select("SELECT id,parentId,area FROM le_area WHERE parentId = #{id}")
	@Results(id = "getCityAreaById",value = {
			@Result(id = true,column = "id",property = "id"),
			@Result(property = "area",column = "area"),
			@Result(property = "parentId",column = "parentId"),
			@Result(property = "children",column = "id"
					//定义一对多的关系映射，实现对account的封装
					//        @Many注解用于一对多或多对多查询使用
					//        select属性指定内容：查询用户的唯一标识
					//        column属性指定内容：用户根据id查询账户是所需要的参数
					//        fetchType属性指定内容:指定延时查询FetchType.LAZY或立即查询FetchType.EAGER
					,many=@Many(select="com.zz.area.dao.LeAreaMapper.getAdministrativeRegionById",fetchType= FetchType.EAGER)
	)

	})
	List<LeAreaNode> getCityAreaById(int id);

	//行政区
	@Select("SELECT id,area,parentId FROM le_area WHERE parentId = #{id}")
	@Results(id = "getAdministrativeRegionById",value = {
			@Result(id = true,column = "id",property = "id"),
			@Result(property = "area",column = "area"),
			@Result(property = "parentId",column = "parentId"),
			@Result(property = "children",column = "id"
					,many=@Many(select="com.zz.area.dao.LeAreaMapper.getAreaById",fetchType= FetchType.EAGER))
	})
	List<LeAreaNode> getAdministrativeRegionById(int id);

	//查商圈
	@Select("SELECT id,parentId,area FROM le_area WHERE parentId = #{id}")
	@Results(id = "getAreaById", value = {
			@Result(id = true,property = "id", column = "id"),
			@Result(property = "area", column = "area"),
			@Result(property = "parentId",column = "parentId")
	})
	List<LeAreaNode> getAreaById(int id);

	//根据城市查询全部商圈及三个热点商圈
	@Select("SELECT id,parentId,area,searchcount FROM le_area WHERE parentId in (SELECT id FROM le_area WHERE parentId = #{id}) order by searchcount desc")
	@Results(id = "getAllAndHotAreasByCittId", value = {
			@Result(id = true,property = "id", column = "id"),
			@Result(property = "area", column = "area"),
			@Result(property = "searchcount", column = "searchcount"),
			@Result(property = "parentId",column = "parentId")
	})
	List<LeAreaNode> getAllAndHotAreasByCittId(int id);




//	insert into tbl_user (name, age) values (#{name}, #{age})
	@Insert("insert into le_area (parentId,area,searchcount,createtime,updatetime) values"
			+ " ("
			+ " #{parentId},"
			+ " #{area},"
			+ " #{searchcount},"
			+ " #{createtime},"
			+ " #{updatetime}"
			+ ")")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int insertArea(LeArea leArea);
	//主键为instance_id，因此需要将keyProperty和keyColumn设置成我们想要的字段。
	// 这个示例的意思就是，从instance_id这个字段把数据放到传入对象instanceDto的instanceId成员变量里面。
	//@Options注解会自动为表的主键字段设置自增的值，并把它赋值给作为入参的DTO，进而可以直接从这个对象中获取新生成记录的主键


	//根据商圈的id更新热值
	@Select("update le_area set searchcount =  #{searchcount} + 1 WHERE searchcount = #{searchcount} and id = #{id}")
	void updateAreaSearchCountById(int id,int searchcount);
}