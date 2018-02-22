package dlt.study.rocksdb;


import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.Test;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

import java.nio.charset.StandardCharsets;

/**
 * NOTE Microsoft Windows Users: If you are using the Maven Central compiled artifacts on Microsoft Windows,
 * they were compiled using Microsoft Visual Studio 2015, if you don't have "Microsoft Visual C++ 2015 Redistributable" installed,
 * then you will need to install it from https://www.microsoft.com/en-us/download/details.aspx?id=48145, or otherwise build your own binaries from source code.
 * <p>
 * java.lang.UnsatisfiedLinkError: C:\Users\denglt\AppData\Local\Temp\librocksdbjni 5827299809774087783.dll: Can't find dependent libraries
 */
public class RocksDBDemo {

    private String dbName = "D:\\rocksdb\\mystudy2";

    @Test
    public void openAndCreateDB() throws Exception {
        try (//Statistics stats = new Statistics();
             Options options = new Options().setCreateIfMissing(true)
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
                     .setTableFormatConfig(new PlainTableConfig());
             RocksDB myDb = RocksDB.open(options, dbName)) {
            WriteOptions wOpts = new WriteOptions().setDisableWAL(false).setSync(false);
            myDb.put("1".getBytes(), "denglt".getBytes(StandardCharsets.UTF_8));
            myDb.put("2".getBytes(), "zyy".getBytes(StandardCharsets.UTF_8));
            myDb.put(wOpts, "3".getBytes(), "鼎鼎大名ddddd".getBytes(StandardCharsets.UTF_8));
            myDb.merge("3".getBytes(), "denglt".getBytes(StandardCharsets.UTF_8));
            myDb.merge("4".getBytes(), "hello world".getBytes(StandardCharsets.UTF_8));
        }
    }

    @Test
    public void read() throws Exception {
        try (RocksDB myDb = RocksDB.open(dbName)) {
            ReadOptions rOpts = new ReadOptions();
            System.out.println(new String(myDb.get("1".getBytes()), StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get("2".getBytes()), StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get(rOpts, "3".getBytes()), StandardCharsets.UTF_8));
           //;myDb.multiGet();
            // myDb.close();
        }
    }

    @Test
    public void iterator() throws Exception {
        try (RocksDB myDb = RocksDB.open(dbName)) {
            RocksIterator iterator = myDb.newIterator();
            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                //  iterator.status();
                System.out.println(new String(iterator.key(), StandardCharsets.UTF_8) + "->" + new String(iterator.value(), StandardCharsets.UTF_8));
            }
        }
    }

}

