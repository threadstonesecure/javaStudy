package com.yuntai.hdp.server;

import com.google.common.util.concurrent.RateLimiter;
import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultKind;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.AccessHospitalHandler;
import com.yuntai.hdp.access.service.UpdataHandler;
import com.yuntai.redisson.RemoteServiceHelper;
import com.yuntai.util.HdpHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.api.RRemoteService;
import org.redisson.api.RedissonClient;
import org.redisson.api.RemoteInvocationOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service("hdpServer2HdpServer")
public class HdpServer2HdpServer implements AccessHospitalHandler, UpdataHandler, ApplicationListener<ContextRefreshedEvent> {

    public static Log log = LogFactory.getLog(HdpServer2HdpServer.class);


    @Resource
    private RedissonClient redissonClient;

    @Resource
    private NodeConfig nodeConfig;

    @Resource(name = "updataHandlerManager")
    private UpdataHandler updataHandler;

    @Resource(name = "accessHospitalHandler")
    private AccessHospitalHandler accessHospitalHandler;

    @Value("${redisson.remoteservice.com.yuntai.hdp.access.service.AccessHospitalHandler.workers:300}")
    private int accessHospitalHandlerWorkers;

    @Value("${redisson.remoteservice.com.yuntai.hdp.access.service.UpdataHandler.workers:300}")
    private int updataHandlerWorkers;

    private void initRedisson() {

        try {
            log.info("HdpServer2HdpServer is creating service and client");
            if (nodeConfig.isToHosByCascade()) {
                // 注册发布 UpdataHander 服务
                RRemoteService remoteServer = RemoteServiceHelper.getRemoteService(redissonClient, "HdpServer2HdpServer");
                remoteServer.register(UpdataHandler.class, updataHandler, updataHandlerWorkers);
                // 创建 AccessHospitalHandler 客户端，
                // remoteAccessHospitalHandler = remoteServer.get(AccessHospitalHandler.class, 60, TimeUnit.SECONDS, Math.max(accessHospitalHandlerWorkers / 60, 3), TimeUnit.SECONDS);
                remoteAccessHospitalHandler = remoteServer.get(AccessHospitalHandler.class,
                        RemoteInvocationOptions.defaults().noAck().expectResultWithin(60, TimeUnit.SECONDS));
                rateLimiter = RateLimiter.create(accessHospitalHandlerWorkers);

            }

            if (nodeConfig.isToYunServiceByCascade()) {
                // 注册发布 AccessHospitalHandler 服务
                RRemoteService remoteServer = RemoteServiceHelper.getRemoteService(redissonClient, "HdpServer2HdpServer");
                remoteServer.register(AccessHospitalHandler.class, accessHospitalHandler, accessHospitalHandlerWorkers);
                // 创建 UpdataHander 客户端
                //remoteUpdataHandler = remoteServer.get(UpdataHandler.class, 60, TimeUnit.SECONDS, Math.max(accessHospitalHandlerWorkers / 60, 3), TimeUnit.SECONDS);

                remoteUpdataHandler = remoteServer.get(UpdataHandler.class,
                        RemoteInvocationOptions.defaults().noAck().expectResultWithin(60, TimeUnit.SECONDS));

                rateLimiter = RateLimiter.create(updataHandlerWorkers);

            }
            isInitOk = true;
            initLatch.countDown();
            log.info("HdpServer2HdpServer have successfully created service and client!");
        } catch (Exception ex) {
            log.error("HdpServer2HdpServer initRedisson server and client", ex);
        }
    }

    @Override
    public ResultPack getHospitalResult(RequestPack request, int timeout) {
        ResultPack hospitalResult;
        try {
            if (!isInitOk) {
                initLatch.await(30, TimeUnit.SECONDS);
            }
            rateLimiter.acquire();
            log.info(String.format("===>转发云服务对接请求到级联HdpServer:%s", request.toKeyString()));
            hospitalResult = remoteAccessHospitalHandler.getHospitalResult(request, timeout);
            log.info(String.format("===>收到级联HdpServer返回结果:%s", hospitalResult.toKeyString()));
        } catch (Exception ex) {
            log.error("hdpServer级联模式，访问远程AccessHospitalHandler接口服务错误", ex);
            hospitalResult = HdpHelper.newResult(request);
            hospitalResult.setKind(ResultKind.ERROE_HDPSERVER2HDPSERVER.getKind());
            hospitalResult.setMsg(ResultKind.ERROE_HDPSERVER2HDPSERVER.getMessage(AccessHospitalHandler.class.toString()));
        }
        return hospitalResult;
    }


    @Override
    public ResultPack process(RequestPack request) {
        ResultPack yunServiceResult;
        try {
            rateLimiter.acquire();
            log.info(String.format("===>转发前置机对接请求到级联HdpServer:%s", request.toKeyString()));
            yunServiceResult = remoteUpdataHandler.process(request);
            log.info(String.format("===>收到级联HdpServer返回结果:%s", yunServiceResult.toKeyString()));
        } catch (Exception ex) {
            log.error("hdpServer级联模式，访问远程UpdataHandler接口服务错误", ex);
            yunServiceResult = HdpHelper.newResult(request);
            yunServiceResult.setKind(ResultKind.ERROE_HDPSERVER2HDPSERVER.getKind());
            yunServiceResult.setMsg(ResultKind.ERROE_HDPSERVER2HDPSERVER.getMessage(UpdataHandler.class.toString()));
        }
        return yunServiceResult;
    }

    @Override
    public boolean checkData(RequestPack data) {
        return false;
    }

    private AccessHospitalHandler remoteAccessHospitalHandler;

    private UpdataHandler remoteUpdataHandler;

    private RateLimiter rateLimiter;

    private CountDownLatch initLatch = new CountDownLatch(1);

    private boolean isInitOk = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initRedisson();
    }

}
