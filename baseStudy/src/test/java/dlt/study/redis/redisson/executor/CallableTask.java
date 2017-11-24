package dlt.study.redis.redisson.executor;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.RInject;

import java.util.concurrent.Callable;

public class CallableTask implements Callable<Long> {

    @RInject  //Each Redisson node has ready to use RedissonClient which injected to task each time via @RInject annotation.
    private RedissonClient redissonClient;

    @Override
    public Long call() throws Exception {
        RMap<String, Integer> map = redissonClient.getMap("myCallableTask");
        Long result = 0l;
        for (Integer value : map.values()) {
            result += value;
        }
        return result;
    }

}