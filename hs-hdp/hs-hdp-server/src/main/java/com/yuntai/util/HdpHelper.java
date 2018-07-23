package com.yuntai.util;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.AccessHospitalHandler;
import com.yuntai.hdp.server.HospitalManager;
import com.yuntai.hdp.server.updata.Command;
import com.yuntai.hdp.server.updata.DiscoveryUpdataHandler;
import com.yuntai.util.spring.PropertyConfigurer;
import com.yuntai.util.spring.SpringContextUtils;
import org.springframework.util.StringUtils;
import sun.misc.SharedSecrets;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @author denglt
 */
public class HdpHelper {


    public static boolean isPortInUse(String host, int port) {
        boolean result = false;

        try {
            (new Socket(host, port)).close();
            result = true;
        } catch (Exception e) {
            // Could not connect.
            // e.printStackTrace();
        }
        return result;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public static ResultPack newResult(RequestPack request) {
        ResultPack resultPack = new ResultPack();
        resultPack.setSeqno(request.getSeqno());
        resultPack.setHosId(request.getHosId());
        resultPack.setCmd(request.getCmd());
        resultPack.setHdpSeqno(request.getHdpSeqno());
        resultPack.setCallMode(request.getCallMode());
        resultPack.setReturnTime(System.currentTimeMillis());
        return resultPack;
    }


    public static String sendData(Map<String, String> params) {
        RequestPack requestPack = new RequestPack();
        requestPack.setHosId(params.get("hosId"));
        requestPack.setCmd(params.get("cmd"));
        requestPack.setBody(params.get("body"));
        requestPack.setSeqno(params.get("seqno"));
        int timeout;
        try {
            timeout = Integer.parseInt(params.get("timeout"));
        } catch (Exception ex) {
            timeout = 60;
        }
        ResultPack resultPack = accessHospitalHandler.getHospitalResult(
                requestPack, timeout);
        return JsonUtils.toJson(resultPack);
    }

    public static String allInfo() {
        StringBuilder buf = new StringBuilder(1000);
        buf.append(version());
        buf.append(hosp());
        buf.append(registry());
        buf.append(updatahandler());
        buf.append(accessToken());
        buf.append(redirectHospital());

        return "ALL INFO:" + NEWLINE + buf.toString();
    }

    public static String hosp() {
        return orderList2String("Hospital Info:", HospitalManager.getHospitalInfo());
    }

    public static String registry() {
        return list2String("dubbo registry info:", discoveryUpdataHandler.getRegistryInfo());
    }

    public static String updatahandler() {
        return list2String("dubbo updatahandler info:", discoveryUpdataHandler.getUpdataHandlerInfo());
    }

    public static String removeUpdataHandler(Map<String, String> params) {
        String cmd = params.get("cmd");
        if (StringUtils.isEmpty(cmd)) {
            return "请指定cmd参数！";
        }
        Command command = discoveryUpdataHandler.removeCmd(cmd);
        if (command == null)
            return String.format("updatahandler[%s] is not exists!", cmd);
        return "remove " + command;

    }

    public static String accessToken() {
        return orderList2String("Hospital AccessToken:", mapToIterable(HospitalManager.getAccessTokenInfo(), "->"));
    }

    public static String alterToken(Map<String, String> params) {
        String hosId = params.get("hosId");
        String newAccessToken = params.get("newAccessToken");
        String oldToken = HospitalManager.alterAccessToken(hosId, newAccessToken);
        return "oldToken:" + oldToken;

    }

    public static String redirectHospital() {
        return orderList2String("Hospital Redirect Info:", mapToIterable(HospitalManager.getRedirectHospitalInfo(), "->"));
    }

    public static String alterRedirect(Map<String, String> params) {
        String hosId = params.get("hosId");
        String redirect = params.get("redirect");
        String oldRedirect = HospitalManager.alterRedirectHospital(hosId, redirect);
        return ("oldRedirect:" + oldRedirect);
    }

    public static String config() {
        return orderList2String("Server Config:", mapToIterable(PropertyConfigurer.properties, "="));
    }

    public static String version() {
        return "HdpServer Version:" + HDP_SERVER_VERSION + NEWLINE;
    }

    public static String memory() {
        HashMap<String, String> memoryInfo = Maps.newLinkedHashMap();
        memoryInfo.put("direct.max", "" + sun.misc.VM.maxDirectMemory() / 1024 / 1024);
        memoryInfo.put("direct.used", "" + SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed() / 1024 / 1024);
        memoryInfo.put("direct.capacity", "" + SharedSecrets.getJavaNioAccess().getDirectBufferPool().getTotalCapacity() / 1024 / 1024);

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long allocatedMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = allocatedMemory - freeMemory;
        long totalFreeMemory = freeMemory + maxMemory - allocatedMemory;
        memoryInfo.put("maxMemory", maxMemory + "M");
        memoryInfo.put("allocatedMemory", allocatedMemory + "M");
        memoryInfo.put("freeMemory", freeMemory + "M");
        memoryInfo.put("usedMemory", usedMemory + "M");
        memoryInfo.put("totalFreeMemory", totalFreeMemory + "M");
        return list2String("Memory Info:", mapToIterable(memoryInfo, "="));
    }

    public static String jvm() {
        return orderList2String("Env Info:", mapToIterable(System.getenv(), "="))
                + NEWLINE + NEWLINE +
                orderList2String("Properties Info:", mapToIterable2(System.getProperties(), "="));
    }

    public static void start() throws Exception{
        String shellFile = System.getProperty("user.dir") + File.separator + "bin" + File.separator + "run.sh";
        ProcessBuilder pb = new ProcessBuilder("sh",shellFile, "new");
        Process p = pb.start();
        int exitCode = p.waitFor();
        assert exitCode == 0;
    }

    public static void restart() throws Exception {
        System.setProperty("hdp.restart","1");
        shutdown();
    }

    public static void restart_2() throws Exception {
        String shellFile = System.getProperty("user.dir") + File.separator + "bin" + File.separator + "run.sh";
        Integer pid = Integer.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]).intValue();
        ProcessBuilder pb = new ProcessBuilder("sh",shellFile, "restartpid", pid.toString());
        Process p = pb.start();
        int exitCode = p.waitFor();
        assert exitCode == 0;
    }


    public static void restart_1() throws Exception {
        StringBuilder cmd = new StringBuilder();
        // cmd.append("cd "+System.getProperty("user.dir")+" ; ");
        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            cmd.append(jvmArg + " ");
        }
        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
        cmd.append(System.getProperty("sun.java.command"));
        Runtime.getRuntime().exec(cmd.toString());
        System.exit(0);
    }

    public static String gc() {
        try {
            long start = System.currentTimeMillis();
            System.gc();
            long end = System.currentTimeMillis();
            return "Escape Time：" + (end - start);
        } catch (Exception ex) {
            return ex.getMessage();
        }

    }

    public static void shutdown() {
        Runtime.getRuntime().exit(0);
    }

    private static Iterable<String> mapToIterable(Map<String, String> map, final String separator) {
        return Iterables.transform(map.entrySet(),
                new Function<Map.Entry<String, String>, String>() {
                    @Override
                    public String apply(Map.Entry<String, String> input) {
                        return input.getKey() + separator + input.getValue();
                    }
                });
    }

    private static Iterable<String> mapToIterable2(Map<Object, Object> map, final String separator) {
        return Iterables.transform(map.entrySet(),
                new Function<Map.Entry<Object, Object>, String>() {
                    @Override
                    public String apply(Map.Entry<Object, Object> input) {
                        return input.getKey() + separator + input.getValue();
                    }
                });
    }


    private static String list2String(String title, Iterable<String> contents) {
        StringBuilder buf = new StringBuilder(200);
        buf.append(title + NEWLINE);
        Joiner.on(NEWLINE).appendTo(buf, contents).append(NEWLINE);
        return buf.toString();
    }

    private static String orderList2String(String title, Iterable<String> contents) {
        StringBuilder buf = new StringBuilder(200);
        buf.append(title + NEWLINE);
        Joiner.on(NEWLINE).appendTo(buf, Ordering.natural().sortedCopy(contents)).append(NEWLINE);
        return buf.toString();
    }



    private static final AccessHospitalHandler accessHospitalHandler;
    private static final DiscoveryUpdataHandler discoveryUpdataHandler;
    private static final String HDP_SERVER_VERSION;

    static {
        accessHospitalHandler = SpringContextUtils.getBean("accessHospitalHandler");
        discoveryUpdataHandler = SpringContextUtils.getBean("discoveryUpdataHandler");
        HDP_SERVER_VERSION = "3.3 gateway  cascade";
    }


}
