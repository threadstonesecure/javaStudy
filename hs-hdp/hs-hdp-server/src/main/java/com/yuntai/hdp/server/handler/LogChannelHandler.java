package com.yuntai.hdp.server.handler;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.server.HdpServer;
import com.yuntai.hdp.server.HospitalManager;
import com.yuntai.hdp.server.net.Connection;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;

/**
 * 记录网络数据日志
 */
@ChannelHandler.Sharable
public class LogChannelHandler extends ChannelDuplexHandler {
    private static Log log = HdpServer.log;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logMessage(ctx,"RECEIVED ", msg);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        logMessage(ctx,"WRITE", msg);
        super.write(ctx, msg, promise);
    }

    private  void logMessage(ChannelHandlerContext ctx, String eventName, Object msg){
        String hosId = HospitalLogonHandler.currentHosId(ctx);
        if (isLog(hosId,msg)){
            log.info(formatMessage(hosId,eventName,msg));
        }
    }

    private  String formatMessage(String hosId, String eventName, Object msg){
        StringBuilder buf = new StringBuilder(eventName.length() + 80);
        buf.append(eventName + String.format(" [hospital[%s]] ",hosId)).append(": ");
        if (msg instanceof RequestPack){
            buf.append(log.isDebugEnabled() ? msg.toString() : ((RequestPack) msg).toKeyString());
        }else if (msg instanceof ResultPack){
            buf.append(log.isDebugEnabled() ? msg.toString() : ((ResultPack) msg).toKeyString());
        }
        forward(hosId,msg);
        return buf.toString();
    }

    private boolean  isLog(String hosId, Object msg){
        if ((msg instanceof String)){
            return false;
        }
        return true;
    }

    private void forward(String hosId, final Object msg) {
        if (msg instanceof String) return;
        Connection conn = HospitalManager.getConnect("_");
        if (conn == null) {
            try {
                //String hosId = BeanUtils.getProperty(msg, "hosId");
                conn = HospitalManager.getConnect("_"+hosId+"_");
            }catch(Exception e){
                return ;
            }
        }

        try {
            if (conn != null) {
                conn.getChannel().writeAndFlush(msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
