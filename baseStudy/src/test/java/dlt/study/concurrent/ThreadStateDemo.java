package dlt.study.concurrent;

import dlt.study.log4j.Log;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by denglt on 16/9/29.
 */
public class ThreadStateDemo {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LockSupport.park();
                Log.info("unpark");
            }
        });
        thread.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (ThreadStateDemo.class) {
                    try {
                        ThreadStateDemo.class.wait();
                        Log.info("notify");
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        thread2.start();

        Thread.sleep(1000);
        synchronized (ThreadStateDemo.class) {
            ThreadStateDemo.class.notify();
            //Thread.sleep(1000);
            Log.info("sleep");

        }
        LockSupport.unpark(thread);
        System.in.read();
    }

    @Test
    public void isAlive() throws Exception{
        System.out.println(Thread.currentThread().isDaemon()); // false
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LockSupport.park();
/*                try {
                    Thread.sleep(100000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        });
        System.out.println(thread.isAlive()); // false
        thread.start();
        System.out.println(thread.isAlive()); // true
        System.out.println(thread.isDaemon()); // false
       // thread.join();

    }

    @Test
    public void state(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long nanosTimeout = TimeUnit.SECONDS.toNanos(10);
                LockSupport.parkNanos(nanosTimeout);
                LockSupport.park();
            }
        });
        Thread.State state = null;
        while (state != Thread.State.TERMINATED){
           if (state != thread.getState()){
               state = thread.getState();
               System.out.println(state);
            }
            if (state == Thread.State.NEW) thread.start();
            if (state == Thread.State.WAITING) LockSupport.unpark(thread);

        }
    }
}
