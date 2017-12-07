package watchhdp;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import dlt.utils.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by denglt on 2015/12/25.
 */
public class WatchHospital {
    private Log log = LogFactory.getLog(WatchHospital.class);
    private HdpWatchClient hdpWatchClient;

    @Test
    public void watch() throws Exception {
        //120.55.66.16  测试
        //120.26.224.231  预发
        //121.40.182.17  -- 正式 come on, someboay kills it. jack get out.
        String ip = "121.40.182.17";
        int port = 8088;
        String hosId = "_";
        String accessToken = "98608d4679a28b719815ee03f7c404e0";
        hosId = "_" + 226 + "_";
        accessToken = "eece77d113a908a1bed958b3bbcc0c68";
        hdpWatchClient = new HdpWatchClient()
                .remoteAddress(ip, port).hosId(hosId).accessToken(accessToken)
                .reconnectDelay(10).ssl(true)
                .dataHandler(new BadDataHandler())
                .businessThreadPoolSize(2).connect();
        RequestPack requestPack = new RequestPack();
        requestPack.setCmd("$$HDP_COMMAND$$");
        requestPack.setBody("{ hdpcmd : \"allInfo\"}");
        ResultPack resultPack = hdpWatchClient.sendData(requestPack, 30);
        System.out.println(resultPack.getBody());
        System.in.read();
    }

    @Test
    public void bomb() throws Exception {

        String ip = "120.26.224.231";//"120.55.66.16";// "121.40.182.17";
        int port = 8088;
        String hosId = "99999";
        String accessToken = "eece77d113a908a1bed958b3bbcc0c68";
        hdpWatchClient = new HdpWatchClient()
                .remoteAddress(ip, port).hosId(hosId).accessToken(accessToken)
                .reconnectDelay(10).ssl(true)
                .dataHandler(new LogDataHandler())
                .businessThreadPoolSize(2).connect();

        byte[] bs = new byte[1024 * 1024 * 5 - 300];
        String s = new String(bs);
        final RequestPack request = new RequestPack();
        request.setCmd("nocmd");
        request.setBody(s);
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (int i=0; i < 100; i ++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    hdpWatchClient.sendData(request,1);
                }
            });
        }
        System.out.println("ok");
        System.in.read();
    }
}
