package com.yuntai.hdp.gateway.service;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultKind;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.AccessHospitalHandler;
import com.yuntai.hdp.access.service.DowndataHandler;
import com.yuntai.util.spring.SpringContextUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("accessHospitalImpl")
public class AccessHospitalImpl implements AccessHospitalHandler, DowndataHandler {
    private static Log log = LogFactory.getLog(AccessHospitalImpl.class);

    @Value("${access.hdpserver.mode:dubbo}")
    private String toHdpServerMode;

    @EventListener(value = ContextRefreshedEvent.class)
    public void initToHdpServerMode() {
        switch (toHdpServerMode) {
            case "dubbo":
                accessHospitalHandler = SpringContextUtils.getBean("dubbo.HdpServer.AccessHospitalHandler");
                downdataHandler = SpringContextUtils.getBean("dubbo.HdpServer.DowndataHandler");
                break;
            case "redis":
                accessHospitalHandler = SpringContextUtils.getBean("redis.HdpServer.AccessHospitalHandler");
                downdataHandler = SpringContextUtils.getBean("redis.HdpServer.DowndataHandler");
                break;
            default:
                throw new IllegalArgumentException("HdpGateWay访问HdpServer参数{access.hdpserver.mode}配置错误！");
        }
        log.info("HdpGateway访问HdpServer的方式 -> " + toHdpServerMode);
    }

    @Override
    public ResultPack getHospitalResult(RequestPack request, int timeout) {

        ResultPack hospitalResult;
        try {
            request.setHdpSeqno(UUID.randomUUID().toString().replace("-", ""));
            log.info(String.format("===>转发云服务对接请求到HdpServer:%s", request.toKeyString()));
            hospitalResult = accessHospitalHandler.getHospitalResult(request, timeout);
            log.info(String.format("===>收到HdpServer返回结果:%s",hospitalResult.toKeyString()));
        } catch (Exception ex) {
            log.error("访问HdpServer.AccessHospitalHandler接口服务错误", ex);
            hospitalResult = newResult(request);
            hospitalResult.setKind(ResultKind.ERROE_HDPGATEWAY2HDPSERVER.getKind());
            hospitalResult.setMsg(ResultKind.ERROE_HDPGATEWAY2HDPSERVER.getMessage(AccessHospitalHandler.class.toString()));
        }
        return hospitalResult;
    }

    @Override
    public Boolean downData(RequestPack request) {
        try {
            log.info(String.format("===>转发云服务对接请求到HdpServer:%s", request.toKeyString()));
            return downdataHandler.downData(request);
        } catch (Exception ex) {
            log.error("访问HdpServer.DowndataHandler接口服务错误", ex);
        }
        return false;
    }

    private AccessHospitalHandler accessHospitalHandler;

    private DowndataHandler downdataHandler;

    private ResultPack newResult(RequestPack request) {
        ResultPack resultPack = new ResultPack();
        resultPack.setSeqno(request.getSeqno());
        resultPack.setHosId(request.getHosId());
        resultPack.setCmd(request.getCmd());
        resultPack.setHdpSeqno(request.getHdpSeqno());
        resultPack.setCallMode(request.getCallMode());
        resultPack.setReturnTime(System.currentTimeMillis());
        return resultPack;
    }
}
