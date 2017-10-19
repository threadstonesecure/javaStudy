package dlt.study.netty;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class ReadBufferHandler extends SimpleChannelHandler {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		System.out.println("ReadBufferHandler:"+this);
		Object msg = e.getMessage();
		if (msg instanceof ChannelBuffer){
			ChannelBuffer buffer = (ChannelBuffer)msg;
			System.out.println("收到 :"+buffer.toString(Charset.defaultCharset()));
		}
		super.messageReceived(ctx, e);
	}
}
