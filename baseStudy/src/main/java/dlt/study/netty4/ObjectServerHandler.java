package dlt.study.netty4;

import java.net.SocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import dlt.domain.model.User;

public class ObjectServerHandler extends  ChannelInboundHandlerAdapter {

	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {

		if (msg instanceof User) {
			User u = (User) msg;
			System.out.println("收到user：" + u);
			u.setUserName(u.getUserName() + " from Server");
			ctx.write(u);

		} else if(msg instanceof String) {
			System.out.println("收到String：" + msg);
			ctx.write(msg + " from Server");
		} else if (msg instanceof ByteBuf){
			super.channelRead(ctx, msg);
		} else{
			System.out.println("收到Object："+ msg);
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
		ctx.flush();
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive");
		super.channelActive(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive");
		super.channelInactive(ctx);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println("exceptionCaught");
		cause.printStackTrace();
		ctx.close();
	}
}
