package dlt.study.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用lock和Condition进行同步控制
 * @author dlt
 *
 */
public class LockSameTime implements SameTime {

	private int iCount = 0;
	private ReentrantLock lock ;
	private Condition ready;

	public LockSameTime(int count) {
		this.iCount = count;
		lock = new ReentrantLock(false);
		ready = lock.newCondition();
	}

	public boolean prepare(Runnable runer) throws InterruptedException {
		lock.lock();
		try {
			iCount--;
			ready.await();
			return true;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void begin() { // 这个不能在方法上synchronized，否则不能启动
		while (iCount > 0) {
			// 这个while必须要，否则当notifyAll时，最后一个thread还没有prepare好。
			// System.out.println(iCount);
		}
		
		lock.lock();
		try{
			System.out.println("Go!");
			ready.signalAll();
		}finally{
			lock.unlock();
		}
	}
}
