package dlt.study.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dlt.infrastructure.ThreadOut;

/**
 * ReentrantLock运行机制 ： 基于AbstractQueuedSynchronizer类提供的框架实现。
 * AbstractQueuedSynchronizer类机制 ：
 *  1、acquire（int）方法如果没有获取到lock，当前线程进入AbstractQueuedSynchronizer的等候队列，同时线程一直循环重试tryAcquire来获取lock。
 *  2、release(int arg)方法释放lock，并unpark等候激活的进程
 * Condition机制：
 *   1、Condition.wait()方法，park当前线程，并放入Condition的Waiter队列中（同时从AbstractQueuedSynchronizer的等候队列中移出，并释放lock），等待signal
 *   2、Condition.signal()和signalAll（）方法，把Condition的Waiter队列中的成员重新放入AbstractQueuedSynchronizer的等候队列中，等待unpark。
 * @author dlt
 *
 */
public class ReentrantLockDemo {

	public static void main(String[] args) throws InterruptedException {
		Lock lock = new ReentrantLock();
/*		LongLock longLock = new LongLock(lock);
		new Thread(longLock).start();
		new Thread(longLock).start();
		Thread.sleep(3000);*/
		WaitLock waitLock = new WaitLock(lock);
		
		new Thread(waitLock).start();
		new Thread(waitLock).start();
		new Thread(waitLock).start();
		
		Thread.sleep(2000);
		lock.lock();
		
		Thread.sleep(3000);
		lock.unlock();
		waitLock.signalAll(); // lock是一个一个获取的，ReentrantLock为独占lock。
		
		System.out.println("ok");
	}
}

class WaitLock implements Runnable {
	private Lock lock;
	private Condition cond;

	public WaitLock() {
		lock = new ReentrantLock();
		cond = lock.newCondition();
	}

	public WaitLock(Lock lock) {
		this.lock = lock;
		cond = lock.newCondition();
	}

	public void run() {
		ThreadOut.println("请求WaitLock");
		lock.lock();
		ThreadOut.println("获取到WaitLock");
		try {
			ThreadOut.println("被wait。");
			cond.await();
			//cond.await(3, TimeUnit.SECONDS);
			ThreadOut.println("被激活");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			ThreadOut.println("退出WaitLock。。。");
			lock.unlock();
		}
	}

	public void signal() {
		lock.lock();
		try {
			cond.signal();
		} finally {
			lock.unlock();
		}
	}

	public void signalAll() {
		lock.lock();
		try {
			cond.signalAll();
		} finally {
			lock.unlock();
		}
		
	}

}

class LongLock implements Runnable {
	private Lock lock;

	public LongLock() {
		lock = new ReentrantLock();
	}

	public LongLock(Lock lock) {
		this.lock = lock;
	}

	public void run() {
		ThreadOut.println("请求 LongLock。");
		lock.lock();
		ThreadOut.println("获取到LongLock。");
		try {
			Thread.sleep(300000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			ThreadOut.println("退出LongLock。。。");
			lock.unlock();
		}
	}
}