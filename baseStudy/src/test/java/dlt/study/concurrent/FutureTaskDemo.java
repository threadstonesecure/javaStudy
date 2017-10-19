package dlt.study.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FutureTaskDemo {

	public static void main(String[] args) {

		MyCall myCall = new MyCall();
		final FutureTask<Integer> task = new FutureTask<Integer>(myCall);
		new Thread(task).start();

		// 启动Thread获取结果，一直等待
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Integer r = task.get(); // 会抛出InterruptedException（中断错误）
					System.out.println("任务运算结果 ：" + r);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
		// 启动Thread获取结果，等待2s
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Integer r = task.get(2, TimeUnit.SECONDS);
					System.out.println("任务运算结果 ：" + r);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
		System.out.println("等候运算结果。。。");
		
		if (task.cancel(true)) {  //中断任务 
			System.out.println("任务被cancel。");
		}

	}

}

class MyCall implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {
		System.out.println("run MyClass");
		int n = 0;
		for (int i = 1; i <= 100000; i++) {
			// Thread.sleep(10);
			if (Thread.interrupted()) {   //响应中断
				throw new InterruptedException();
			}
			n = n + i;
		}
		System.out.println("计算完毕：" + n);
		return n;
	}
}