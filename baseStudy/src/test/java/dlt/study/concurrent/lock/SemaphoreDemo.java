package dlt.study.concurrent.lock;

import dlt.study.log4j.Log;
import org.junit.Test;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    @Test
    public  void info() throws Exception{
        Semaphore semaphore = new Semaphore(3,true);
        Log.info(semaphore.availablePermits());
        semaphore.acquire();
        semaphore.acquire();
        Log.info(semaphore.availablePermits());
        semaphore.release();
        Log.info(semaphore.availablePermits());
    }

    @Test
    public void zero() throws Exception{
        Semaphore semaphore = new Semaphore(0);
        semaphore.acquire();
    }

    @Test
    public void zero2() throws Exception{
        Semaphore semaphore = new Semaphore(0);
        semaphore.release();
        semaphore.acquire();
    }
}
