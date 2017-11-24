package dlt.study.redis.redisson.executor;


import dlt.study.spring.JUnit4Spring;
import org.junit.Before;
import org.junit.Test;
import org.redisson.api.*;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * Redisson distributed Executor service for Java implements java.util.concurrent.ExecutorService
 * and allows to run java.util.concurrent.Callable and java.lang.Runnable tasks on different Redisson nodes.
 * Tasks have an access to Redisson instance, can do any manipulations with Redis data
 * and execute distributed computations in fast and efficient way.
 */
public class RedissoExecutorServiceDemo extends JUnit4Spring {

    @Inject
    RedissonClient redissonClient;

    @Inject
    RScheduledExecutorService executorService;

    @Before
    public  void init(){
        RMap<String, Integer> map = redissonClient.getMap("myCallableTask");
        map.put("1",1);
        map.put("2",2);
        map.put("3",3);
        map.put("4",4);
        map.put("5",5);
    }
    @Test
    public void submit() throws Exception {
        RExecutorService executorService = redissonClient.getExecutorService("myExecutor"); // 需要在Redisson Node上配置名称为myExecutor的worker数据
        RExecutorFuture<Long> future = executorService.submit(new CallableTask());
        Long result = future.get();
        System.out.println(result); // 15
        future.cancel(true);
        // 或
        String taskId = future.getTaskId();
        executorService.cancelTask(taskId);
    }

    @Test
    public void schedule() throws Exception{
        RScheduledExecutorService executorService = redissonClient.getExecutorService("myExecutor");
        RScheduledFuture<Long> future = executorService.schedule(new CallableTask(), 10, TimeUnit.MINUTES);
        Long result = future.get();
        future.cancel(true);
       // 或
        String taskId = future.getTaskId();
        executorService.cancelScheduledTask(taskId);

    }

    @Test
    public void beanSubmit() throws Exception{
        RExecutorFuture<Long> future = executorService.submit(new CallableTask());
        Long result = future.get();
        System.out.println(result); // 15
    }
}
