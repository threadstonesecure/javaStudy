package dlt.study.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class ObjectServer {

	static final boolean SSL = false; // System.getProperty("ssl") != null;
	private int port;

	public ObjectServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {

		// Configure SSL.
		final SslContext sslCtx;
		if (SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContext.newServerContext(ssc.certificate(),
					ssc.privateKey());
		} else {
			sslCtx = null;
		}
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)// (3)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
								@Override
								public void initChannel(SocketChannel ch)
										throws Exception {
									ChannelPipeline p = ch.pipeline();
									if (sslCtx != null) {
										//p.addLast(sslCtx.newHandler(ch.alloc()));
									}
									p.addLast(
											new LoggingHandler(LogLevel.INFO),
											new ObjectDecoder(
													ClassResolvers
															.cacheDisabled(ObjectServer.class
																	.getClassLoader())),
											new ObjectEncoder(),
											new WriteLogHandler(),
											new ObjectServerHandler());
								}
							}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port);
			System.out.println(f.getClass() + ":" + f);
			f.sync();
			System.out.println("server start.");
			System.out.println(f.channel().getClass());
			System.out.println(f.channel());
			Thread.sleep(10000);
			f.channel().writeAndFlush("ssssss");
			System.out.println("发送了:ssssss");
			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to
			// gracefully
			// shut down your server.
			ChannelFuture fClose = f.channel().closeFuture();
			System.out.println(fClose.getClass() + ":" + fClose);
			fClose.sync();
			System.out.println("server stop.");
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new ObjectServer(port).run();
	}
}
