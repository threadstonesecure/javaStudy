package com.yuntai.hdp.gateway;

import com.yuntai.util.spring.SpringContextUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StartHdpGateway {
    private static Log log = LogFactory.getLog(StartHdpGateway.class);

    public static void main(String[] args) {
        log.info("正在启动HdpGateway服务...");
        try {
            String[] paths = new String[]{"spring/*.xml"};
            SpringContextUtils.init(paths);
            log.info("启动HdpGateway服务启动完成.");
            Thread.currentThread().join();
        } catch (Exception ex) {
            log.error("启动HdpGateway服务启动失败", ex);
        }


    }
}
