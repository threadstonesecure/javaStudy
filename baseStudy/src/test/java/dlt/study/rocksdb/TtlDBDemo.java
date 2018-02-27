package dlt.study.rocksdb;

import org.junit.Test;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

import java.nio.charset.StandardCharsets;

public class TtlDBDemo {
    private String dbName = "D:\\rocksdb\\mystudyTTL";

    @Test
    public void openAndCreateDB() throws Exception {
        try (//Statistics stats = new Statistics();
             Options options = new Options().setCreateIfMissing(true).setComparator(BuiltinComparator.BYTEWISE_COMPARATOR)//.useFixedLengthPrefixExtractor()
                     // .setStatistics(stats)
                     .setWriteBufferSize(8 * SizeUnit.KB) // default 4M
                     .setMaxWriteBufferNumber(3) // default 2
                     .setMaxBackgroundCompactions(2) // default 1 ,后台压缩thread 数量
                     // .setCompressionType(CompressionType.SNAPPY_COMPRESSION)
                     .setCompactionStyle(CompactionStyle.UNIVERSAL)
                     .setDisableAutoCompactions(false)
                     .setMemTableConfig(   //缓冲区
                             new HashSkipListMemTableConfig()
                                     .setHeight(4)
                                     .setBranchingFactor(4)
                                     .setBucketCount(2000000)).setAllowConcurrentMemtableWrite(false);
             RocksDB myDb = TtlDB.open(options, dbName, 2, false)) {
            WriteOptions wOpts = new WriteOptions().setDisableWAL(false).setSync(false);
            myDb.put("1".getBytes(), "denglt".getBytes(StandardCharsets.UTF_8));
            myDb.put("2".getBytes(), "zyy".getBytes(StandardCharsets.UTF_8));
            myDb.put(wOpts, "3".getBytes(), "鼎鼎大名ddddd".getBytes(StandardCharsets.UTF_8));
            Thread.sleep(10000);
           // myDb.compactRange();
            System.out.println(new String(myDb.get("1".getBytes()), StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get("2".getBytes()), StandardCharsets.UTF_8));
        }
    }

    @Test
    public void read() throws Exception {
        try (Options options = new Options();
             RocksDB myDb = TtlDB.open(options, dbName, 2, false)) {
            Thread.sleep(10000);
            ReadOptions rOpts = new ReadOptions();
            System.out.println(new String(myDb.get("1".getBytes()), StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get("2".getBytes()), StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get(rOpts, "3".getBytes()), StandardCharsets.UTF_8));

        }
    }
}
