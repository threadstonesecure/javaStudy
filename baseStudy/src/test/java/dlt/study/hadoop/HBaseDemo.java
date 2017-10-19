package dlt.study.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

public class HBaseDemo {

	public byte[] toBytes(String text) {
		return Bytes.toBytes(text);
	}

	@Test
	public void putData() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		/*
		 * conf.set("hbase.zookeeper.quorum",
		 * "172.16.110.132,172.16.110.133,172.16.110.134");
		 */
		HTable hTable = new HTable(conf, "people");
		System.out.println("Table is:" + Bytes.toString(hTable.getTableName()));
		// Put put = new Put(toBytes("3"));
		Put put = new Put(Bytes.toBytes("5"));
		put.add(toBytes("info"), toBytes("name"), toBytes("zyy3"));
		put.add(toBytes("info"), toBytes("age"), toBytes("36"));
		put.add(toBytes("info"), toBytes("sex"), toBytes("女"));
		KeyValue kv = new KeyValue(Bytes.toBytes("5"), toBytes("info"),
				toBytes("name"), toBytes("DZY"));

		put.add(kv);
		hTable.put(put);
		hTable.close();
	}

	@Test
	public void getData() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		HTable hTable = new HTable(conf, "people");
		String rowkey = "3";
		Get get = new Get(toBytes(rowkey));
		Result result = hTable.get(get);

		hTable.close();
	}
	
	@Test
	public void createTable() throws IOException{
		
	}
}
