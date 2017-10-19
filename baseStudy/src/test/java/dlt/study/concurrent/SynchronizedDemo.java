package dlt.study.concurrent;

/**
 * 这个 例子是错误的 
 * 本想通过synchronized来实现Lock和多个Condition的功能，但是失败。
 * @author ltdeng
 *
 */
public class SynchronizedDemo {

	public static void main(String[] args) throws InterruptedException {
		final BlockingQuene quene = new BlockingQuene();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					quene.add();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					quene.get();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		Thread.sleep(3000);
		quene.NofifyGet();
		quene.NotifyAdd();
	}
}

class BlockingQuene {

	private Object addSync = new Object();
	private Object getSync = new Object();

	public BlockingQuene() {

	}
	synchronized public void add() throws InterruptedException {

		addSync.wait();
		System.out.println("add ok");
	}

	synchronized public Object get() throws InterruptedException {
		getSync.wait();
		System.out.println("get ok");
		return null;
	}

	synchronized public void NotifyAdd() {
		addSync.notify();
	}

	synchronized public void NofifyGet() {
		getSync.notify();
	}
}