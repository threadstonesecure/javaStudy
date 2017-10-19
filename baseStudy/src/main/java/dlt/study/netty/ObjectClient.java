package dlt.study.netty;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.logging.LoggingHandler;

public class ObjectClient {
	public static void main(String args[]) throws Exception {
		// Client服务启动器
		final ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		// 设置一个处理服务端消息和各种消息事件的类(HandlerWithThreadPool)
		List<ChannelHandler> chs = new ArrayList<ChannelHandler>();
		chs.add(new LoggingHandler());

/*	这个需要	chs.add(new ObjectDecoder(ClassResolvers
				.cacheDisabled(ObjectServer.class.getClassLoader())));*/
		chs.add(new ObjectEncoder());

		// chs.add(new DecoderAndEncoderHandler());

		chs.add(new ObjectClientHandler());

		MyChannelPipelineFactory myCpf = new MyChannelPipelineFactory(
				chs.toArray(new ChannelHandler[0]));
		bootstrap.setPipelineFactory(myCpf);
		// 连接到本地的8000端口的服务端
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				"127.0.0.1", 8000));
		future.addListener(new StartListener());
		future.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
		System.out.println("=======================");
/*		concurrency.await();
		if (concurrency.isSuccess()) {
			System.out.println("客户端启动成功！" + concurrency + ":" + concurrency.getClass());
			System.out.println("Channel:" + concurrency.getChannel() + ":"
					+ concurrency.getChannel().getClass());
			Channel channel = concurrency.getChannel();
			ChannelFuture f1 =  channel.write("Hello,我来了 !");
			ChannelFuture f2 =  channel.write(new Date());
			System.out.println(f1);
			System.out.println(f2);
		}else{
			System.out.println("客户端启动失败！"+concurrency.getCause().getMessage());
			
		} */

		/*
		 * for (int i = 0; i < 1000; i++) { //并发测试 // 连接到本地的8000端口的服务端 Thread t
		 * = new Thread(new Runnable() {
		 * 
		 * @Override public void run() { bootstrap.connect(new
		 * InetSocketAddress("127.0.0.1", 8000)); try { Thread.sleep(300000); }
		 * catch (InterruptedException e) { e.printStackTrace(); } } });
		 * t.start(); }
		 */
	}

	
	private static class StartListener implements ChannelFutureListener{
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			if (future.isSuccess()) {
				System.out.println("客户端启动成功！" + future + ":" + future.getClass());
				System.out.println("Channel:" + future.getChannel() + ":"
						+ future.getChannel().getClass());
				Channel channel = future.getChannel();
				ChannelFuture f1 =  channel.write("Hello,我来了 !");
				//f1.await();
				ChannelFuture f2 =  channel.write(new Date());
				//f2.await();
				System.out.println(f1);
				System.out.println(f2);
			}else{
				System.out.println("客户端启动失败！"+future.getCause().getMessage());
			}
		}
	}
}
