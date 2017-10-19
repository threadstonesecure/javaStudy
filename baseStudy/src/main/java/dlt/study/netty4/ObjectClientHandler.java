package dlt.study.netty4;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

import dlt.domain.model.User;

public class ObjectClientHandler extends ChannelDuplexHandler {

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
			SocketAddress localAddress, ChannelPromise promise)
			throws Exception {
		System.out.println("正在连接上服务器：" + remoteAddress);
		super.connect(ctx, remoteAddress, localAddress, promise);


	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive");
		super.channelActive(ctx);
		User user = new User();
		user.setUserId(1);
		user.setUserName("denglt");
		user.setAge(38);
		user.setInterests(new String[] { "打球", "跑步" });

		ctx.write(user);

		user.setUserId(2);
		user.setUserName("zyy");
		ctx.write(user);
		// Thread.sleep(100);

		String msg = "Hello, I'm client. ";
		ctx.write(msg);
		ctx.flush();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive");
		super.channelInactive(ctx);

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// System.out.println(msg);
		if (msg instanceof User) {
			User u = (User) msg;
			System.out.println("收到User：" + u);
		} else if (msg instanceof String) {
			System.out.println("收到String：" + msg);
		} else
			super.channelRead(ctx, msg);
	}
	
	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		super.flush(ctx);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		System.out.println("write:"+msg);
		super.write(ctx, msg, promise);
	}
}
