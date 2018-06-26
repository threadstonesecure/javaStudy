package com.yuntai.hdp.gateway;


import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.AccessHospitalHandler;
import com.yuntai.util.spring.SpringContextUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StartHdpGateway {
    private static Log log = LogFactory.getLog(StartHdpGateway.class);

    public static void main(String[] args) throws Exception {
        log.info("启动HdpGateway服务...");
        try {
            String[] paths = new String[]{"spring/*.xml"};
            SpringContextUtils.init(paths);
            log.info("启动HdpGateway服务启动完成.");
        } catch (Exception ex) {
            log.error("启动HdpGateway服务启动失败", ex);
        }
        Thread.currentThread().join();

    }
}
