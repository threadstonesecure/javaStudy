package dlt.study.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobConf;
import org.junit.Test;

import dlt.utils.hdfs.dao.HdfsDao;
import dlt.utils.hdfs.dao.IHdfsDao;

public class TestHdfsDAO {

	@Test
	public void readFile() throws IOException {
		IHdfsDao hdfsDao = new HdfsDao("hdfs://172.16.110.133:9000");
		hdfsDao.cat("/test/input/error.log");
		// System.out.println(context);
	}

	@Test
	public void readFile2() throws IOException {

		IHdfsDao hdfsDao = new HdfsDao("hdfs://172.16.108.210:8020");
		hdfsDao.cat("/user/root/bas_region/part-m-00000");

	}

	@Test
	public void mapRed() throws IOException {
		JobConf conf = new JobConf(TestHdfsDAO.class);
		conf.setJobName("HdfsDAO");
		conf.addResource("classpath:/hadoop/core-site.xml");
		conf.addResource("classpath:/hadoop/hdfs-site.xml");
		conf.addResource("classpath:/hadoop/mapred-site.xml");
		IHdfsDao hdfsDao = new HdfsDao("hdfs://172.16.110.133:9000", conf);
		hdfsDao.cat("/data/error.log");

	}

	@Test
	public void putFile() throws IOException {
		IHdfsDao hdfsDao = new HdfsDao("hdfs://172.16.110.133:9000");

		hdfsDao.copyFile("E:/oradba_output/ddl_owner_rmgz_kf.sql",
				"/test/input/ddl_owner_rmgz_kf.sql");
	}

	@Test
	public void putFile2() throws IOException {
		Configuration conf = new Configuration();
		conf.set("dfs.replication", "1");
		IHdfsDao hdfsDao = new HdfsDao("hdfs://172.16.108.210:8020", conf);
		hdfsDao.copyFile("E:/oradba_output/ddl_owner_rmgz_kf.sql",
				"/test/input/ddl_owner_rmgz_kf.sql");
		// hdfsDao.copyFile("F:/tool/java/jdk/jdk-6u45-linux-x64-rpm.bin",
		// "/data/jdk-6u45-linux-x64-rpm.bin2");
	}

	@Test
	public void configurationInfo() {
		Configuration conf = new Configuration();
		System.out.println(conf.get("dfs.replication"));
	}
}
