package dlt.study.concurrent;

/**
 * Created by denglt on 2016/1/21.
 */
public class ErrorThread {
	public static void main(String[] args) throws  Exception {
        Thread thread = new Thread(new MyRun() );
        thread.start();
        Thread.sleep(5000);
        thread.start();

	}
}

class MyRun implements Runnable {
	@Override
	public void run() {
		try {
            Thread.sleep(3000);
			System.out.println(Thread.currentThread());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}