package dlt.study.redis.redisson;

import com.google.common.collect.Lists;
import dlt.study.log4j.Log;
import dlt.study.spring.JUnit4Spring;
import org.junit.Test;
import org.redisson.api.*;
import org.redisson.client.RedisClient;
import org.redisson.connection.ConnectionListener;
import org.redisson.connection.RedisClientEntry;

import javax.inject.Inject;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedissonAdditionalFeaturesDemo extends JUnit4Spring {

    @Inject
    RedissonClient redissonClient;

    /**
     * Operations with Redis nodes
     */
    @Test
    public void nodeInfo() {
        NodesGroup<Node> nodesGroup = redissonClient.getNodesGroup();
        nodesGroup.addConnectionListener(new ConnectionListener() {
            @Override
            public void onConnect(InetSocketAddress addr) {
                Log.info("connect ->" + addr);
            }

            @Override
            public void onDisconnect(InetSocketAddress addr) {
                Log.info("disconnect ->" + addr);
            }
        });
        //nodesGroup.pingAll();
        nodesGroup.getNodes().forEach(n -> n.ping());

        nodesGroup.getNodes().forEach(System.out::println);

        nodesGroup.getNodes().forEach(n -> {
            RedisClientEntry redisClientEntry = (RedisClientEntry) n;
            //redisClientEntry.clusterInfo()
            redisClientEntry.info(Node.InfoSection.ALL).forEach((k, v) -> System.out.println(k + " -> " + v));
            RedisClient redisClient = redisClientEntry.getClient();
        });
    }

    /**
     * References to Redisson objects
     * It's possible to use a Redisson object inside another Redisson object in any combination.
     * In this case a special reference object will be used and handled by Redisson.
     */
    @Test
    public void refRedissonObj() {
        RMap<RSet<RList>, RList<RMap>> map = redissonClient.getMap("myMap");
        RSet<RList> set = redissonClient.getSet("mySet");
        RList<RMap> list = redissonClient.getList("myList");

        map.put(set, list);
        // 在特殊引用对象的帮助下，我们甚至可以构建一个循环引用，这是通过普通序列化方式实现不了的。
        set.add(list);
        list.add(map);
    }

    @Test
    public void refRedissonObj2() {
        RMap<String, List<String>> map = redissonClient.getMap("myMapRefRedissonObj");
        RList<String> lists = redissonClient.getList("myListRefRedissonObj");
        map.put("1", Lists.newArrayList("hello","world"));
        map.put("myListRefRedissonObj",lists);
        lists.add("hello2");
        lists.add("world");
    }

    /**
     *  Execution batches of commands
     */
    @Test
    public void batch() throws Exception{
        RBatch batch = redissonClient.createBatch();
        batch.timeout(10, TimeUnit.SECONDS); // 设置Response timeout
        // Retry interval for each attempt to send Redis commands batch
        batch.retryInterval(2, TimeUnit.SECONDS);
       // Attempts amount to re-send Redis commands batch if it hasn't been sent already
        batch.retryAttempts(4);

        RFuture<Boolean> test = batch.getMap("test").fastPutAsync("1", "2");
        batch.getMap("test").fastPutAsync("2", "3");
        batch.getMap("test").putAsync("2", "5");
        batch.getAtomicLong("counter").incrementAndGetAsync();
        batch.getAtomicLong("counter").incrementAndGetAsync();
/**
        发送指令并等待执行，但是跳过结果
        batch.executeSkipResult(); // executeSkipResultAsync
        System.out.println(test.get());  // 一直等待，无结果返回
 */
        List<?> executes = batch.execute(); // batch.executeAsync()
        System.out.println(test.get());
        executes.forEach( v -> System.out.println(v.getClass() + " -> "+ v));
    }

}
