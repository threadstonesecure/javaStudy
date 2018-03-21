package dlt.study.rocksdb;


import com.google.common.collect.Lists;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.Test;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * NOTE Microsoft Windows Users: If you are using the Maven Central compiled artifacts on Microsoft Windows,
 * they were compiled using Microsoft Visual Studio 2015, if you don't have "Microsoft Visual C++ 2015 Redistributable" installed,
 * then you will need to install it from https://www.microsoft.com/en-us/download/details.aspx?id=48145, or otherwise build your own binaries from source code.
 * <p>
 * java.lang.UnsatisfiedLinkError: C:\Users\denglt\AppData\Local\Temp\librocksdbjni 5827299809774087783.dll: Can't find dependent libraries
 */
public class RocksDBDemo {

    private String dbName = "D:\\rocksdb\\mystudy";

    @Test
    public void openAndCreateDB() throws Exception {
        try (//Statistics stats = new Statistics();
             Options options = new Options().setCreateIfMissing(true).setComparator(BuiltinComparator.BYTEWISE_COMPARATOR)//.useFixedLengthPrefixExtractor()
                     // .setStatistics(stats)
                     .setWriteBufferSize(8 * SizeUnit.KB) // default 4M
                     .setMaxWriteBufferNumber(3) // default 2
                     .setMaxBackgroundCompactions(2) // default 1 ,后台压缩thread 数量
                     // .setCompressionType(CompressionType.SNAPPY_COMPRESSION) rocksdb 没有link 第三方压缩包,需要自行install. @see:https://github.com/facebook/rocksdb/blob/master/INSTALL.md
                     .setCompressionType(CompressionType.NO_COMPRESSION)
                     .setCompactionStyle(CompactionStyle.UNIVERSAL)
                     .setMemTableConfig(   //缓冲区
                             new HashSkipListMemTableConfig()
                                     .setHeight(4)
                                     .setBranchingFactor(4)
                                     .setBucketCount(2000000)).setAllowConcurrentMemtableWrite(false)
                     .setMergeOperator(new StringAppendOperator());
             // .setMergeOperator(new CassandraValueMergeOperator(1));
             // .setTableFormatConfig(new PlainTableConfig());
             RocksDB myDb = RocksDB.open(options, dbName)) {
            WriteOptions wOpts = new WriteOptions().setDisableWAL(false)/*process crashes*/.setSync(false)/*machine crashes*/;
            myDb.put("1".getBytes(), "denglt".getBytes(StandardCharsets.UTF_8));
            myDb.put("2".getBytes(), "zyy".getBytes(StandardCharsets.UTF_8));
            myDb.put(wOpts, "3".getBytes(), "鼎鼎大名ddddd".getBytes(StandardCharsets.UTF_8));
            myDb.merge("3".getBytes(), "denglt".getBytes(StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get("3".getBytes()), StandardCharsets.UTF_8));
            // myDb.merge("4".getBytes(), "hello world".getBytes(StandardCharsets.UTF_8));
/*            ColumnFamilyOptions cfOpts = new ColumnFamilyOptions().optimizeUniversalStyleCompaction();
            ColumnFamilyDescriptor cfDescriptor = new ColumnFamilyDescriptor("my-first-columnfamily".getBytes(), cfOpts);
            ColumnFamilyHandle columnFamily = myDb.createColumnFamily(cfDescriptor);
            myDb.put(columnFamily, "1".getBytes(), "denglt_family".getBytes(StandardCharsets.UTF_8));
            ColumnFamilyHandle columnFamily2 = myDb.createColumnFamily(cfDescriptor); // org.rocksdb.RocksDBException: Column family already exists
            myDb.put(columnFamily, "1".getBytes(), "denglt_family2".getBytes(StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get(columnFamily, "1".getBytes()), StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get(columnFamily2, "1".getBytes()), StandardCharsets.UTF_8));*/

        }
    }

    @Test
    public void read() throws Exception {

        try (Options options = new Options().setMergeOperator(new StringAppendOperator());
             RocksDB myDb = RocksDB.open(options, dbName)) {
            ReadOptions rOpts = new ReadOptions();
            System.out.println(new String(myDb.get("1".getBytes()), StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get("2".getBytes()), StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get(rOpts, "3".getBytes()), StandardCharsets.UTF_8));

            ColumnFamilyOptions cfOpts = new ColumnFamilyOptions().optimizeUniversalStyleCompaction();
            ColumnFamilyDescriptor cfDescriptor = new ColumnFamilyDescriptor("my-first-columnfamily".getBytes(), cfOpts);
            ColumnFamilyHandle columnFamily = myDb.createColumnFamily(cfDescriptor);
            System.out.println(new String(myDb.get(columnFamily, "1".getBytes()), StandardCharsets.UTF_8));




            //;myDb.multiGet();
            // myDb.close();
            //myDb.delete();
        }
    }

