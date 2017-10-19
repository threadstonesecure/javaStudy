package dlt.study.zookeeper;

import org.apache.zookeeper.*;
import org.junit.Test;

/**
 * Created by denglt on 2016/5/16.
 */
public class ZkAuthDemo {
    private static ZooKeeper zk;
    static {
        String address = "127.0.0.1:2181";
        try {
            zk = new ZooKeeper(address, 60000, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    System.out.println("已经触发了" + event + "事件！");
                }
            });
        } catch (Exception ex) {

        }
    }

    @Test
    public void createSub() throws Exception {
        // 创建一个与服务器的连接

        zk.addAuthInfo("digest","test:test".getBytes());
        zk.create("/mydubbo/child1", "testRootPathData".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);


        zk.close();
        System.in.read();
    }
}
