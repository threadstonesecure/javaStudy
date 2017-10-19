package dlt.study.nio.asynio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
/**
 * 异步io的实现方法
 *   1、通过java.util.concurrent.Future类的对象表示异步操作的结果
 *   2、java.nio.channels.CompletionHandler接口的实现对象作为异步操作完成时的回调方法
 * @author dlt
 *
 */
public class AsynIoDemo {

	@Test
	public void asyncWrite() throws IOException, ExecutionException,
			InterruptedException {
		Path path = Paths.get("d:", "temp.txt");
		// System.out.println(path);
		AsynchronousFileChannel channel = AsynchronousFileChannel.open(path,
				StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		ByteBuffer buffer = ByteBuffer.allocate(32 * 1024 * 1024);
		Future<Integer> result = channel.write(buffer, 0);
		// 其他操作
		System.out.println("其他操作");
		Integer len = result.get();
		System.out.println("写入："+len);
	}

	@Test
	public void asyncWrite2() throws IOException, ExecutionException,
			InterruptedException {
		Path path = Paths.get("d:", "temp.txt");
		// System.out.println(path);
		AsynchronousFileChannel channel = AsynchronousFileChannel.open(path,
				StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		ByteBuffer buffer = ByteBuffer.allocate(32 * 1024 * 1024);
		channel.write(buffer, 0, null, new CompletionHandler<Integer, Void>() {
			@Override
			public void completed(Integer result, Void attachment) {
				// TODO Auto-generated method stub
				System.out.println("文件写入完成。");
				System.out.println("写入："+result);
			}

			@Override
			public void failed(Throwable exc, Void attachment) {
				// TODO Auto-generated method stub
				//失败处理
			}
		});
		// 其他操作
		System.out.println("其他操作");
	}
}
