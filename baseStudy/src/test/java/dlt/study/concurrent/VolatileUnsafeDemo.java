package dlt.study.concurrent;

import java.lang.reflect.Field;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import sun.misc.Unsafe;
import dlt.utils.UnsafeSupport;

public class VolatileUnsafeDemo {

    private static int finishCount = 0;

    public synchronized static void finish() {
        finishCount++;
    }

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 1000; i++)
            new Thread(new Compute()).start();

        for (int i = 0; i < 1000; i++)
            new Thread(new SyncCompute()).start();

        for (int i = 0; i < 1000; i++)
            new Thread(new AtomCompute()).start();

        while (finishCount < 3000) {

        }
        System.out.println("Compute :  n= " + Compute.n);
        System.out.println("SyncCompute: n= " + SyncCompute.n);
        System.out.println("AtomCompute: n= " + AtomCompute.n);

		/*
         * AtomCompute c = new AtomCompute(); c.setName("denglt");
		 * System.out.println("nOffset= " + AtomCompute.nOffset); c.setN(10);
		 * System.out.println("AtomCompute: n= " + AtomCompute.n);
		 * System.out.println("AtomCompute: name= " + c.name);
		 */
    }

}

class Compute implements Runnable {
    public static volatile int n = 0;

    public void run() {
        for (int i = 0; i < 10; i++)
            try {
                n = n + 1;
                Thread.sleep(3); // 为了使运行结果更随机，延迟3毫秒

            } catch (Exception e) {
            }
        VolatileUnsafeDemo.finish();
    }
}

class SyncCompute implements Runnable {
    public static volatile int n = 0;

    public void run() {
        for (int i = 0; i < 10; i++)
            try {
                synchronized (SyncCompute.class) {
                    n = n + 1;
                }

                Thread.sleep(3); // 为了使运行结果更随机，延迟3毫秒

            } catch (Exception e) {
            }
        VolatileUnsafeDemo.finish();
    }
}

// 使用原子计算(cas)
class AtomCompute implements Runnable {
    public static volatile int n = 0;
    public volatile String name;
    public static final Unsafe unsafe;
    public static final long nOffset;
    public static final long nameOffset;

    static {
        unsafe = UnsafeSupport.getInstance();
        try {
            nOffset = unsafe.staticFieldOffset(AtomCompute.class
                    .getDeclaredField("n"));
            nameOffset = unsafe.objectFieldOffset(AtomCompute.class
                    .getDeclaredField("name"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public void setN(int value) {
        unsafe.putIntVolatile(this.getClass(), nOffset, value);
        // unsafe.putInt(this.getClass(), nOffset, value);
    }

    public void setName(String value) {
        // unsafe.putIntVolatile(this, nOffset, value);
        unsafe.putObjectVolatile(this, nameOffset, value);
        // unsafe.putObject(this, nameOffset, value);
    }

    public void run() {
        for (int i = 0; i < 10; i++)
            try {
                while (!unsafe.compareAndSwapInt(this.getClass(), nOffset, n,
                        n + 1)) {
                }
                Thread.sleep(3); // 为了使运行结果更随机，延迟3毫秒

            } catch (Exception e) {
            }
        VolatileUnsafeDemo.finish();
    }
}