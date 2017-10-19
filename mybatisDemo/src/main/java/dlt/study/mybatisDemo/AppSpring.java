package dlt.study.mybatisDemo;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import dlt.study.app.DemoService;
import dlt.study.model.Demo;
import dlt.study.util.SpringContextUtils;

public class AppSpring {

	public static void main(String[] args) {
		SpringContextUtils.init("spring/mybatis.xml", "spring/db.xml",
				"spring/prop.xml", "spring/aop.xml","spring/base.xml");
		SqlSessionFactory sessionFactory = SpringContextUtils
				.getBean("sqlSessionFactory");
		String databaseid = sessionFactory.getConfiguration().getDatabaseId();
		System.out.println("databaseid =" + databaseid);
		DemoService demoService = SpringContextUtils.getBean("demoServiceImpl");
		Demo demo = demoService.get(null); //demoService.get("dlt=>-1244746321");
		System.out.println(demo);

		demo = new Demo();
		demo.setSn("dltsn");
		demo.setApp("dltApp");
		//demoService.insert(demo);
		System.out.println("insert demo success!" + demo.getId());

		System.out.println(demo);
	}
}
