package dlt.study.netty4.eventloop;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;

import java.util.concurrent.TimeUnit;

/**
 * Created by denglt on 2016/1/21.
 */
public class FutureLoop {
	public static void main(String[] args) throws Exception {
		DefaultEventExecutorGroup businessHandler = new DefaultEventExecutorGroup(
				10, new DefaultThreadFactory("BusinessHandler"));
		DefaultEventExecutorGroup timeOutHandler = new DefaultEventExecutorGroup(
				2, new DefaultThreadFactory("timeOutHandler"));
		for (int i = 0; i < 100; i++) {
			String taskName = "Tasks-" + i;
			final Future future = businessHandler.submit(new Task(taskName));
			timeOutHandler.schedule(new CancelTask(taskName, future), 10,
					TimeUnit.SECONDS);
		}

		System.in.read();
	}
}

class Task implements Runnable {
	private String name;

	public Task(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(5000);
			System.out.println(name + ":run ok!");
		} catch (Exception e) {

		}
	}
}

class CancelTask implements Runnable {
	private String name;
	private Future future;

	public CancelTask(String name, Future future) {
		this.name = name;
		this.future = future;
	}

	@Override
	public void run() {

		if (future.isCancellable()) {
			future.cancel(false);
			System.out.println(name + ":run cancel!");
		}

	}
}