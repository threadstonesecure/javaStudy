package com.yuntai;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.AccessHospitalHandler;
import com.yuntai.util.spring.SpringContextUtils;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.junit.Before;
import org.junit.Test;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ContextConfiguration(locations = {"/spring/prop.xml", "/spring/redisrpc2HdpServer.xml"})
public class RequestHdpByRedissionDemo extends AbstractJUnit4SpringContextTests {

    @Autowired
    private RedissonClient redissonClient;

    // "redis.HdpServer.AccessHospitalHandler"

    // @Autowired
    private AccessHospitalHandler accessHospitalHandler;

    @Before
    public void init() {
        accessHospitalHandler = SpringContextUtils.getBean("redis.HdpServer.AccessHospitalHandler");
    }

    @Test
    public void requestOne() {

        RequestPack requestPack = new RequestPack();
        String[] hostIds = {"100009", "100009", "100009"};
        Random r = new Random();
        int i = r.nextInt(3);
        //requestPack.setHosId(hostIds[i]);
        requestPack.setHosId("999999-test");
        requestPack.setCmd("业务代码:" + 10000);//System.currentTimeMillis());
        requestPack.setBody("ddddd");
        ResultPack result = accessHospitalHandler.getHospitalResult(
                requestPack, 30);
        System.out.println(result);
    }

    @Test
    public void requests() throws Exception {
        accessHospitalHandler = SpringContextUtils.getBean("redis.HdpServer.AccessHospitalHandler");
        ExecutorService tpe = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 50; i++) {
            tpe.execute(new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    RequestPack requestPack = new RequestPack();
                    String[] hostIds = {"100009", "100009", "100009"};
                    Random r = new Random();
                    int i = r.nextInt(3);
                    //requestPack.setHosId(hostIds[i]);
                    requestPack.setHosId("999999-test");
                    requestPack.setCmd("业务代码:" + 10000);//System.currentTimeMillis());
                    requestPack.setBody("ddddd");
                    ResultPack result = accessHospitalHandler.getHospitalResult(
                            requestPack, 30);
                    long endTime = System.currentTimeMillis();
                    if (!result.getCmd().equals(requestPack.getCmd())) {
                        System.out.println("fuck !!!!!!!!!");
                    }
                    logger.info("time[" + (endTime - startTime) + "] -> " + result);
                }

            });
        }
        System.in.read();
    }

    @Test
    public void testBlockQueue() throws Exception {
        RBlockingQueue<String> myBlockQueue = redissonClient.getBlockingQueue("myBlockQueue");
        RFuture<String> stringRFuture = myBlockQueue.takeAsync();
/*        stringRFuture.addListener((f) -> {
            String msg = f.getNow();
            logger.info("获取到消息 ->" + msg);
            RBlockingQueue<String> myBlockQueue = redissonClient.getBlockingQueue("myBlockQueue");
            myBlockQueue.takeAsync().addListener(this);
        });*/

        stringRFuture.addListener(new FutureListener<String>() {
            @Override
            public void operationComplete(Future<String> future) throws Exception {
                String msg = future.getNow(); // 这个竟然在IO线程(即netty的线程)中工作
                logger.info("获取到消息 ->" + msg);
                RBlockingQueue<String> myBlockQueue = redissonClient.getBlockingQueue("myBlockQueue");
                myBlockQueue.takeAsync().addListener(this);
            }
        });
        Thread.currentThread().join();
    }

    @Test
    public void putData() throws Exception{
        RBlockingQueue<String> myBlockQueue = redissonClient.getBlockingQueue("myBlockQueue");
        for (int i = 0; i < 10000; i++) {
            myBlockQueue.put("msg ->" + i);
        }
    }
}
