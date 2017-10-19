package dlt.study.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtils {
	static final String resource = "MybatisConfig.xml";
	static final String environment = "development";
	static SqlSessionFactory sessionFactory;
	static {
		try {
			Reader reader = Resources.getResourceAsReader(resource);
			sessionFactory = new SqlSessionFactoryBuilder().build(reader,
					environment);
			String databaseid = sessionFactory.getConfiguration().getDatabaseId();
			System.out.println("databaseid =" + databaseid);
		} catch (IOException e) {
			e.printStackTrace();
			sessionFactory = null;

		}

	}

	public static SqlSessionFactory getSessionFactory() {
		return sessionFactory;

	}
}
