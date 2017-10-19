package dlt.study.netty;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import dlt.infrastructure.ThreadOut;

public class HelloServer {
	public static void main(String args[]) {
		// Server服务启动器
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		// 设置一个处理客户端消息和各种消息事件的类(HandlerWithThreadPool)
		ChannelHandler[] chs = new ChannelHandler[2];
		//chs[0] = new HelloServerHandler();
		chs[0] = new ServerBufferHandler();
		MyChannelPipelineFactory myCpf = new MyChannelPipelineFactory(chs);

		bootstrap.setPipelineFactory(myCpf);
		// 开放8000端口供客户端访问。
		bootstrap.bind(new InetSocketAddress(8000));
		ThreadOut.println("服务端启动！");
	}

	private static class HelloServerHandler extends SimpleChannelHandler {

		/**
		 * 当有客户端绑定到服务端的时候触发，打印"Hello world, I'm server."
		 * 
		 * @alia OneCoder
		 * @author lihzh
		 */
		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) {
			ThreadOut.println("channelConnected");
			ThreadOut.println("Hello world, I'm server.");
		}

		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
				throws Exception {
			super.channelClosed(ctx, e);
			ThreadOut.println("channelClosed");
		}

		@Override
		public void channelDisconnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			super.channelDisconnected(ctx, e); 
			ThreadOut.println("channelDisconnected");
		}

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception { // 每次最多接受1024字节
			super.messageReceived(ctx, e); //传递给下一个ChannelHandler处理
			ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
			ThreadOut.println(buffer.toString(Charset.defaultCharset()));
		}
	}

	private static class ServerBufferHandler extends SimpleChannelHandler {

		
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
			System.out.println("messageReceived");
			ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
			// 五位读取
			while (buffer.readableBytes() >= 5) {
				ChannelBuffer tempBuffer = buffer.readBytes(5);
				ThreadOut.println(tempBuffer.toString(Charset.defaultCharset()));
			}
			// 读取剩下的信息
			ThreadOut.println(buffer.toString(Charset.defaultCharset()));
		}

	}
}
