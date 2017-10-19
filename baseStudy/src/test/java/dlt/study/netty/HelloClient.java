package dlt.study.netty;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import dlt.infrastructure.ThreadOut;

public class HelloClient {
	public static void main(String args[]) throws Exception {
	//	for (int i = 0; i < 5; i++) {
			// Client服务启动器
			ClientBootstrap bootstrap = new ClientBootstrap(
					new NioClientSocketChannelFactory(
							Executors.newCachedThreadPool(),
							Executors.newCachedThreadPool()));
			// 设置一个处理服务端消息和各种消息事件的类(HandlerWithThreadPool)
			ChannelHandler[] chs = new ChannelHandler[2];
			// chs[0] = new HelloClientHandler();
			chs[0] = new ClientBufferHandler();
			MyChannelPipelineFactory myCpf = new MyChannelPipelineFactory(chs);
			bootstrap.setPipelineFactory(myCpf);
			// 连接到本地的8000端口的服务端
			bootstrap.connect(new InetSocketAddress("127.0.0.1", 8000));
			ThreadOut.println("客户端启动！");
	//	}

	}

	private static class HelloClientHandler extends SimpleChannelHandler {

		/**
		 * 当绑定到服务端的时候触发，打印"Hello world, I'm client."
		 * 
		 * @alia OneCoder
		 * @author lihzh
		 */
		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			super.channelConnected(ctx, e);
			ThreadOut.println("Hello world, I'm client.");
			// 将字符串，构造成ChannelBuffer，传递给服务端
			String msg = "Hello, I'm client. ";
			ChannelBuffer buffer = ChannelBuffers.buffer(msg.length());
			buffer.writeBytes(msg.getBytes());
			e.getChannel().write(buffer);
			/*
			 * msg = ""; for (int i = 0; i < 1024 + 1; i++) { msg = msg + "a"; }
			 * buffer = ChannelBuffers.buffer(msg.length());
			 * buffer.writeBytes(msg.getBytes()); e.getChannel().write(buffer);
			 */
		}

		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
				throws Exception {
			super.channelClosed(ctx, e);
			System.out.println("channelClosed");
		}

		@Override
		public void channelDisconnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			super.channelDisconnected(ctx, e);
			System.out.println("channelDisconnected");
		}

	}

	private static class ClientBufferHandler extends SimpleChannelHandler {

		@Override
		public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
				throws Exception {
			System.out.println("handleDownstream");
			super.handleDownstream(ctx, e);
		}
		
		@Override
		public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
				throws Exception {
			System.out.println("handleUpstream");
			super.handleUpstream(ctx, e);
		}
		
		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) {
			System.out.println("发送消息 ");
			// 分段发送信息
			//for (int i = 0; i < 6; i++)
				sendMessageByFrame(e);
		}

		private void sendMessageByFrame(ChannelStateEvent e) {
			String msgOne = "Hello, ";
			String msgTwo = "I'm ";
			String msgThree = "client.";
			e.getChannel().write(tranStr2Buffer(msgOne));
			e.getChannel().write(tranStr2Buffer(msgTwo));
			e.getChannel().write(tranStr2Buffer(msgThree));
		}

		private ChannelBuffer tranStr2Buffer(String str) {
			ChannelBuffer buffer = ChannelBuffers.buffer(str.length());
			buffer.writeBytes(str.getBytes());
			return buffer;
		}

	}
}
