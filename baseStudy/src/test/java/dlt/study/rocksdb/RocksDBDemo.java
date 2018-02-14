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

    private String dbName = "D:\\rocksdb\\mystudy";

    @Test
    public void openAndCreateDB() throws Exception {
        try (Statistics stats = new Statistics();
             Options options = new Options().setCreateIfMissing(true)
                     .setStatistics(stats)
                     .setWriteBufferSize(8 * SizeUnit.KB) // default 4M
                     .setMaxWriteBufferNumber(3) // default 2
             ;
             RocksDB myDb = RocksDB.open(options, dbName)) {
            WriteOptions wOpts = new WriteOptions();
            myDb.put("1".getBytes(), "denglt".getBytes(StandardCharsets.UTF_8));
            myDb.put("2".getBytes(), "zyy".getBytes(StandardCharsets.UTF_8));
            myDb.put(wOpts, "3".getBytes(), "鼎鼎大名ddddd".getBytes(StandardCharsets.UTF_8));
        }
    }

    @Test
    public void read() throws Exception {

        try (RocksDB myDb = RocksDB.open(dbName)) {
            ReadOptions rOpts = new ReadOptions();
            System.out.println(new String(myDb.get("1".getBytes()), StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get("2".getBytes()), StandardCharsets.UTF_8));
            System.out.println(new String(myDb.get(rOpts, "3".getBytes()), StandardCharsets.UTF_8));
            // myDb.close();
        }
    }
}

