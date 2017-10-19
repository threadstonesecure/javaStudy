package dlt.study.concurrent;

import dlt.study.log4j.Log;

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
}
