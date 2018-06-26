package com.yuntai.hdp.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.server.updata.DiscoveryUpdataHandler;
import com.yuntai.util.HdpHelper;
import com.yuntai.util.spring.SpringContextUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.*;

/**
 * Created by denglt on 16/10/18.
 */
@ChannelHandler.Sharable
public class HdpCmdHandler extends SimpleChannelInboundHandler<RequestPack> {
    private static final String HDP_CMD = "$$HDP_COMMAND$$";
    private DiscoveryUpdataHandler discoveryUpdataHandler = SpringContextUtils.getBean("discoveryUpdataHandler");

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        boolean isAccept = super.acceptInboundMessage(msg);
        if (isAccept) {
            RequestPack requestPack = (RequestPack) msg;
            String cmd = requestPack.getCmd();
            isAccept = cmd != null & cmd.equals(HDP_CMD);
        }
        return isAccept;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestPack msg) throws Exception {
        ctx.writeAndFlush("ReceiveAnswer:" + msg.getHdpSeqno());  //发送数据接收应答消息
        try {
            JSONObject jsonObject = JSON.parseObject(msg.getBody());
            String cmd = jsonObject.getString("hdpcmd");
            ResultPack resultPack =  HdpHelper.newResult(msg);
            Map<String,String> params = new HashMap<>();
            Set<String> keys = jsonObject.keySet();
            for (String key :keys){
                params.put(key,jsonObject.getString(key));
            }
            String content = HdpHelper.hdpCmd(cmd,params);
            resultPack.setBody(content);
            ctx.writeAndFlush(resultPack);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
