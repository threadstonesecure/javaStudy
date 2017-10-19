package dlt.study.hadoop;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import dlt.utils.hbase.ByteTo;
import dlt.utils.hbase.dao.HBaseDao;
import dlt.utils.hbase.dao.IHBaseDao;

public class TestHBaseDao {
	private IHBaseDao hbaseDao;

	private String tableName = "people";

	@Before
	public void init() throws IOException {
		hbaseDao = new HBaseDao();
	}

	@Test
	public void createTable() throws IOException {

		hbaseDao.createTable("xw", new String[] { "info", "info2" });
	}

	@Test
	public void dropFamily() throws IOException {
		hbaseDao.dropFamily(tableName, "info2");
	}

	@Test
	public void dropTable() throws IOException {
		hbaseDao.dropTable(tableName);
	}

	@Test
	public void putData() throws IOException {
		hbaseDao.deleteColumn(tableName, "1", null, null);
		hbaseDao.putData(tableName, "1", "info", "name", "denglt");
		hbaseDao.putData(tableName, "1".getBytes(), "info", "age",
				Bytes.toBytes(36));
		hbaseDao.putData(tableName, "2", "info", "name", "zyy");
		hbaseDao.putData(tableName, "2".getBytes(), "info", "age",
				Bytes.toBytes(Integer.MAX_VALUE));
	}

	@Test
	public void scanColumn() throws IOException {
		Map<String, String> result = hbaseDao.scanColumn(tableName, "info",
				"name");
		System.out.println(result);
	}

	@Test
	public void scanColumn2() throws IOException {
		Map<String, Integer> result = hbaseDao.scanColumn(tableName, "info",
				"age", "9", null, ByteTo.Integer);
		System.out.println(result);
	}

	@Test
	public void scanFamily() throws IOException {
		Map<String, Result> result = hbaseDao.scanFamily(tableName, "info",
				ByteTo.String);
		System.out.println(result);
	}

	@Test
	public void getCellValue() throws IOException {
		Integer age = hbaseDao.getCellValue(tableName, "100", "info", "age",
				ByteTo.Integer);
		System.out.println(age);
	}
	
	@Test
	public void rowKeyLength(){
		
		int key="422326197604160016".hashCode();
		System.out.println(key);
		byte[] bs =Bytes.toBytes(key);//Bytes.toBytes("422326197604160016_201509241331");
		System.out.println(bs.length);
		
		long time = 201509241331l;
		           
		bs = Bytes.toBytes(time);
		System.out.println(bs.length);
	}
	
	@Test
	public void dateRowKey(){
		Date d = new Date();
		System.out.println(d.getTime());
		System.out.println( Long.MAX_VALUE - d.getTime());
	}
}
