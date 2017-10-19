package dlt.study.mybatisDemo;

import java.io.IOException;
import java.util.List;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.SqlUtil;

import dlt.study.dao.DemoMapper;
import dlt.study.model.Demo;
import dlt.study.util.MyBatisUtils;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		
		SqlSessionFactory sqlSessionFactory = MyBatisUtils.getSessionFactory();
		try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {

			String demoId = "cf45baa4115d45f7a8e36959abcde55c";
			Demo demo;
			int deleteCount = -1;


			// 接口编程
			System.out.println("接口编程结果：");
			
			PageHelper.startPage(1, 10); //分页插件  http://git.oschina.net/free/Mybatis_PageHelper/blob/master/wikis/HowToUse.markdown
			DemoMapper demoMapper = sqlSession.getMapper(DemoMapper.class);
			Demo qd = new Demo();
			qd.setId(demoId);
			List<Demo> demos = demoMapper.selectDemos(1000); // Page 对象
			System.out.println(demos + ":" + demos.getClass());
			PageInfo page = new PageInfo(demos);
			System.out.println("page:" + SqlUtil.getLocalPage());
			Page p1 =  SqlUtil.getPageFromObject(demos);
/*			deleteCount = demoMapper.deleteDemo(demoId);
			System.out.println("delete from demo :" + deleteCount);

			DemoMapper2 demoMapper2 = sqlSession.getMapper(DemoMapper2.class);
			demo = demoMapper2.selectDemo(demoId);
			System.out.println(demo);

			deleteCount = demoMapper2.deleteDemo(demoId);
			System.out.println("delete from demo :" + deleteCount);*/
			sqlSession.flushStatements();
			//sqlSession.commit();
			//sqlSession.commit();
		}
	}
}
