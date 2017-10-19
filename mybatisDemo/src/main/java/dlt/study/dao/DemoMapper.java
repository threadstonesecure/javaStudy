package dlt.study.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.session.RowBounds;

import dlt.study.model.Demo;

public interface DemoMapper {


	Demo selectDemo(String id);

	Demo selectDemo(Demo demo);

	Demo selectDemoResultMap(String id);

	HashMap<String, Object> selectDemo2(String demoId);

	List<Demo> selectDemos(int count);

	List<Demo> selectDemos(int count, RowBounds rb); // RowBounds仅为内存中的分页。

	List<Demo> selectDemosWhereIn(List<String> demoIds);

	List<Demo> selectDemosMap(Map<String, Object> params);

	int deleteDemo(String id);

	int deleteDemo2(Demo demo);

	void insertDemo(Demo demo);

	void updateDemo(Demo demo);

	void insertDemoAutoID(Demo demo);
}
