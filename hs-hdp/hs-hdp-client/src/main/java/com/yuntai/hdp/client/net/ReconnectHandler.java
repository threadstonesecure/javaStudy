package com.yuntai.hdp.client.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Description 失败重新connect处理器
 * @author denglt
 *
 */
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

	Log log = LogFactory.getLog(ReconnectHandler.class.getName());

	@SuppressWarnings("rawtypes")
	private Connector connector;

	private int delay;

	@SuppressWarnings("rawtypes")
	public ReconnectHandler(Connector connector, int delay) {
		this.connector = connector;
		this.delay = delay;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(final ChannelHandlerContext ctx)
			throws Exception {
		log.info("Disconnected from HdpServer ");
		super.channelInactive(ctx);
	}

	@Override
	public void channelUnregistered(final ChannelHandlerContext ctx)
			throws Exception {
		log.info("After Sleeping for: " + delay + "s , reconnect to HdpServer");

		EventLoop loop = ctx.channel().eventLoop();
		loop.schedule(new Runnable() {
			@Override
			public void run() {
				connector.connect();
			}
		}, delay, TimeUnit.SECONDS);
		super.channelUnregistered(ctx);
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Channel will be closed:",cause);
        ctx.close();
        //super.exceptionCaught(ctx, cause);
    }

    public interface Connector<V> {
		public V connect();
	}

}
