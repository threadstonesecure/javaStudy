package dlt.study.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("client logon");
		super.channelActive(ctx);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("handlerAdded");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("handlerRemoved");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof ByteBuf) {
			ByteBuf m = (ByteBuf) msg; // (1)
			System.out.println("信息长度 ：" + m.readableBytes());
			try {
				long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
				System.out.println(new Date(currentTimeMillis));
				// ctx.close(); //关闭客户端
			} finally {
				m.release();
			}
		} else if (msg instanceof Date) {
			System.out.println("服务器时间:" + msg);
		} else {
			ctx.fireChannelRead(msg);
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("TimeClientHandler.channelReadComplete");
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println("TimeClientHandler.exceptionCaught");
		cause.printStackTrace();
		// ctx.close();
		super.exceptionCaught(ctx, cause);
	}
}
