package dlt.study.redis.lock;

import dlt.study.spring.JUnit4Spring;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.redisson.api.RedissonClient;
import org.springframework.integration.redis.util.RedisLockRegistry;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

import static dlt.utils.CommonUtils.log;
/**
 * Created by denglt on 16/9/19.
 */
public class DistributedLock extends JUnit4Spring {


    final String lockKey = "hdpSeqno:1234567890";
    @Resource
    private RedisLockRegistry redisLockRegistry;

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void lock() throws Exception {

        Lock lock = redisLockRegistry.obtain(lockKey);
        if (lock.tryLock()) {
            System.out.println(String.format("获取到[%s]锁!", lockKey));
            Thread.sleep(10000);
            lock.unlock();   //java.lang.IllegalStateException: Lock was released due to expiration
        } else {
            System.out.println(String.format("获取[%s]锁失败!", lockKey));
        }
    }

    @Test
    public void lock2() throws Exception {  // 开两个程序测试
        Lock lock = redisLockRegistry.obtain(lockKey);
        try {
            lock.lock();
            System.out.println(String.format("获取到[%s]锁!", lockKey));
            Thread.sleep(30000);
            System.in.read();
        } finally {
            lock.unlock();
        }

    }

    /**
     * 通过同时启用两个JVM运行观察,同一个JVM里如果一个thread获取到redis lock后,其他的thread不会一直向redis发送指令(这说明维护了一个app级的同步lock来提高性能)
     * 而另一个APP会一直向redis发送指令企图获取lock
     * @throws Exception
     */
    @Test
    public void multiThreadlock() throws Exception {  // 多线程测试lock,观察redis server端的连接(注意redis pool参数maxTotal的设置)
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 20; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Lock lock = redisLockRegistry.obtain(lockKey);
                    try {
                        log.info("开始获取lock");
                        lock.lock();
                        log.info(String.format("获取到[%s]锁!", lockKey));
                        Thread.sleep(6000);
                    } catch (Exception ex) {
                        log.error(ex);
                    } finally {
                        log.info("释放lock");
                        lock.unlock();
                    }
                }
            });
        }
        executor.shutdown();
        log.info("关闭thread pool!");
        System.in.read();
    }


    @Test
    public void multiThreadAndMultiKeylock() throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(20);
        for ( int i = 0; i < 20; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    String lockKey = Thread.currentThread().getName();
                    Lock lock = redisLockRegistry.obtain(lockKey);
                    try {
                        log.info("开始获取lock");
                        lock.lock();
                        log.info(String.format("获取到[%s]锁!", lockKey));
                        Thread.sleep(60000);
                    } catch (Exception ex) {
                        log.error(ex);
                    } finally {
                        log.info("释放lock");
                        lock.unlock();
                    }
                }
            });
        }
        executor.shutdown();
        log.info("关闭thread pool!");
        System.in.read();
    }
}