    /**
     * Snapshot API 允许应用程序创建数据库的时间点视图。
     * Get 和 Iterator API 可用于从指定的快照读取数据。
     * 在某种意义上，Snapshot 和 Iterator 都提供了数据库的时间点视图，但它们的实现是不同的。
     * 短期扫描最好通过迭代器完成，而长时间运行的扫描最好通过快照完成。
     * 迭代器对与数据库的该时间点视图相对应的所有底层文件保持引用计数 - 这些文件在 Iterator 被释放之前不会被删除。
     * 另一方面，快照不会防止文件被删除; 但在压缩过程中，压缩程序能够判断快照的存在，它不会删除在任何现有快照中可见的 key。
     * 快照不会在数据库重新启动后保持持久化，因此重新加载 RocksDB 库（通过服务器重新启动）会释放所有预先存在的快照。
     *
     * @throws Exception
     */
    @Test
    public void iterator() throws Exception {
        try (Options options = new Options().setMergeOperator(new StringAppendOperator());
             RocksDB myDb = RocksDB.open(options, dbName)) {
            ReadOptions readOptions = new ReadOptions();//.setPrefixSameAsStart();
            RocksIterator iterator = myDb.newIterator(readOptions);

            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                //  iterator.status();
                System.out.println(new String(iterator.key(), StandardCharsets.UTF_8) + "->" + new String(iterator.value(), StandardCharsets.UTF_8));
            }
            // iterator.seek();
        }
    }

    @Test
    public void openOnColumnFamily() throws Exception {
        try (ColumnFamilyOptions cfOpts = new ColumnFamilyOptions().optimizeUniversalStyleCompaction()/*.setMergeOperator(new StringAppendOperator())*/) {
            // list of column family descriptors, first entry must always be default column family
            final List<ColumnFamilyDescriptor> cfDescriptors = Lists.newArrayList(
                    new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOpts),
                    new ColumnFamilyDescriptor("my-first-columnfamily".getBytes(), cfOpts)
            );
            // a list which will hold the handles for the column families once the db is opened
            final List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();

            try (final DBOptions options = new DBOptions()
                    // .setCreateMissingColumnFamilies()
                    .setCreateIfMissing(true)
                    .setCreateMissingColumnFamilies(true);
                 final RocksDB myDb = RocksDB.open(options, "D:\\rocksdb\\myCF", cfDescriptors, columnFamilyHandleList)) {

                try {
                    myDb.put("1".getBytes(), "denglt".getBytes(StandardCharsets.UTF_8));
                    myDb.put("2".getBytes(), "zyy".getBytes(StandardCharsets.UTF_8));
                    WriteOptions wOpts = new WriteOptions().setDisableWAL(false).setSync(false);
                    myDb.put(wOpts, "3".getBytes(), "鼎鼎大名ddddd".getBytes(StandardCharsets.UTF_8));

                    myDb.put(columnFamilyHandleList.get(1), "1".getBytes(), "denglt_2".getBytes(StandardCharsets.UTF_8));
                    myDb.put(columnFamilyHandleList.get(1), "2".getBytes(), "zyy_2".getBytes(StandardCharsets.UTF_8));
                    //WriteOptions wOpts = new WriteOptions().setDisableWAL(false).setSync(false);
                    myDb.put(columnFamilyHandleList.get(1), wOpts, "3".getBytes(), "鼎鼎大名ddddd_3".getBytes(StandardCharsets.UTF_8));
                } finally {

                    // NOTE frees the column family handles before freeing the db
                    for (final ColumnFamilyHandle columnFamilyHandle : columnFamilyHandleList) {
                        System.out.println(columnFamilyHandle.getClass());
                        columnFamilyHandle.close();
                    }
                } // frees the db and the db options
            }
        }
    }

    @Test
    public void readOnColumnFamily() throws Exception {

        try (ColumnFamilyOptions cfOpts = new ColumnFamilyOptions().optimizeUniversalStyleCompaction()) {

            final List<ColumnFamilyDescriptor> cfDescriptors = Lists.newArrayList(

                    new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOpts),
                    new ColumnFamilyDescriptor("my-first-columnfamily".getBytes(), cfOpts),
                    new ColumnFamilyDescriptor("no-columnfamily".getBytes(), cfOpts)
            );

            final List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();

            try (final DBOptions options = new DBOptions()
                    .setCreateIfMissing(true)
                    .setCreateMissingColumnFamilies(true);
                 final RocksDB myDb = RocksDB.open(options, "D:\\rocksdb\\myCF", cfDescriptors, columnFamilyHandleList)) {

                try {
                    ReadOptions rOpts = new ReadOptions();
                    System.out.println(new String(myDb.get("1".getBytes()), StandardCharsets.UTF_8));
                    System.out.println(new String(myDb.get("2".getBytes()), StandardCharsets.UTF_8));
                    System.out.println(new String(myDb.get(rOpts, "3".getBytes()), StandardCharsets.UTF_8));

                    System.out.println(new String(myDb.get(columnFamilyHandleList.get(1), "1".getBytes()), StandardCharsets.UTF_8));
                    System.out.println(new String(myDb.get(columnFamilyHandleList.get(1), "2".getBytes()), StandardCharsets.UTF_8));
                    System.out.println(new String(myDb.get(columnFamilyHandleList.get(1), rOpts, "3".getBytes()), StandardCharsets.UTF_8));

                } finally {

                    // NOTE frees the column family handles before freeing the db
                    for (final ColumnFamilyHandle columnFamilyHandle : columnFamilyHandleList) {
                        System.out.println(columnFamilyHandle.getClass());
                        columnFamilyHandle.close();
                    }
                } // frees the db and the db options
            }
        }
    }
}

