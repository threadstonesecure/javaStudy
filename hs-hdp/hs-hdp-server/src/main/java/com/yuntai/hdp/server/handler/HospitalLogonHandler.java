package com.yuntai.hdp.server.handler;

import com.alibaba.fastjson.JSON;
import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.server.HdpServer;
import com.yuntai.hdp.server.HospitalManager;
import com.yuntai.hdp.server.net.Connection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.logging.Log;

/**
 * 不能共享 医院注册hosId处理器
 * 增加绑定hos_id：解决Haproxy转发后ip变为HA的IP，并端口复用后，ip和port不能唯一确定一个connect
 */
public class HospitalLogonHandler extends SimpleChannelInboundHandler<RequestPack> {
    private static Log log = HdpServer.log;
    private static final String CMD_REG_HOSPITAL = "$$Register Hospital$$";
    private static final String REG_HOSPITAL_OK = "$$Register Hospital OK$$";
    private String hosId;
    private String remoteIp;
    private String accessToken; // 访问权限Token
    private int remotePort;
    private boolean isLog = true;
    private boolean isMonitor = false;

    private final static LogChannelHandler logChannelHandler = new LogChannelHandler();
    private final static HdpCmdHandler hdpCmdHandler = new HdpCmdHandler();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Connection conn = Connection.wrap(ctx.channel());
        remoteIp = conn.getRemoteIp();
        remotePort = conn.getRemotePort();
        log.info(String.format("One HdpClient[ip=%s, port=%d] logon.", remoteIp, remotePort));
        super.channelActive(ctx);
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        boolean isAccept = super.acceptInboundMessage(msg);
        if (isAccept) {
            RequestPack requestPack = (RequestPack) msg;
            String cmd = requestPack.getCmd();
            isAccept = cmd != null & cmd.equals(CMD_REG_HOSPITAL);
        }
        return isAccept;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestPack requestPack) throws Exception {
        decode(requestPack);
        if (HospitalManager.add(hosId, remoteIp, remotePort, accessToken)) {
            log.info(String.format("Hospital[%s] register on [ip=%s, port=%d]", hosId, remoteIp, remotePort));
            ctx.writeAndFlush(REG_HOSPITAL_OK);
            if (isLog)
                ctx.pipeline().addAfter(ctx.name(), "logChannelHandler", logChannelHandler);
            if (isMonitor){
                ctx.pipeline().addAfter(ctx.name(), "hdpCmdHandler", hdpCmdHandler);
            }
        } else {
            ctx.writeAndFlush(HdpServer.HDPSERVER_NOTICE_PREFIX + String.format("-> AccessToken[%s] is error,hdpClient will close!", accessToken));
            log.error(String.format("Hospital[%s] register failly, because AccessToken[%s] is error ! ", hosId, accessToken));
            ctx.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (hosId != null) {
            HospitalManager.remove(hosId, remoteIp, remotePort);
            log.info(String.format("Hospital[%s] on [ip=%s, port=%d] logoff.", hosId, remoteIp, remotePort));
        } else {
            log.info(String.format("One HdpClient [ip=%s, port=%d] logoff.", remoteIp, remotePort));
        }
        super.channelInactive(ctx);
    }

    private void decode(RequestPack requestPack){
        hosId = requestPack.getHosId();
        try{
            BodyInfo bodyinfo = JSON.parseObject(requestPack.getBody(),BodyInfo.class);
            accessToken = bodyinfo.accessToken;
            isLog = bodyinfo.isLog;
            isMonitor = bodyinfo.isMonitor;
        }catch (Exception ex){
            accessToken = requestPack.getBody();
        }

    }
    private String getHosId() {
        return hosId;
    }


    public static String currentHosId(ChannelHandlerContext ctx) {
        HospitalLogonHandler hospitalLogonHandler = ctx.pipeline().get(HospitalLogonHandler.class);
        return hospitalLogonHandler.getHosId();
    }


    private static class BodyInfo{
        private String accessToken;
        private boolean isLog = true;
        private boolean isMonitor = false;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public boolean isLog() {
            return isLog;
        }

        public void setLog(boolean log) {
            isLog = log;
        }

        public boolean isMonitor() {
            return isMonitor;
        }

        public void setMonitor(boolean monitor) {
            isMonitor = monitor;
        }
    }

}
