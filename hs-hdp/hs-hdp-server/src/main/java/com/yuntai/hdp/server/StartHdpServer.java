package com.yuntai.hdp.server;

import com.yuntai.hdp.server.updata.UpdataHandlerManager;
import com.yuntai.hdp.server.updata.DiscoveryUpdataHandler;
import com.yuntai.hdp.web.JettyServer;
import com.yuntai.util.spring.PropertyConfigurer;
import com.yuntai.util.spring.SpringContextUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StartHdpServer {
    private static Log log = LogFactory.getLog(StartHdpServer.class);

    public static void main(String[] args) {

/*
        Netty的EventLoopGroup线程interrupt()不成功
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.info("Interrupting threads");
                Set<Thread> runningThreads = Thread.getAllStackTraces().keySet();
                for (Thread th : runningThreads) {
                    if (th != Thread.currentThread() && !th.isDaemon()) {
                        log.info("Interrupting '" + th.getClass() + "' termination");
                        th.interrupt();
                    }
                }
                for (Thread th : runningThreads) {
                    try {
                        if (th != Thread.currentThread() && !th.isDaemon() && th.isInterrupted()) {
                            log.info("Waiting '" + th.getName() + "' termination");
                            th.join();
                        }
                    } catch (InterruptedException ex) {
                        log.info("Shutdown interrupted");
                    }
                }
                log.info("Shutdown finished");
            }
        });
*/

        try {
            String[] paths = new String[]{"spring/*.xml"};
            SpringContextUtils.init(paths);
            UpdataHandlerManager updataHandler = SpringContextUtils.getBean("updataHandlerManager");
            DiscoveryUpdataHandler discoveryUpdataHandler = SpringContextUtils.getBean("discoveryUpdataHandler");
            String ssl = PropertyConfigurer.getProperty("hdp.server.ssl", "1");
            String ip = PropertyConfigurer.getProperty("hdp.server.ip");
            int port = Integer.parseInt(PropertyConfigurer.getProperty("hdp.server.port", "8088"));
            String monitor_ip = PropertyConfigurer.getProperty("hdp.server.monitor.ip");
            String monitor_port = PropertyConfigurer.getProperty("hdp.server.monitor.port", "8089");

            int nThread = Integer.parseInt(PropertyConfigurer.getProperty("hdp.server.businessthreadpoolsize", "200"));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int webPort = Integer.parseInt(PropertyConfigurer.getProperty("hdp.server.webport", "8090"));
                        JettyServer jettyServer = new JettyServer(webPort);
                        log.info(String.format("Jetty is starting on %d !", webPort));
                        jettyServer.start();
                    } catch (Exception ex) {
                        log.error("Jetty start failly!", ex);
                    }
                }
            }).start();
            SpringContextUtils.getAllBeans().forEach((k, v) -> System.out.println(k + "=" + v));
            new HdpServer().updataHandler(updataHandler)
                    .ssl(ssl.equals("1"))
                    .discoveryUpdataHandler(discoveryUpdataHandler)
                    .monitor(monitor_ip, Integer.parseInt(monitor_port))
                    .businessThreadPoolSize(nThread)
                    .run(ip, port);
        } catch (Exception ex) {
            log.error("启动HdpServer发生意外，程序退出：", ex);
        }

    }
}
