package dlt.study.netty4;



import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		System.out.println("one client logon!");
		Connection conn = new Connection(ctx.channel());
		final ByteBuf time = ctx.alloc().buffer(4);
		time.writeInt((int)(System.currentTimeMillis()/1000L+2208988800L));
/*		for (int i = 0;i<100;i++){
			 time.retain();
			 time.setIndex(0, 4);
			 ctx.writeAndFlush(time); //ChannelHandlerContext.write**(msg)等方法，会自动调用msg.release()方法。
		}*/
		time.setIndex(0, 4);
		final ChannelFuture f =  ctx.writeAndFlush(time);
		
		
		f.addListener(new ChannelFutureListener(){
			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception {
				assert  f == future;
				//ctx.close(); //将关闭客户
				
			}
		});
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("one client logoff!");
		super.channelInactive(ctx);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
