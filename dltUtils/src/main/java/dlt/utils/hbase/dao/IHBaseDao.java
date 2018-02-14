package dlt.utils.hbase.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;

import dlt.utils.hbase.ByteTo;

/**
 * 对hbase的DAO操作(接口)
 * 
 * @author dlt
 *
 */
 interface IHBaseDao {

	 boolean tableExists(String tableName) throws IOException;
	

	 HTableDescriptor getDefine(String tableName) throws IOException;

	/**
	 * 创建表
	 * 
	 * @param tableName
	 *            表名,
	 * @param familyNames
	 *            family name组成的数组
	 * @throws java.io.IOException
	 */
	void createTable(String tableName, String[] familyNames) throws IOException;

	/**
	 * 删除表
	 *
	 * @param tableName
	 * @throws java.io.IOException
	 */
	void dropTable(String tableName) throws IOException;

	/**
	 * 删除列名(也就是删除family.属于修改表结构的操作.调用的时候请慎重)
	 *
	 * @param tableName
	 * @param familyName
	 * @throws java.io.IOException
	 */
	void dropFamily(String tableName, String familyName) throws IOException;

	/**
	 * 给表增加一个 family
	 *
	 * @param tableName
	 * @param familyName
	 * @throws java.io.IOException
	 */
	 void addFamily(String tableName, String familyName)
			throws IOException;

	/**
	 * 删除某个family下的某个列的某行
	 *
	 * @param tableName
	 *            表名
	 * @param rowID
	 *            行名
	 * @param family
	 *            family name(列簇)
	 * @param qualifier
	 *            列名 : 如果 qualifier为 null，删除family里的所有column
	 * @throws java.io.IOException
	 */
	void deleteColumn(String tableName, String rowID, String family,
                      String qualifier) throws IOException;

	 void deleteColumn(String tableName, String[] rowIDs, String family,
                             String qualifier) throws IOException;

	 void deleteColumn(String tableName, byte[] rowID, String family,
                             String qualifier) throws IOException;

	 void deleteColumn(String tableName, byte[][] rowID, String family,
                             String qualifier) throws IOException;

	/**
	 * 更新和插入一列数据
	 *
	 * @param tableName
	 * @param rowID
	 * @param family
	 * @param qualifier
	 * @param value
	 * @throws java.io.IOException
	 */
	 void putData(String tableName, String rowID, String family,
                        String qualifier, String value) throws IOException;

	 void putData(String tableName, byte[] rowID, String family,
                        String qualifier, byte[] value) throws IOException;

	 void putData(String tableName, final List<KeyValue> keyValues)
			throws IOException;

	/**
	 * 扫描一列数据
	 *
	 * @param tableName
	 * @param family
	 * @param qualifier
	 * @param startRow
	 * @param endRow
	 * @return
	 * @throws java.io.IOException
	 */

	 Map<String, String> scanColumn(String tableName, String family,
                                          String qualifier) throws IOException;

	 Map<String, String> scanColumn(String tableName, String family,
                                          String qualifier, String startRow, String endRow)
			throws IOException;

	 <V> Map<String, V> scanColumn(String tableName, String family,
                                         String qualifier, ByteTo<V> valueConver) throws IOException;

	 <V> Map<String, V> scanColumn(String tableName, String family,
                                         String qualifier, String startRow, String endRow,
                                         ByteTo<V> valueConver) throws IOException;

	 <T, V> Map<T, V> scanColumn(String tableName, String family,
                                       String qualifier, ByteTo<T> rowKeyConver, ByteTo<V> valueConver)
			throws IOException;

	 <T, V> Map<T, V> scanColumn(String tableName, String family,
                                       String qualifier, byte[] startRow, byte[] endRow,
                                       ByteTo<T> rowKeyConver, ByteTo<V> valueConver) throws IOException;

	/**
	 * 扫描一个family数据
	 *
	 * @param tableName
	 * @param family
	 * @param startRow
	 * @param endRow
	 * @param rowKeyConver
	 * @return
	 * @throws java.io.IOException
	 */
	 Map<String, Result> scanFamily(String tableName, String family)
			throws IOException;

	 Map<String, Result> scanFamily(String tableName, String family,
                                          String startRow, String endRow) throws IOException;

	 <T> Map<T, Result> scanFamily(String tableName, String family,
                                         ByteTo<T> rowKeyConver) throws IOException;

	 <T> Map<T, Result> scanFamily(String tableName, String family,
                                         byte[] startRow, byte[] endRow, ByteTo<T> rowKeyConver)
			throws IOException;

	/**
	 * 扫描整行 数据
	 *
	 * @param tableName
	 * @return
	 * @throws java.io.IOException
	 */
	 Map<String, Result> scanRow(String tableName) throws IOException;

	 Map<String, Result> scanRow(String tableName, String startRow,
                                       String endRow) throws IOException;

	 <T> Map<T, Result> scanRow(String tableName, ByteTo<T> rowKeyConver)
			throws IOException;

	 <T> Map<T, Result> scanRow(String tableName, byte[] startRow,
                                      byte[] endRow, ByteTo<T> rowKeyConver) throws IOException;

	/**
	 * 获取某一行,某一列簇中的某一列的值
	 *
	 * @param tableName
	 * @param rowID
	 * @param family
	 * @param column
	 * @param valueConver
	 * @return
	 * @throws java.io.IOException
	 */

	 String getCellValue(String tableName, String rowID, String family,
                               String qualifier) throws IOException;

	 String getCellValue(String tableName, byte[] rowID, String family,
                               String qualifier) throws IOException;

	 <T> T getCellValue(String tableName, String rowID, String family,
                              String qualifier, ByteTo<T> valueConver) throws IOException;

	 <T> T getCellValue(String tableName, byte[] rowID, String family,
                              String qualifier, ByteTo<T> valueConver) throws IOException;

	/**
	 * 获取某一行,某一列簇中的所有值
	 *
	 * @param tableName
	 * @param rowID
	 * @param family
	 * @return
	 * @throws java.io.IOException
	 */
	 Result getFamily(String tableName, String rowID, String family)
			throws IOException;

	 Result getFamily(String tableName, byte[] rowID, String family)
			throws IOException;

	/**
	 * 获取某一行数据
	 *
	 * @param tableName
	 * @param rowID
	 * @return
	 * @throws java.io.IOException
	 */
	 Result getRow(String tableName, String rowID) throws IOException;

	 Result getRow(String tableName, byte[] rowID) throws IOException;

	/**
	 *
	 * @param tableName
	 * @param action
	 * @return
	 * @throws java.io.IOException
	 */
	 <T> T execute(String tableName, HBaseCallback<T> action)
			throws IOException;
}