package dlt.study.jcache;

import org.terracotta.ehcachedx.monitor.monitor.MonitorDxService;

/**
 * Created by denglt on 2016/11/1.
 */
public class EhcacheMonitor {
    public static void main(String[] args) throws Exception  {
        MonitorDxService service = new MonitorDxService("127.0.0.1",9889);
        service.start();
    }
}
