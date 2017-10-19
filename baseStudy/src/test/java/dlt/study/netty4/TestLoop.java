package dlt.study.netty4;

import io.netty.util.concurrent.DefaultEventExecutorGroup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class TestLoop {
	
	@Test
	public void eventExecutorGroup() throws Exception {
		DefaultEventExecutorGroup businessExecutor = new DefaultEventExecutorGroup(
				4);
		for (int i = 1; i < 10; i++)
			businessExecutor.execute(new MyRunner(i));

		System.out.println(businessExecutor.executorCount());
		System.in.read();
	}
}

class MyRunner implements Runnable {
	private static Log log = LogFactory.getLog(TestLoop.class);
	private String name;

	public MyRunner(int name) {
		this.name = "" + name;
	}

	@Override
	public void run() {

		try {
			Thread.sleep(6000);
			log.info(name);
		} catch (Exception ex) {

		}

	}
}
