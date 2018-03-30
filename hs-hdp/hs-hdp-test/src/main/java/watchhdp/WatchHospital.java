package watchhdp;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.rocksdb.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by denglt on 2015/12/25.
 */
public class WatchHospital {
    private Log log = LogFactory.getLog(WatchHospital.class);
    private HdpWatchClient hdpWatchClient;

    @Test
    public void bad() throws Exception {
        //120.55.66.16  测试
        //120.26.224.231  预发
        //121.40.182.17  -- 正式 come on, someboay kills it. jack get out.
        String ip = "121.40.182.17";
        int port = 8088;
        String hosId = "_";
        String accessToken = "98608d4679a28b719815ee03f7c404e0";
        //  hosId = "_" + 226 + "_";
        //  accessToken = "eece77d113a908a1bed958b3bbcc0c68";
        hdpWatchClient = new HdpWatchClient()
                .remoteAddress(ip, port).hosId(hosId).accessToken(accessToken)
                .reconnectDelay(10).ssl(true)
                //.dataHandler(new BadDataHandler())
                //.dataHandler(new LookupDataHandler())
                .dataHandler(new StatsDataHandler("/tmp/hdpStatDB"))
                .businessThreadPoolSize(2).connect();
        RequestPack requestPack = new RequestPack();
        requestPack.setCmd("$$HDP_COMMAND$$");
        requestPack.setBody("{ hdpcmd : \"allInfo\"}");
        ResultPack resultPack = hdpWatchClient.sendData(requestPack, 30);
        System.out.println(resultPack.getBody());

        Thread.currentThread().join();
    }

    @Test
    public void bomb() throws Exception {

        String ip = "120.55.66.16";//"120.55.66.16";// "121.40.182.17";
        int port = 8088;
        String hosId = "99999";
        String accessToken = "c5fbe05b6aaf6cf3f994f8c234fe048d";
        hdpWatchClient = new HdpWatchClient()
                .remoteAddress(ip, port).hosId(hosId).accessToken(accessToken)
                .reconnectDelay(10).ssl(true)
                .dataHandler(new LookupDataHandler())
                .businessThreadPoolSize(2).connect();

        byte[] bs = new byte[1024 * 1024 * 10 - 300];
        String s = new String(bs);
        final RequestPack request = new RequestPack();
        request.setCmd("nocmd");
        request.setBody(s);
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 100; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    log.info("one bomb begin");
                    hdpWatchClient.bombData(request);
                    log.info("one bomb end !");
                }
            });
        }
        System.out.println("ok");
        System.in.read();
    }


    @Test
    public void iteratorStatsDB() throws Exception {
        try (RocksDB myDb = RocksDB.openReadOnly("/tmp/hdpStatDB")) {
            RocksIterator iterator = myDb.newIterator();
            int i = 0;
            int totalRequestNum = 0;
            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {

                String key = new String(iterator.key(), StandardCharsets.UTF_8);
                Integer value = deserialize(iterator.value());
                if (key.contains("2018032210")) {
                    i++;
                    totalRequestNum = totalRequestNum + value;
                    System.out.println(key + " : " + value);
                }
            }
            System.out.println("一共有记录 ：" + i);
            System.out.println("总请求数：" + totalRequestNum);
            System.out.println("平均每分钟请求数" + totalRequestNum / 60);
        }
    }


    private Integer deserialize(byte[] data) {
        if (data == null) {
            return null;
        } else if (data.length != 4) {
            throw new RuntimeException("Size of data received by IntegerDeserializer is not 4");
        } else {
            int value = 0;
            byte[] arr$ = data;
            int len$ = data.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                byte b = arr$[i$];
                value <<= 8;
                value |= b & 255;
            }
            return value;
        }
    }
}
