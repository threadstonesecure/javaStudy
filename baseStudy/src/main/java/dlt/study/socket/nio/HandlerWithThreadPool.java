package dlt.study.socket.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlerWithThreadPool {
	private ExecutorService executorService;// 线程池

	public HandlerWithThreadPool() {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors());
	}

	public void execute(SelectionKey key) {
		
			SocketChannel channel = (SocketChannel) key.channel();
			Handler handler = new Handler(channel);
			executorService.execute(handler);
		
	}

	private class Handler implements Runnable {
		SocketChannel channel;

		Handler(SocketChannel channel) {
			this.channel = channel;
		}

		@Override
		public void run() {
			try {
				ByteBuffer buffer = ByteBuffer.allocate(100);
				channel.read(buffer);
				byte[] data = buffer.array();
				String msg = new String(data).trim();
				System.out.println("服务端收到信息：" + msg);
				msg = "hello by Server.";
				ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
				channel.write(outBuffer);
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
	}
}

/*
	字符集处理：
	ByteBuffer temp = ByteBuffer.allocate(100)
	String msg = Charset.forName("UTF-8").decode(temp).toString();
	sc.write(ByteBuffer.wrap(msg.getBytes(Charset.forName("UTF-8"))));
*/