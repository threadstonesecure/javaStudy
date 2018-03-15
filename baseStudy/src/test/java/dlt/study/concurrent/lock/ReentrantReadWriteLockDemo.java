package dlt.study.concurrent.lock;

import org.junit.Test;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDemo {

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     *  同线程：先writeLock,后readLock （writeLock 可以降级为 readLock）
     */
    @Test
    public void wrOnSameThread(){
        lock.writeLock().lock();
        lock.readLock().lock();
        System.out.println("同Thread 先获取到write lock后，获取到了read lock"); // 可以同时获取
        lock.readLock().unlock();
        lock.writeLock().unlock();
    }

    /**
     * 多线程
     */
    @Test
    public void wrOnMultiThread() throws Exception{
        lock.writeLock().lock();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.readLock().lock();
                System.out.println("获取到read lock"); //  无法获取 read lock
            }
        });
        thread.start();
        thread.join();
    }
    /**
     *  同线程：先 readLock,后 writeLock (readLock 无法升级为 writeLock)
     */
    @Test
    public void rwOnSameThread(){
        lock.readLock().lock();
        lock.writeLock().lock(); // 无法同获取
        System.out.println("同Thread 先获取到read lock，但无法获取到write lock");  // 同线程都不能，
        lock.readLock().unlock();
        lock.writeLock().unlock();
    }
}
