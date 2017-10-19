package dlt.study.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

import dlt.domain.model.User;


public class ObjectServerHandler extends SimpleChannelHandler {

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		System.out.println("ObjectServerHandler:" + this);
		// TODO Auto-generated method stub
		if (e instanceof IdleStateEvent) {
			System.out.println("I am idle");
		} else
			super.handleUpstream(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Object msg = e.getMessage();
		// System.out.println(msg);
		Channel channel = ctx.getChannel();
		if (msg instanceof User) {
			User u = (User) msg;
			System.out.println("收到user：" + u);
			u.setUserName(u.getUserName() + " from Server");
			channel.write(u);

		} else if(msg instanceof String) {
			System.out.println("收到String：" + msg);
			channel.write(msg + " from Server");
		} else if (msg instanceof ChannelBuffer){
			super.messageReceived(ctx, e);
		} else{
			System.out.println("收到Object："+ msg);
		}
	}
}
