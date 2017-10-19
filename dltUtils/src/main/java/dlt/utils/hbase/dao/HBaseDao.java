package dlt.utils.hbase.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import dlt.utils.hbase.ByteTo;

/**
 * 封装对HBase的操作
 * 
 * @author dlt
 *
 */
public class HBaseDao implements IHBaseDao {

	public final static String COLENDCHAR = String
			.valueOf(KeyValue.COLUMN_FAMILY_DELIMITER);// ":" 列簇和列之间的分隔符

	private Configuration conf;

	public static HBaseDao getDao(Configuration conf) throws IOException {
		return new HBaseDao(conf);
	}

	public HBaseDao() throws IOException {
		conf = HBaseConfiguration.create();

	}

	public HBaseDao(Configuration conf) throws IOException {
		this.conf = conf;

	}

	public Configuration getConf() {
		return conf;
	}

	@Override
	public boolean tableExists(String tableName) throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		try {
			return admin.tableExists(tableName);
		} finally {
			admin.close();
		}
	}

	@Override
	public HTableDescriptor getDefine(final String tableName)
			throws IOException {
		return execute(tableName, new HBaseCallback<HTableDescriptor>() {
			@Override
			public HTableDescriptor doInHBase(HTable hTable) throws IOException {
				return hTable.getTableDescriptor();

			}
		});
	}

	@Override
	public void createTable(String tableName, String[] familyNames)
			throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		try {
			if (admin.tableExists(tableName)) {
				return;// 判断表是否已经存在
			}
			TableName tn = TableName.valueOf(tableName);
			HTableDescriptor htdesc = new HTableDescriptor(tn);
			if (familyNames != null && familyNames.length > 0) {
				for (int i = 0; i < familyNames.length; i++) {
					String familyName = familyNames[i];
					HColumnDescriptor family = new HColumnDescriptor(familyName);
					htdesc.addFamily(family);
				}
			}
			admin.createTable(htdesc);

		} catch (IOException e) {
			throw e;
		} finally {
			admin.close();
		}
	}

	@Override
	public void dropTable(String tableName) throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		try {
			if (!admin.tableExists(tableName)) {
				throw new IOException(tableName + " is not exists");
			}

			if (admin.isTableAvailable(tableName))
				admin.disableTable(tableName);
			admin.deleteTable(tableName);
		} finally {
			admin.close();
		}
	}

	@Override
	public void dropFamily(String tableName, String familyName)
			throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		try {
			if (!admin.tableExists(tableName)) {
				throw new IOException(tableName + " is not exists");
			}

			if (admin.isTableAvailable(tableName))
				admin.disableTable(tableName);
			admin.deleteColumn(tableName, familyName);
			admin.enableTable(tableName);
		} finally {
			admin.close();
		}
	}

	@Override
	public void addFamily(String tableName, String familyName)
			throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		try {
			if (!admin.tableExists(tableName)) {
				throw new IOException(tableName + " is not exists");
			}

			if (admin.isTableAvailable(tableName))
				admin.disableTable(tableName);
			HColumnDescriptor family = new HColumnDescriptor(familyName);
			admin.addColumn(tableName, family);
			admin.enableTable(tableName);
		} finally {
			admin.close();
		}
	}

	@Override
	public void deleteColumn(String tableName, String rowID, String family,
			String qualifier) throws IOException {
		deleteColumn(tableName, Bytes.toBytes(rowID), family, qualifier);
	}

	@Override
	public void deleteColumn(String tableName, String[] rowIDs, String family,
			String qualifier) throws IOException {
		byte[][] bRowIDs = new byte[rowIDs.length][];
		int i = 0;
		for (String rowID : rowIDs) {
			bRowIDs[i++] = Bytes.toBytes(rowID);
		}
		deleteColumn(tableName, bRowIDs, family, qualifier);
	}

	@Override
	public void deleteColumn(String tableName, byte[] rowID, String family,
			String qualifier) throws IOException {

		byte[][] bRowIDs = new byte[1][];
		bRowIDs[0] = rowID;
		deleteColumn(tableName, bRowIDs, family, qualifier);
	}

	@Override
	public void deleteColumn(String tableName, final byte[][] rowIDs,
			final String family, final String qualifier) throws IOException {
		execute(tableName, new HBaseCallback<Void>() {
			@Override
			public Void doInHBase(HTable hTable) throws IOException {
				List<Delete> dels = new ArrayList<Delete>();
				for (byte[] rowID : rowIDs) {
					Delete del = new Delete(rowID);
					if (!isEmpty(family)) {
						if (isEmpty(qualifier))
							del.deleteFamily(Bytes.toBytes(family));
						else
							del.deleteColumns(Bytes.toBytes(family),
									Bytes.toBytes(qualifier));
					}
					dels.add(del);
				}
				hTable.delete(dels);
				return null;
			}
		});
	}

	@Override
	public void putData(String tableName, String rowID, String family,
			String qualifier, String value) throws IOException {
		putData(tableName, Bytes.toBytes(rowID), family, qualifier,
				Bytes.toBytes(value));
	}

	@Override
	public void putData(String tableName, final byte[] rowID,
			final String family, final String qualifier, final byte[] value)
			throws IOException {

		execute(tableName, new HBaseCallback<Void>() {
			@Override
			public Void doInHBase(HTable hTable) throws IOException {
				Put p = new Put(rowID);
				p.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), value);
				hTable.put(p);
				return null;
			}
		});
	}

	@Override
	public void putData(String tableName, final List<KeyValue> keyValues)
			throws IOException {
		execute(tableName, new HBaseCallback<Void>() {
			@Override
			public Void doInHBase(HTable hTable) throws IOException {
				List<Put> puts = new ArrayList<Put>();
				for (KeyValue keyValue : keyValues) {
					Put p = new Put(CellUtil.cloneRow(keyValue));
					p.add(keyValue);
					puts.add(p);
				}
				hTable.put(puts);
				return null;
			}
		});
	}

	@Override
	public Map<String, String> scanColumn(String tableName, String family,
			String qualifier) throws IOException {
		return scanColumn(tableName, family, qualifier, "", null);
	}

	@Override
	public Map<String, String> scanColumn(String tableName, String family,
			String qualifier, String startRow, String endRow)
			throws IOException {
		byte[] bStartRow = null;
		byte[] bEndRow = null;
		if (!isEmpty(startRow))
			bStartRow = Bytes.toBytes(startRow);
		if (!isEmpty(endRow))
			bEndRow = Bytes.toBytes(endRow);
		return scanColumn(tableName, family, qualifier, bStartRow, bEndRow,
				ByteTo.String, ByteTo.String);
	}

	@Override
	public <V> Map<String, V> scanColumn(String tableName, String family,
			String qualifier, ByteTo<V> valueConver) throws IOException {
		return scanColumn(tableName, family, qualifier, null, null, valueConver);
	}

	@Override
	public <V> Map<String, V> scanColumn(String tableName, String family,
			String qualifier, String startRow, String endRow,
			ByteTo<V> valueConver) throws IOException {
		byte[] bStartRow = null;
		byte[] bEndRow = null;
		if (!isEmpty(startRow))
			bStartRow = Bytes.toBytes(startRow);
		if (!isEmpty(endRow))
			bEndRow = Bytes.toBytes(endRow);
		return scanColumn(tableName, family, qualifier, bStartRow, bEndRow,
				ByteTo.String, valueConver);
	}

	@Override
	public <T, V> Map<T, V> scanColumn(String tableName, final String family,
			final String qualifier, final ByteTo<T> rowKeyConver,
			final ByteTo<V> valueConver) throws IOException {
		return scanColumn(tableName, family, qualifier, null, null,
				rowKeyConver, valueConver);
	}

	@Override
	public <T, V> Map<T, V> scanColumn(String tableName, final String family,
			final String qualifier, final byte[] startRow, final byte[] endRow,
			final ByteTo<T> rowKeyConver, final ByteTo<V> valueConver)
			throws IOException {
		return doScanColumn(tableName, family, qualifier, startRow, endRow,
				rowKeyConver, valueConver);
	}

	public Map<String, Result> scanFamily(String tableName, String family)
			throws IOException {
		return scanFamily(tableName, family, null, null);
	}

	public Map<String, Result> scanFamily(String tableName, String family,
			String startRow, String endRow) throws IOException {
		byte[] bStartRow = null;
		byte[] bEndRow = null;
		if (!isEmpty(startRow))
			bStartRow = Bytes.toBytes(startRow);
		if (!isEmpty(endRow))
			bEndRow = Bytes.toBytes(endRow);
		return scanFamily(tableName, family, bStartRow, bEndRow, ByteTo.String);
	}

	public <T> Map<T, Result> scanFamily(String tableName, String family,
			ByteTo<T> rowKeyConver) throws IOException {
		return scanFamily(tableName, family, null, null, rowKeyConver);
	}

	@Override
	public <T> Map<T, Result> scanFamily(String tableName, final String family,
			final byte[] startRow, final byte[] endRow,
			final ByteTo<T> rowKeyConver) throws IOException {
		return doScan(tableName, family, startRow, endRow, rowKeyConver);
	}

	@Override
	public Map<String, Result> scanRow(String tableName) throws IOException {

		return scanRow(tableName, null, null);
	}

	@Override
	public Map<String, Result> scanRow(String tableName, String startRow,
			String endRow) throws IOException {
		byte[] bStartRow = null;
		byte[] bEndRow = null;
		if (!isEmpty(startRow))
			bStartRow = Bytes.toBytes(startRow);
		if (!isEmpty(endRow))
			bEndRow = Bytes.toBytes(endRow);
		return scanRow(tableName, bStartRow, bEndRow, ByteTo.String);
	}

	@Override
	public <T> Map<T, Result> scanRow(String tableName, ByteTo<T> rowKeyConver)
			throws IOException {

		return scanRow(tableName, null, null, rowKeyConver);
	}

	@Override
	public <T> Map<T, Result> scanRow(String tableName, byte[] startRow,
			byte[] endRow, ByteTo<T> rowKeyConver) throws IOException {

		return doScan(tableName, null, startRow, endRow, rowKeyConver);
	}

	@Override
	public String getCellValue(String tableName, String rowID, String family,
			String qualifier) throws IOException {
		return getCellValue(tableName, Bytes.toBytes(rowID), family, qualifier);
	}

	@Override
	public String getCellValue(String tableName, byte[] rowID, String family,
			String qualifier) throws IOException {
		return getCellValue(tableName, rowID, family, qualifier, ByteTo.String);
	}

	@Override
	public <T> T getCellValue(String tableName, String rowID, String family,
			String qualifier, ByteTo<T> valueConver) throws IOException {
		return getCellValue(tableName, Bytes.toBytes(rowID), family, qualifier,
				valueConver);
	}

	@Override
	public <T> T getCellValue(String tableName, final byte[] rowID,
			final String family, final String qualifier,
			final ByteTo<T> valueConver) throws IOException {
		return doGetCell(tableName, rowID, family, qualifier, valueConver);
	}

	@Override
	public Result getFamily(String tableName, String rowID, String family)
			throws IOException {
		return getFamily(tableName, Bytes.toBytes(rowID), family);
	}

	@Override
	public Result getFamily(String tableName, final byte[] rowID,
			final String family) throws IOException {
		return doGet(tableName, rowID, family);
	}

	@Override
	public Result getRow(String tableName, String rowID) throws IOException {
		return getRow(tableName, Bytes.toBytes(rowID));
	}

	@Override
	public Result getRow(String tableName, byte[] rowID) throws IOException {
		return doGet(tableName, rowID, null);
	}

	@Override
	public <T> T execute(String tableName, HBaseCallback<T> action)
			throws IOException {
		HTable hTable = getHTable(tableName);
		try {
			return doExecute(hTable, action);
		} finally {
			hTable.close();
		}

	}

	protected <T> T doExecute(HTable hTable, HBaseCallback<T> action)
			throws IOException {
		return action.doInHBase(hTable);
	}

	protected HTable getHTable(String tableName) throws IOException {
		if (!tableExists(tableName)) {
			throw new IOException(tableName + " is not exists");
		}
		return new HTable(conf, tableName);
	}

	private boolean isEmpty(byte[] bs) {
		if (bs == null || bs.length == 0)
			return true;
		return false;
	}

	private boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

	private <T, V> Map<T, V> doScanColumn(String tableName,
			final String family, final String qualifier, final byte[] startRow,
			final byte[] endRow, final ByteTo<T> rowKeyConver,
			final ByteTo<V> valueConver) throws IOException {
		return execute(tableName, new HBaseCallback<Map<T, V>>() {
			@Override
			public Map<T, V> doInHBase(HTable hTable) throws IOException {
				Scan scan;
				if (isEmpty(startRow)) {
					scan = new Scan();
				} else {
					if (isEmpty(endRow))
						scan = new Scan(startRow);
					else
						scan = new Scan(startRow, endRow);
				}
				byte[] bFamily = Bytes.toBytes(family);
				byte[] bQualifier = Bytes.toBytes(qualifier);
				scan.addColumn(bFamily, bQualifier);
				ResultScanner scanner = hTable.getScanner(scan);
				Map<T, V> rMap = new HashMap<T, V>();
				for (Result r : scanner) {
					rMap.put(rowKeyConver.to(r.getRow()),
							valueConver.to(r.getValue(bFamily, bQualifier)));
				}
				scanner.close();
				return rMap;
			}
		});
	}

	private <T> Map<T, Result> doScan(String tableName, final String family,
			final byte[] startRow, final byte[] endRow,
			final ByteTo<T> rowKeyConver) throws IOException {
		return execute(tableName, new HBaseCallback<Map<T, Result>>() {
			@Override
			public Map<T, Result> doInHBase(HTable hTable) throws IOException {
				Scan scan;
				if (isEmpty(startRow)) {
					scan = new Scan();
				} else {
					if (isEmpty(endRow))
						scan = new Scan(startRow);
					else
						scan = new Scan(startRow, endRow);
				}
				if (!isEmpty(family)) {
					byte[] bFamily = Bytes.toBytes(family);
					scan.addFamily(bFamily);
				}
				ResultScanner scanner = hTable.getScanner(scan);
				Map<T, Result> rMap = new HashMap<T, Result>();
				for (Result r : scanner) {
					rMap.put(rowKeyConver.to(r.getRow()), r);
				}
				scanner.close();
				return rMap;
			}
		});
	}

	private <T> T doGetCell(String tableName, final byte[] rowID,
			final String family, final String qualifier,
			final ByteTo<T> valueConver) throws IOException {
		return execute(tableName, new HBaseCallback<T>() {
			@Override
			public T doInHBase(HTable hTable) throws IOException {
				Get get = new Get(rowID);
				byte[] bFamily = Bytes.toBytes(family);
				byte[] bQualifier = Bytes.toBytes(qualifier);
				get.addColumn(bFamily, bQualifier);
				Result r = hTable.get(get);
				byte[] v = r.getValue(bFamily, bQualifier);
				if (isEmpty(v)) {
					return null;
				}
				return valueConver.to(v);
			}
		});
	}

	private Result doGet(String tableName, final byte[] rowID,
			final String family) throws IOException {
		return execute(tableName, new HBaseCallback<Result>() {
			@Override
			public Result doInHBase(HTable hTable) throws IOException {
				Get get = new Get(rowID);
				if (!isEmpty(family)) {
					byte[] bFamily = Bytes.toBytes(family);
					get.addFamily(bFamily);
				}
				Result r = hTable.get(get);
				return r;
			}
		});
	}

}