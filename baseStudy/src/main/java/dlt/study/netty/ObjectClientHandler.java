package dlt.study.netty;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import dlt.domain.model.User;

public class ObjectClientHandler extends SimpleChannelHandler {

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelConnected(ctx, e);
		Channel channel = ctx.getChannel();
		User user = new User();
		user.setUserId(1);
		user.setUserName("denglt");
		user.setAge(38);
		user.setInterests(new String[]{"打球","跑步"});

		channel.write(user);
		//Thread.sleep(100); //sleep让服务器端能够分开触发相应的处理事件，因为netty不能保证消息收发次数是匹配的。
		                     //但是使用netty自带的 ObjectDecoder却不需要sleep.还是 ObjectDecoder的解码强
		
		user.setUserId(2);
		user.setUserName("zyy");
		channel.write(user);
		//Thread.sleep(100);
		
		String msg = "Hello, I'm client. ";
		channel.write(msg);
		
		//Thread.sleep(100);
		ChannelBuffer buffer = ChannelBuffers.buffer(msg.length());
		buffer.writeBytes(msg.getBytes());
		//channel.write(buffer);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Object msg = e.getMessage();
		//System.out.println(msg);
		if (msg instanceof User) {
			User u = (User) msg;
			System.out.println("收到User：" + u);
		}else if(msg instanceof String){
			System.out.println("收到String：" + msg);
		} else
			super.messageReceived(ctx, e);
	}
}
