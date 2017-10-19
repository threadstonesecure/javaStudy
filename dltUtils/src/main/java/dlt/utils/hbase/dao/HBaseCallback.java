package dlt.utils.hbase.dao;

import java.io.IOException;

import org.apache.hadoop.hbase.client.HTable;

public interface HBaseCallback<T> {
	T doInHBase(HTable hTable) throws IOException;
}
