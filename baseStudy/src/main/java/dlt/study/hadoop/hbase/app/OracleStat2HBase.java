package dlt.study.hadoop.hbase.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

import dlt.utils.hbase.dao.IHBaseDao;

public class OracleStat2HBase implements Callable<Integer> {

	private Connection conn;
	private IHBaseDao hbaseDao;
	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final String tableName = "oracle_sysstat";
	private List<String> familys = new ArrayList<String>();
	private int batchSize = 1000;
	private boolean isDebug = false;

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public OracleStat2HBase(Connection conn, IHBaseDao hbaseDao)
			throws IOException {
		this.conn = conn;
		this.hbaseDao = hbaseDao;
		initHTable();
	}

	private void initHTable() throws IOException {
		if (!hbaseDao.tableExists(tableName)) {
			hbaseDao.createTable(tableName, new String[] { "dbinfo" });
		}
		HTableDescriptor tableDefile = hbaseDao.getDefine(tableName);
		HColumnDescriptor[] hcds = tableDefile.getColumnFamilies();
		for (HColumnDescriptor hcd : hcds) {
			familys.add(hcd.getNameAsString());
		}
	}

	private void addFamily(String familyName) throws IOException {
		for (String fname : familys) {
			if (fname.equals(familyName)) {
				return;
			}
		}
		hbaseDao.addFamily(tableName, familyName);
		familys.add(familyName);
	}

	@Override
	public Integer call() throws Exception {
		String sql = "select t.rowid id, t.ip, t.instance_name, t.key_class, t.key, t.value,t.collect_time  "
				+ "  from TB_DBSTAT t where rownum <= ?";
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setFetchSize(1000);
			stmt.setInt(1, batchSize);
			debug("开始从Oracle取数据并转换");
			ResultSet rs = stmt.executeQuery();
			debug("完成SQL执行！");
			List<String> rowids = new ArrayList<String>();
			List<KeyValue> kvs = new ArrayList<KeyValue>();
			int i = 0;
			
			while (rs.next()) {
				i++;
				rowids.add(rs.getString("id"));
				byte[] rowKey = getRowKey(rs.getTimestamp("collect_time"),
						rs.getString("ip"), rs.getString("instance_name"));
				addFamily("dbinfo");
				kvs.add(newKeyValue(rowKey, "dbinfo", "ip", rs.getString("ip")));
				kvs.add(newKeyValue(rowKey, "dbinfo", "instance_name",
						rs.getString("instance_name")));
				kvs.add(newKeyValue(rowKey, "dbinfo", "collect_time",
						dateFormat2.format(rs.getTimestamp("collect_time"))));
				String key_class = rs.getString("key_class");
				key_class = key_class.toLowerCase();
				key_class = key_class.replaceAll(" ", "_");
				addFamily(key_class);
				String key = rs.getString("key");
				key = key.toLowerCase();
				key = key.replaceAll(" ", "_");
				Long value = rs.getLong("value");
				kvs.add(newKeyValue(rowKey, key_class, key,
						Bytes.toBytes(value)));
			}
			rs.close();
			debug("完成从Oracle取数据并转换！");
			debug("开始写数据到hbase");
			hbaseDao.putData(tableName, kvs);
			kvs.clear();
			debug("完成写数据到hbase！");
			stmt.close();

			debug("开始删除Oracle上的数据");
			stmt = conn.prepareStatement("delete TB_DBSTAT where rowid = ? ");
			for (String rowid : rowids) {
				stmt.setString(1, rowid);
				stmt.addBatch();
			}
			stmt.executeBatch();
			conn.commit();
			rowids.clear();
			debug("完成删除Oracle上的数据！");
			return i;
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	private KeyValue newKeyValue(byte[] rowKey, String family,
			String qualifier, String value) {
		KeyValue ky = new KeyValue(rowKey, Bytes.toBytes(family),
				Bytes.toBytes(qualifier), Bytes.toBytes(value));
		return ky;
	}

	private KeyValue newKeyValue(byte[] rowKey, String family,
			String qualifier, byte[] value) {
		KeyValue ky = new KeyValue(rowKey, Bytes.toBytes(family),
				Bytes.toBytes(qualifier), value);
		return ky;
	}

	public byte[] getRowKey(Date d, String ip, String instanceName) {
		String rowKey = dateFormat.format(d) + "_" + ip + "_" + instanceName;
		return Bytes.toBytes(rowKey);

	}

	private void debug(String info) {
		if (isDebug) {
			Date date = new Date();
			System.out.println(dateFormat2.format(date) + ":" + info);
		}
	}

}
