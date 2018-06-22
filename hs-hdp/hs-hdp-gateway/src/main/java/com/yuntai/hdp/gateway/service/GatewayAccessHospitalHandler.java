package com.yuntai.hdp.gateway.service;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.AccessHospitalHandler;
import com.yuntai.hdp.gateway.StartHdpGateway;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service("accessHospitalHandler")
public class GatewayAccessHospitalHandler implements AccessHospitalHandler {
    private  Log logger = LogFactory.getLog(StartHdpGateway.class);


    @Override
    public ResultPack getHospitalResult(RequestPack request, int timeout) {
        // TODO: 2018/6/20 转发到hdpServer
        logger.info("GatewayAccessHospitalHandler.getHospitalResult");
        return null;
    }
}
