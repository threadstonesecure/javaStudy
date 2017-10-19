package dlt.study.mybatisDemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import dlt.study.dao.DemoMapper;
import dlt.study.dao.DemoMapper2;
import dlt.study.model.Demo;
import dlt.study.util.MyBatisUtils;

/**
 * Unit test for simple App.
 */
public class AppTest {
	private SqlSessionFactory sessionFactory = MyBatisUtils.getSessionFactory();
	String demoId = "94f45506b4df45fe9e2c6fccea3175a9";

	@Test
	public void nameOper() {

		try (SqlSession sqlSession = sessionFactory.openSession()) {
			Demo demo;
			int deleteCount = -1;
			System.out.println("读取sql执行结果：");
			demo = sqlSession.selectOne(
					"dlt.study.dao.DemoMapper.selectDemo", demoId);
			System.out.println(demo);
			deleteCount = sqlSession.delete(
					"dlt.study.dao.DemoMapper.deleteDemo", demoId);

			System.out.println("delete from demo :" + deleteCount);
		}
	}

	@Test
	public void mapperSelectDemoResultMap() {
		try (SqlSession sqlSession = sessionFactory.openSession()) {

			// 接口编程
			System.out.println("接口编程结果：");
			DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
			Demo demo = demoMapper.selectDemoResultMap(demoId);
			System.out.println(demo);
		}
	}

	@Test
	public void mapperHashMap() {
		try (SqlSession sqlSession = sessionFactory.openSession()) {

			// 接口编程
			System.out.println("接口编程结果：");
			DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
			HashMap<String, Object> demos = demoMapper.selectDemo2(demoId);
			System.out.println(demos);
			for (Entry<String, Object> entry : demos.entrySet()) {
				System.out.println(entry.getKey() + ":" + entry.getValue()
						+ ":" + entry.getValue().getClass());
			}
		}
	}

	@Test
	public void mapperList() {
		try (SqlSession sqlSession = sessionFactory.openSession()) {

			// 接口编程
			System.out.println("接口编程结果：");
			DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
			List<Demo> demos = demoMapper.selectDemos(100, new RowBounds(0,20));
			for (Demo demo : demos) {
				System.out.println(demo);
				Demo parent = demo.getParent();
				System.out.println("parent:"
						+ (parent == null ? "" : parent.hashCode()));
			}

		}
	}

	// List<Demo> selectDemosWhereIn(List<String> demoIds);

	@Test
	public void mapperWhereIn() {
		try (SqlSession sqlSession = sessionFactory.openSession()) {

			// 接口编程
			System.out.println("接口编程结果：");
			DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
			List<String> demoIds = new ArrayList<String>();
			demoIds.add("dlt=>-1244746321");
			demoIds.add("9b485245c1fb4565959d36ccfe379d3b");
			List<Demo> demos = demoMapper.selectDemosWhereIn(demoIds);
			for (Demo demo : demos) {
				System.out.println(demo);
			}

		}
	}

	// List<Demo> selectDemosMap(Map<String,Object> params);

	@Test
	public void mapperParamMap() {
		try (SqlSession sqlSession = sessionFactory.openSession()) {

			// 接口编程
			System.out.println("接口编程结果：");
			DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", "dlt=>-1244746321");
			//map.put("name", "deng");
			List<Demo> demos = demoMapper.selectDemosMap(map);
			for (Demo demo : demos) {
				System.out.println(demo);
			}

		}
	}

	@Test
	public void mapperInsert() {
		try (SqlSession sqlSession = sessionFactory.openSession()) {

			// 接口编程
			System.out.println("接口编程结果：");
			DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
			Demo demo = new Demo();
			String id = "dlt=>" + (new Random(1000)).nextInt();
			demo.setId(id);
			demo.setSn("dltsn");
			demo.setApp("dltApp");
			demoMapper.insertDemo(demo);
			System.out.println("insert demo success!" + id);
			sqlSession.commit();
			Demo tempDemo = demoMapper.selectDemo(id);
			System.out.println(demo);
		}
	}

	@Test
	public void mapperInsertDemoAutoID() {
		try (SqlSession sqlSession = sessionFactory.openSession()) {

			// 接口编程
			System.out.println("接口编程结果：");
			DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
			Demo demo = new Demo();
			demo.setSn("dltsn");
			demo.setApp("dltApp");
			demoMapper.insertDemoAutoID(demo);
			System.out.println("insert demo success!" + demo.getId());
			sqlSession.commit();
			System.out.println(demo);
		}
	}

	@Test
	public void mapperUpdate() {
		try (SqlSession sqlSession = sessionFactory.openSession()) {

			// 接口编程
			System.out.println("接口编程结果：");
			DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
			String id = "dlt=>-1244746321";
			Demo demo = demoMapper.selectDemo(id);
			demo.setApp("zyyApp");
			demo.setSn("zyyApp");
			demoMapper.updateDemo(demo);
			sqlSession.commit();
			Demo tempDemo = demoMapper.selectDemo(id);
			System.out.println(demo);
		}
	}

	@Test
	public void mapperDeleteDemo() {
		try (SqlSession sqlSession = sessionFactory.openSession()) {
			DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
			int deleteCount = -1;
			// 接口编程
			System.out.println("接口编程结果：");
			deleteCount = demoMapper.deleteDemo(demoId);
			System.out.println("delete from demo :" + deleteCount);

			// sqlSession.commit();
		}
	}

	@Test
	public void mapperDeleteDemo2() {
		try (SqlSession sqlSession = sessionFactory.openSession()) {
			DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
			int deleteCount = -1;
			// 接口编程
			System.out.println("接口编程结果：");
			Demo demo = new Demo();
			demo.setId(demoId);
			demo.setName("denglt");
			deleteCount = demoMapper.deleteDemo2(demo);
			System.out.println("delete from demo :" + deleteCount);

			// sqlSession.commit();
		}
	}

	@Test
	public void mapper2Oper() {
		try (SqlSession sqlSession = sessionFactory.openSession()) {
			Demo demo;
			int deleteCount = -1;
			// 接口编程
			System.out.println("接口编程结果2：");
			DemoMapper2 demoMapper2 = sqlSession.getMapper(DemoMapper2.class);
			demo = demoMapper2.selectDemo(demoId);
			System.out.println(demo);

			deleteCount = demoMapper2.deleteDemo(demoId);
			System.out.println("delete from demo :" + deleteCount);
			// sqlSession.commit();
		}
	}
}
