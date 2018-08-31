package dlt.study.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import dlt.study.model.Demo;

public interface DemoMapper2 {
	@Select("select id,sn,app,app_date as appDate,name,code,dept,stage,director,costs,memo,app_dept as appDept from app where id = #{id} ")
	@ResultType(Demo.class)
	Demo selectDemo(String id);

	@Delete("delete app where id = #{id}")
	int deleteDemo(String id);
}
