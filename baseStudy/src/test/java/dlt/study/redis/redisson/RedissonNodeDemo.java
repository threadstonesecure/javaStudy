package dlt.study.redis.redisson;

import com.google.common.collect.Maps;
import dlt.study.log4j.Log;
import org.redisson.RedissonNode;
import org.redisson.api.RedissonNodeInitializer;
import org.redisson.config.RedissonNodeConfig;

import java.util.Map;


/**
 * 独立节点
 */
public class RedissonNodeDemo {

    public static void main(String[] args) {

        Map<String, Integer> workers =  Maps.newHashMap();
        workers.put("myExecutor",5);
        RedissonNodeConfig config = new RedissonNodeConfig();
        config.setMapReduceWorkers(10)
                .setRedissonNodeInitializer(new RedissonNodeInitializer() {
                    @Override
                    public void onStartup(RedissonNode redissonNode) {
                        //RMap<String, Integer> map = redissonNode.getRedisson().getMap("myMap");
                        Log.info("onStartup -> " + redissonNode);
                    }
                })
                .setExecutorServiceWorkers(workers)
                //.setUseLinuxNativeEpoll(true)
                .setThreads(10)
                .useSingleServer()
                .setAddress("redis://localhost:6379");
        RedissonNode redissonNode = RedissonNode.create(config);
        redissonNode.start();

       // redissonNode.shutdown();
    }
}
