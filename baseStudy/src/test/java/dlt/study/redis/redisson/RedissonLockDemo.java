package dlt.study.redis.redisson;

import dlt.study.log4j.Log;
import dlt.study.spring.JUnit4Spring;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

public class RedissonLockDemo extends JUnit4Spring {

    @Resource
    private RedissonClient redissonClient;

    /**
     * // Acquire lock and release it automatically after 10 seconds
     * // if unlock method hasn't been invoked
     * lock.lock(10, TimeUnit.SECONDS);
     * <p>
     * // Wait for 100 seconds and automatically unlock it after 10 seconds
     * boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
     */
    @Test
    public void lock() throws Exception {
        RLock myLock = redissonClient.getLock("myLock");
/*        //redissonClient.getConfig().setLockWatchdogTimeout(60000*2);
        ((Redisson) redissonClient).getConnectionManager().getCfg().setLockWatchdogTimeout(60000 * 10);*/
        System.out.println(redissonClient.getConfig());
        myLock.lock(); // 没有设置超时时间，key(myLock) 的ttl设置为Config.LockWatchdogTimeout,
                       // 并通过Redisson.scheduleExpirationRenewal调度设置不断设置key的ttl
        try {
            Log.info("get lock!!!!");
            Thread.sleep(60000 * 10);
        } finally {
            Log.info("release lock!!!!");
            myLock.unlock();
        }

    }

    @Test
    public void fairLock() throws Exception {

        RLock fairLock = redissonClient.getFairLock("anyLock");
        // 最常见的使用方法
        fairLock.lock();

        // 支持过期解锁功能
        // 10秒钟以后自动解锁
        // 无需调用unlock方法手动解锁
        fairLock.lock(10, TimeUnit.SECONDS);

        // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
        boolean res = fairLock.tryLock(100, 10, TimeUnit.SECONDS);
        fairLock.unlock();

    }

    /**
     * Redisson的RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，
     * 每个RLock对象实例可以来自于不同的Redisson实例。
     * @throws Exception
     */
    @Test
    public void multiLock() throws Exception{
        RLock lock1 = redissonClient.getLock("lock1");
        RLock lock2 = redissonClient.getLock("lock2");
        RLock lock3 = redissonClient.getLock("lock3");

        RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
        // locks: lock1 lock2 lock3
        lock.lock();
        lock.unlock();

        // Acquire lock1, lock2, lock3 and release it automatically after 10 seconds
        // if unlock method hasn't been invoked
        lock.lock(10, TimeUnit.SECONDS); // lock定时长

        // Wait for 100 seconds and automatically unlock it after 10 seconds
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
    }

    /**
     * Redisson的RedissonRedLock对象实现了Redlock介绍的加锁算法。
     * 该对象也可以用来将多个RLock对象关联为一个红锁，每个RLock对象实例可以来自于不同的Redisson实例。
     */
    @Test
    public void redLock(){
        RLock lock1 = redissonClient.getLock("lock1");
        RLock lock2 = redissonClient.getLock("lock2");
        RLock lock3 = redissonClient.getLock("lock3");

        RedissonRedLock lock = new RedissonRedLock(lock1, lock2, lock3);
        // locks: lock1 lock2 lock3
        // 红锁在大部分节点上加锁成功就算成功。
        lock.lock();
        lock.unlock();
    }

    @Test
    public void readWriteLock() throws Exception{
        RReadWriteLock rwlock = redissonClient.getReadWriteLock("anyRWLock");
        // Most familiar locking method
        rwlock.readLock().lock();
        // or
        rwlock.writeLock().lock();

        // Acquire lock and release it automatically after 10 seconds
        // if unlock method not invoked
        rwlock.readLock().lock(10, TimeUnit.SECONDS);
        // or
        rwlock.writeLock().lock(10, TimeUnit.SECONDS);

        // Wait for 100 seconds and automatically unlock it after 10 seconds
        boolean res = rwlock.readLock().tryLock(100, 10, TimeUnit.SECONDS);
        // or
         res = rwlock.writeLock().tryLock(100, 10, TimeUnit.SECONDS);
        //rwlock.unlock();
    }

    @Test
    public void semaphore() throws Exception{
        RSemaphore mySemaphore = redissonClient.getSemaphore("mySemaphore");
        mySemaphore.trySetPermits(10);
        mySemaphore.acquire();
        mySemaphore.release();
    }

    @Test
    public void permitExpirableSemaphore() throws Exception{
        RPermitExpirableSemaphore myPermitExpirableSemaphore = redissonClient.getPermitExpirableSemaphore("myPermitExpirableSemaphore");
        myPermitExpirableSemaphore.trySetPermits(30);
        String permitId = myPermitExpirableSemaphore.acquire();// 在请求许可的时候，会先释放超时的许可（故不需要定时任务清理）
        myPermitExpirableSemaphore.acquire(30,TimeUnit.SECONDS);
        myPermitExpirableSemaphore.release(permitId);
    }

    @Test
    public void countDownLatch() throws Exception{
        RCountDownLatch latch = redissonClient.getCountDownLatch("myCountDownLatch");
        latch.trySetCount(1);
        latch.await();
    }

    public void s(){
        RCountDownLatch latch = redissonClient.getCountDownLatch("anyCountDownLatch");
        latch.countDown();
    }

    @Test
    public void threadId() {
        System.out.println(Thread.currentThread().getId());
    }
}
