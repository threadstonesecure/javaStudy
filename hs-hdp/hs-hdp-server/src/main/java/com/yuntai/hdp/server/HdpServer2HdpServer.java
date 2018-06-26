package com.yuntai.hdp.server;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultKind;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.AccessHospitalHandler;
import com.yuntai.hdp.access.service.UpdataHandler;
import com.yuntai.util.HdpHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.api.RRemoteService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("hdpServer2HdpServer")
public class HdpServer2HdpServer implements AccessHospitalHandler, UpdataHandler {

    private Log log = LogFactory.getLog(HdpServer2HdpServer.class);


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

    @PostConstruct
    public void init() {

        if (nodeConfig.isToHosByCascade()) {
            // 注册发布 UpdataHander 服务
            RRemoteService remoteServer = redissonClient.getRemoteService("HdpServer2HdpServer");
            remoteServer.register(UpdataHandler.class, updataHandler, updataHandlerWorkers);
            // 创建 AccessHospitalHandler 客户端，
            remoteAccessHospitalHandler = remoteServer.get(AccessHospitalHandler.class, 60, TimeUnit.SECONDS, 3, TimeUnit.SECONDS);
        }

        if (nodeConfig.isToYunServiceByCascade()) {
            // 注册发布 AccessHospitalHandler 服务
            RRemoteService remoteServer = redissonClient.getRemoteService("HdpServer2HdpServer");
            remoteServer.register(AccessHospitalHandler.class, accessHospitalHandler, accessHospitalHandlerWorkers);
            // 创建 UpdataHander 客户端
            remoteUpdataHandler = remoteServer.get(UpdataHandler.class, 60, TimeUnit.SECONDS, 3, TimeUnit.SECONDS);
        }

    }


    @Override
    public ResultPack getHospitalResult(RequestPack request, int timeout) {
        ResultPack hospitalResult;
        try {
            log.info(String.format("===>转发云服务对接请求到级联HdpServer:%s", request.toKeyString()));
            hospitalResult = remoteAccessHospitalHandler.getHospitalResult(request, timeout);
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
            log.info(String.format("===>转发前置机对接请求到级联HdpServer:%s", request.toKeyString()));
            yunServiceResult = remoteUpdataHandler.process(request);
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


}
