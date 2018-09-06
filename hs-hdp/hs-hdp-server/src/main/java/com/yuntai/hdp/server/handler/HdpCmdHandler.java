package com.yuntai.hdp.server.handler;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.util.HdpCmdHelper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by denglt on 16/10/18.
 */
@ChannelHandler.Sharable
public class HdpCmdHandler extends SimpleChannelInboundHandler<RequestPack> {

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        boolean isAccept = super.acceptInboundMessage(msg);
        if (isAccept) {
            return HdpCmdHelper.isHdpCmdRequest((RequestPack) msg);
        }
        return isAccept;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestPack msg) {
        ctx.writeAndFlush("ReceiveAnswer:" + msg.getHdpSeqno());  //发送数据接收应答消息
        ctx.writeAndFlush(HdpCmdHelper.deal(msg));
    }

}
