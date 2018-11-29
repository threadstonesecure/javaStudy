package com.yuntai.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultKind;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.server.HdpServer2HdpServer;
import com.yuntai.hdp.server.NodeConfig;
import com.yuntai.util.spring.SpringContextUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.netty.util.internal.StringUtil.NEWLINE;


public class HdpCmdHelper {
    private static final String HDP_CMD = "$$HDP_COMMAND$$";

    private static NodeConfig nodeConfig;
    private static HdpServer2HdpServer hdpServer2HdpServer;

    static {
        nodeConfig = SpringContextUtils.getBean(NodeConfig.class);
        hdpServer2HdpServer = SpringContextUtils.getBean(HdpServer2HdpServer.class);
    }

    public static boolean isHdpCmdRequest(RequestPack requestPack) {
        String cmd = requestPack.getCmd();
        return cmd != null & cmd.equals(HDP_CMD);
    }

    public static ResultPack deal(RequestPack requestPack) {
        if (!isHdpCmdRequest(requestPack)) {
            throw new IllegalArgumentException("传入的参数" + requestPack + "错误！");
        }
        ResultPack resultPack = HdpHelper.newResult(requestPack);
        try {
            JSONObject jsonObject = JSON.parseObject(requestPack.getBody());
            String hdpcmd = jsonObject.getString("hdpcmd");
            Map<String, String> params = new HashMap<>();
            Set<String> keys = jsonObject.keySet();
            for (String key : keys) {
                params.put(key, jsonObject.getString(key));
            }

            StringBuilder buf = new StringBuilder(200);
            buf.append(nodeConfig.toString() + NEWLINE);
            buf.append(hdpCmd(hdpcmd, params) + NEWLINE);
            buf.append("===========================end==========================" + NEWLINE + NEWLINE + NEWLINE);
            if (nodeConfig.isCascade() && !hdpcmd.equals("sendData")) {
                ResultPack cascadeResultPack = dealCascadeHdp(requestPack);
                if (cascadeResultPack != null)
                    buf.append(cascadeResultPack.getKind().equals("0") ? cascadeResultPack.getBody() : cascadeResultPack.getMsg());
            }

            resultPack.setBody(buf.toString());
            resultPack.setKind(ResultKind.OK.getKind());
        } catch (Exception ex) {
            ex.printStackTrace();
            resultPack.setKind("异常");
            resultPack.setBody(ex.getMessage());
        }
        return resultPack;
    }

    /**
     * 给HdpServlet
     *
     * @param cmd
     * @param params
     * @return
     */
    public static ResultPack deal(String cmd, Map<String, String> params) {
        RequestPack requestPack = new RequestPack();
        requestPack.setCmd(HDP_CMD);
        if (params != null) {
            params.put("hdpcmd", cmd);
        }
        requestPack.setBody(JSON.toJSONString(params));
        return deal(requestPack);
    }

    private static final String cmdflag = "$$FROM CASCADE HDPSERVER$$";

    private static ResultPack dealCascadeHdp(RequestPack requestPack) {
        if (cmdflag.equals(requestPack.getHdpOther())) return null;

        requestPack.setHdpOther(cmdflag);
        if (nodeConfig.isToHosByCascade()) {
            return hdpServer2HdpServer.getHospitalResult(requestPack, 30);
        }
        if (nodeConfig.isToYunServiceByCascade()) {
            return hdpServer2HdpServer.process(requestPack);
        }
        return null;
    }

    private static String hdpCmd(String cmd, Map<String, String> params) throws Exception {

        Method method;
        String result;
        try {
            method = HdpHelper.class.getDeclaredMethod(cmd, Map.class);
            method.setAccessible(true);
            result = (String) method.invoke(HdpHelper.class, params);
        } catch (NoSuchMethodException ex) {
            method = HdpHelper.class.getDeclaredMethod(cmd);
            method.setAccessible(true);
            result = (String) method.invoke(HdpHelper.class);
        }

        return result;
    }
}
