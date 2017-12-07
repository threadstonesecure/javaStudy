package com.yuntai.hdp.future;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * FutureResult 未来结果即等待结果
 * Created by denglt on 2015/12/14.
 */
public class FutureResult<T> {

    private volatile T result;
    private Thread thread;
    private String unique;
    private FutureResultManager futureResultManager;

    public FutureResult(String unique, FutureResultManager futureResultManager) {
        this.unique = unique;
        this.futureResultManager = futureResultManager;
    }

    /**
     * 获取等待结果，thread进行入等待
     *
     * @param timeout 超时时间  单位秒
     * @return
     */
    public T get(long timeout) {
        thread = Thread.currentThread();
        long nanosTimeout = TimeUnit.SECONDS.toNanos(timeout);
        final long deadline = System.nanoTime() + nanosTimeout;
        while (nanosTimeout > 0 && result == null) {
            if (nanosTimeout >= 1000L)
                LockSupport.parkNanos(this, nanosTimeout);
            nanosTimeout = deadline - System.nanoTime();
        }
        futureResultManager.remove(unique);
        return result;
    }

    /**
     * 设置结果，唤醒thread
     *
     * @param pack
     */
    public void setResult(T pack) {
        result = pack;
        LockSupport.unpark(thread);
    }

}
