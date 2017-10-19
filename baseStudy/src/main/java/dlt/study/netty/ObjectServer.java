package dlt.study.netty;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.logging.LoggingHandler;

import dlt.infrastructure.ThreadOut;


public class ObjectServer {
	public static void main(String args[]) {
		// Server服务启动器
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		// 设置一个处理客户端消息和各种消息事件的类(HandlerWithThreadPool)
		List<ChannelHandler> chs = new ArrayList<ChannelHandler>();
		chs.add(new LoggingHandler());
		//netty自带编码、解码
/*		chs.add(new ObjectDecoder(ClassResolvers
				.cacheDisabled(ObjectServer.class.getClassLoader()))); */   //这用使用有问题，因为ObjectDecoder是不能多个Channnel共享的，每个Channel应该有自己的独占的实例
		chs.add(new ObjectEncoder());


		//编码、解码二合一
	//	chs.add(new DecoderAndEncoderHandler());
		
		chs.add(new ObjectServerHandler()); // 业务处理的ChannelHandler必寻放在编码、解码的后面
		chs.add(new ReadBufferHandler());
		chs.add(new ReadBufferHandler());
		MyChannelPipelineFactory myCpf = new MyChannelPipelineFactory(
				chs.toArray(new ChannelHandler[0]));
		
		bootstrap.setPipelineFactory(myCpf);
		// 开放8000端口供客户端访问。
		Channel channel = bootstrap.bind(new InetSocketAddress(8000));
		
		ThreadOut.println("服务端启动！"+ channel + ":" + channel.getClass());
	}

}
