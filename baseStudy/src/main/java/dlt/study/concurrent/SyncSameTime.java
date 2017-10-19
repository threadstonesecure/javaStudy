package dlt.study.concurrent;



public class SyncSameTime implements SameTime {

	private int iCount = 0;

	public SyncSameTime(int count) {
		this.iCount = count;
	}

	synchronized public boolean prepare(Runnable runer)
			throws InterruptedException {
		iCount--;
		//System.out.println(iCount);
		wait();
		return true;
	}

	@Override
	public void begin() { //这个不能在方法上synchronized，否则不能启动
		while (iCount > 0) {
			//这个while必须要，否则当notifyAll时，最后一个thread还没有prepare好。
			// System.out.println(iCount);
		}
		System.out.println("Go!");
		synchronized (this) {
			notifyAll();
		}
	}
}
