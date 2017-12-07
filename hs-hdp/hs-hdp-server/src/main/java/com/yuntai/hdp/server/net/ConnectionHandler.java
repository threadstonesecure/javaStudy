package com.yuntai.hdp.server.net;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Description  Connection处理器
 * @author denglt
 *
 */
@ChannelHandler.Sharable
public class ConnectionHandler extends ChannelInboundHandlerAdapter {

    private static Log log = LogFactory.getLog(ConnectionHandler.class);
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ConnectionManager.addConnection(Connection.wrap(ctx.channel()));
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//log.debug("进入ConnectionHandler.channelInactive");
        ConnectionManager.removeConnection(Connection.wrap(ctx.channel()));
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
        //log.debug("进入ConnectionHandler.exceptionCaught");
        log.error(cause);
		ConnectionManager.removeConnection(Connection.wrap(ctx.channel()));
        ctx.close();
		// super.exceptionCaught(ctx, cause);
	}
}
