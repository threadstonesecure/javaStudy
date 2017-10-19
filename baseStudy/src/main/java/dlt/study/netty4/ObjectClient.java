package dlt.study.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import dlt.domain.model.User;

public class ObjectClient {
	static final boolean SSL = false; // System.getProperty("ssl") != null;

	public static void main(String[] args) throws Exception {
		// Configure SSL.git
		final SslContext sslCtx;
		if (SSL) {
			sslCtx = SslContext
					.newClientContext(InsecureTrustManagerFactory.INSTANCE);
		} else {
			sslCtx = null;
		}
		final String host = "localhost";
		final int port = 8080;
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					if (sslCtx != null) {
						//p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
					}
					p.addLast(
							//new LoggingHandler(LogLevel.INFO),
							new ObjectDecoder(ClassResolvers
									.cacheDisabled(ObjectClient.class
											.getClassLoader())),
							new ObjectEncoder()
							// new WriteLogHandler(),
							//new ObjectClientHandler()
							);
				}
			});

			// Start the client.
			// ChannelFuture f = b.connect(host, port).sync(); // (5) the sync()
			// will rethrows the cause of the failure if this future failed.
			ChannelFuture f = b.connect(host, port).await(); // (5) but await()
																// not.
			if (f.isSuccess()) {
				System.out.println("Client Start.");
				System.out.println(f.channel().getClass());
				ChannelFuture ff = f.channel().writeAndFlush("ccccccccc");

				ff.awaitUninterruptibly();
				System.out.println("发送了:cccccccc");

				User user = new User();
				user.setUserId(111);
				user.setUserName("denglt");
				user.setAge(38);
				user.setInterests(new String[] { "打球", "跑步" });

				ff = f.channel().writeAndFlush(user);
				ff.await();
				// Wait until the connection is closed.
				f.channel().closeFuture().sync();
				System.out.println("Client Close.");
			} else {
				System.out.println("Client 启动失败。" + f.cause());
			}

		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}
