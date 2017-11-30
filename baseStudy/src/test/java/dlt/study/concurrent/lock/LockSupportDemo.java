package dlt.study.concurrent.lock;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.concurrent.locks.LockSupport;

import org.junit.Test;
import sun.nio.ch.Interruptible;

public class LockSupportDemo {

	@Test
	public void waitObject() throws Exception{
		Object o = new Object();
		synchronized (o){
			o.wait();
		}
	}
	@Test
	public void lockdemo() throws InterruptedException {

		Thread thread = new Thread(new Caller());
		thread.start();
		System.out.println(new Date());
		for (int i = 0; i < 2; i++)
			Thread.sleep(1000);
		System.out.println(new Date());
		LockSupport.unpark(thread);//发出unpark后，LockSupport.park才执行成功！

	}

	@Test
	public void lockdemo2() throws InterruptedException {
        Thread thread = new Thread(new Caller2());
        LockSupport.unpark(thread);  // unpark在Thread.start()后才有意义
        thread.start();
        System.out.println(new Date());
        for (int i = 0; i < 10; i++)
            Thread.sleep(1000);
        System.out.println(new Date());
	}

	@Test
	public void parkBlocker() throws Exception {
		Caller caller =  new Caller();
		Thread thread = new Thread(caller);
		thread.start();
		Thread.sleep(6000);
		synchronized (caller){
			System.out.println("caller.notify");
			Field field = Thread.class.getDeclaredField("parkBlocker");
			field.setAccessible(true);
			Object fBlocker = field.get(thread);
			System.out.println(caller == fBlocker);
			caller.notifyAll();  // 不能唤醒Thread
		}
		Thread.sleep(6000);
		LockSupport.unpark(thread);
	}


	/**
	 * 尝试去中断一个LockSupport.park()，会有响应但不会抛出InterruptedException异常
	 * @throws Exception
     */
	@Test
	public void interruptPark() throws  Exception{
		Caller caller =  new Caller();
		Thread thread = new Thread(caller);
		thread.start();
		Thread.sleep(6000);
		thread.interrupt(); // 这个竟然可以唤醒thread

	}

	@Test
	public void interruptUpPark() throws  Exception{
		Caller caller =  new Caller();
		Thread thread = new Thread(caller);
		thread.start();
		Thread.sleep(6000);
		LockSupport.unpark(thread);
		thread.interrupt();
	}

	@Test
	public void interruptible() throws Exception {
		Caller caller =  new Caller();
		Thread thread = new Thread(caller);
		thread.start();
		blockedOn(thread, new LogInterruptible());
		thread.interrupt();
	}

	// 注册Interrupt处理事件 -- sun.misc.SharedSecrets --
	static void blockedOn(Interruptible intr) { // package-private
		sun.misc.SharedSecrets.getJavaLangAccess().blockedOn(Thread.currentThread(), intr);
	}

	static void blockedOn(Thread thread, Interruptible intr) { // package-private
		sun.misc.SharedSecrets.getJavaLangAccess().blockedOn(thread, intr);
	}

	class  LogInterruptible  implements  Interruptible{
		@Override
		public void interrupt(Thread thread) {
			System.out.println(thread + " interrupt ");
		}
	}
	class Caller implements Runnable {

		@Override
		public void run() {
			LockSupport.park(this);
			if (Thread.currentThread().isInterrupted()){
				System.out.println("Thread is Interrupted");
			}
			System.out.println("Caller run!");

		}
	}

	class Caller2 implements Runnable {

		@Override
		public void run() {
			LockSupport.unpark(Thread.currentThread());
			LockSupport.park(this); // 由于上面的unpark,park不会block thread
			if (Thread.currentThread().isInterrupted()){
				System.out.println("Thread is Interrupted");
			}
			System.out.println("Caller run!");

		}
	}
}
