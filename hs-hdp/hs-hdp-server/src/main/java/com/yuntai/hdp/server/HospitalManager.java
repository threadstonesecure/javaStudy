package com.yuntai.hdp.server;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSON;
import com.yuntai.hdp.server.net.Connection;
import com.yuntai.hdp.server.net.ConnectionManager;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author denglt
 * @Description hospital管理类
 */
@Service("hospitalManager")
public class HospitalManager {
    private static final String HDP_ACCESS_TOKEN_KEY = "HOSPITAL:CONFIG:HDP:ACCESS_TOKEN";
    private static final String HDP_REDIRECT_HOSPITAL_KEY = "HOSPITAL:CONFIG:HDP:REDIRECT_HOSPITAL";
    private static ConcurrentMap<String, Set<IpAndPort>> hospitals = new ConcurrentHashMap<>();
    private static ReentrantLock lock = new ReentrantLock();
    private static ConcurrentMap<String, String> hdpAccessTokens = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, String> hdpRedirectHospitals = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, RedirectHospital> cacheRedirectHospitals = new ConcurrentHashMap<>();
    private static RedisTemplate<String, String> redisTemplate;

    public void init() {
        //  init HdpAcessTokens

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        Map<String,String> redisHdpAccessTokens = opsForHash.entries(HDP_ACCESS_TOKEN_KEY);
        if (redisHdpAccessTokens != null) {
            for (Map.Entry<String, String> entry : redisHdpAccessTokens.entrySet()) {
                hdpAccessTokens.put(entry.getKey(), entry.getValue());
            }
        }

        // init HdpTargetHospital
        scanRedirectHospital();
    }

    /**
     * 校验 accessToken
     * 目前为了兼容已经上线的前置机,使用参数"HdpClientAccessMode"来控制校验方式:
     * HdpClientAccessMode = 1 (default):可以无Token登录;由是否配置了Token来决定
     * HdpClientAccessMode = 2 :必须有AccessToken才能登录
     * @param hosId
     * @param accessToken
     * @return
     */
    public static boolean checkAccessToken(String hosId, String accessToken) {
        String pzAccessToken = hdpAccessTokens.get(hosId);
        String accessMode = hdpAccessTokens.get("HdpClientAccessMode");
        accessMode = accessMode == null ? "1" :accessMode;
        if (accessMode.equals("2") && pzAccessToken == null) return false;
        if (pzAccessToken != null && !pzAccessToken.equals(accessToken)) return false;
        return true;
    }

    /**
     * 注册前置机
     *
     * @param hosId
     * @param ip
     * @param port
     * @param accessToken
     * @return
     */
    public static boolean add(String hosId, String ip, int port, String accessToken) {

        if (!checkAccessToken(hosId, accessToken)) return false;

        IpAndPort iap = new IpAndPort(ip, port, accessToken);

        try {
            lock.lock();
            Set<IpAndPort> iaps = hospitals.get(hosId);
            if (iaps == null) {
                iaps = new HashSet<IpAndPort>();
            }
            hospitals.put(hosId, iaps);
        } finally {
            lock.unlock();
        }

        Set<IpAndPort> iaps = hospitals.get(hosId);

        synchronized (iaps) {// Collections.synchronizedSet(iaps);
            iaps.add(iap);
        }

        return true;

    }


    /**
     * 获取重定向的前置机hosId
     * @param from_hosId
     * @param cmd  先预留,后期可能会根据业务类型进行重定向
     * @return 返回重定向的前置机hosId,如果没有重定向配置返回null
     */
    // TODO: 2017/7/10  denglt   支持一家医院三个以上的前置机，及重定向配置为数组的情况
    public static String getRedirectHosId(String from_hosId, String cmd) {
        String redirectStr = hdpRedirectHospitals.get(from_hosId);
        if (redirectStr != null) {
            HdpServer.log.info(String.format("Hospital[%s] Redirect Info: %s", from_hosId, redirectStr));
            try {
                RedirectHospital redirectHospital = cacheRedirectHospitals.get(redirectStr);
                if (redirectHospital == null) {
                    redirectHospital = JSON.parseObject(redirectStr, RedirectHospital.class); //  JSON.parseArray()
                    cacheRedirectHospitals.put(redirectStr, redirectHospital);
                }
                return redirectHospital.redirectCmds == null ? redirectHospital.redirectHosId :
                         redirectHospital.redirectCmds.contains(cmd) ? redirectHospital.redirectHosId : null;
            } catch (Exception ex) {
                HdpServer.log.error(String.format("Hospital[%s] Redirect Info is error!", from_hosId), ex);
            }
        }
        return null;
    }


    /**
     * 根据hosId,IP,port 删除退出的前置机连接信息
     * @param hosId
     * @param ip
     * @param port
     */
    public static void remove(String hosId, String ip, int port) {
        IpAndPort iap = new IpAndPort(ip, port);
        Set<IpAndPort> iaps = hospitals.get(hosId);
        if (iaps != null) {
            synchronized (iaps) {
                Iterator<IpAndPort> iter = iaps.iterator();
                while (iter.hasNext()) {
                    IpAndPort temp = iter.next();
                    if (temp.equals(iap)) {
                        iter.remove();
                    }
                }
            }
        }
    }


    /**
     * 根据hosId获取Connection
     * @param hosId
     * @return
     */
    public static Connection getConnect(String hosId) {
        Set<IpAndPort> iaps = hospitals.get(hosId);
        if (iaps == null || iaps.isEmpty()) {
            return null;
        }
        synchronized (iaps) {
            Iterator<IpAndPort> iter = iaps.iterator();
            while (iter.hasNext()) {
                IpAndPort iap = iter.next();
                Connection conn = ConnectionManager.getConnection(iap.ip, iap.port);
                if (conn == null) {
                    iter.remove();
                } else {
                    return conn;
                }
            }
        }

        return null;
    }

    @Resource
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        HospitalManager.redisTemplate = redisTemplate;
    }

    /**
     *  读取redis中AccessToken的配置信息
     */
    @Scheduled(fixedDelay = 60000 )
    public void scanHosAccessToken() {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        Map<String, String> regdisHdpAccessTokens = opsForHash.entries(HDP_ACCESS_TOKEN_KEY);
        Map<String, String> alterTokens = new HashMap<>();
        for (Map.Entry<String, String> entry : regdisHdpAccessTokens.entrySet()) {
            String hosId = entry.getKey();
            String pzToken = entry.getValue();
            String currentToken = hdpAccessTokens.get(hosId);
            if (!pzToken.equals(currentToken)) {
                hdpAccessTokens.put(hosId, pzToken);
                alterTokens.put(hosId, pzToken);
            }
        }

        for (Map.Entry<String, String> entry : alterTokens.entrySet()) {
            closeHospital(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 关闭Token不符合要求的前置机连接
     *
     * @param hosId
     * @param accessToken  : new accessToken
     */
    private static void closeHospital(String hosId, String accessToken) {
        Set<IpAndPort> iaps = hospitals.get(hosId);
        if (iaps == null || iaps.isEmpty()) {
            return;
        }
        synchronized (iaps) {
            for (IpAndPort iap : iaps) {
                if (!accessToken.equals(iap.accessToken)) {
                    Connection conn = ConnectionManager.getConnection(iap.ip, iap.port);
                    conn.write(HdpServer.HDPSERVER_NOTICE_PREFIX + "-> AccessToken is altered by admin,hdpClient will close!");
                    HdpServer.log.warn(String.format("Hospital[%s] AccessToken is altered by admin, Channel will close!", hosId));
                    conn.close();
                }
            }
        }
    }

    /**
     *  读取redis中重定向前置机信息
     */
    @Scheduled(fixedDelay = 60000)
    public void scanRedirectHospital(){
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        Map<String, String> redisHdpRedirectHospital = opsForHash.entries(HDP_REDIRECT_HOSPITAL_KEY);
        if (redisHdpRedirectHospital != null){
            for (Map.Entry<String, String> entry : redisHdpRedirectHospital.entrySet()){
                hdpRedirectHospitals.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public static List<String> getHospitalInfo() {
        List<String> hosps = new ArrayList<String>();
        Set<Entry<String, Set<IpAndPort>>> entrys = hospitals.entrySet();
        for (Entry<String, Set<IpAndPort>> entry : entrys) {
            String hosId = entry.getKey();
            Set<IpAndPort> iaps = entry.getValue();
            synchronized (iaps) {
                if (iaps.isEmpty()) {
                    hosps.add(String.format("Hospital[%s] is inactive!", hosId));
                    continue;
                }
                Iterator<IpAndPort> iter = iaps.iterator();
                while (iter.hasNext()) {
                    IpAndPort iap = iter.next();
                    Connection conn = ConnectionManager.getConnection(iap.ip, iap.port);
                    if (conn != null) {
                        hosps.add(String.format("Hospital[%s] on IP[%s] Port[%d] is active! ", hosId, iap.ip, iap.port));
                    } else {
                        iter.remove();
                        hosps.add(String.format("Hospital[%s] on IP[%s] Port[%d] is inactive!", hosId, iap.ip, iap.port));
                    }
                }
            }

        }
        return hosps;
    }

    public static Map<String,String> getAccessTokenInfo() {
        return hdpAccessTokens;
    }

    private static void alterInRedis(String key, String hashKey, String value) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        opsForHash.put(key, hashKey, value);
    }

    private static void deleteFromRedis(String key, String hashKey) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        opsForHash.delete(key, hashKey);
    }

    public static String alterAccessToken(String hosId, String accessToken) {
        String oldToken = hdpAccessTokens.get(hosId);
        if (accessToken == null || accessToken.isEmpty()) {
            hdpAccessTokens.remove(hosId);
            deleteFromRedis(HDP_ACCESS_TOKEN_KEY, hosId);
        } else {
            if (!accessToken.equals(oldToken)) {
                hdpAccessTokens.put(hosId, accessToken);
                closeHospital(hosId, accessToken);
                alterInRedis(HDP_ACCESS_TOKEN_KEY, hosId, accessToken);
            }
        }
        return oldToken;
    }

    public static Map<String,String> getRedirectHospitalInfo(){

        return hdpRedirectHospitals;
    }

    public static String alterRedirectHospital(String hosId, String redirect) {
        String oldRedirect;
        if (redirect == null || redirect.isEmpty()) {
            oldRedirect = hdpRedirectHospitals.remove(hosId);
            deleteFromRedis(HDP_REDIRECT_HOSPITAL_KEY, hosId);
        } else {
            oldRedirect = hdpRedirectHospitals.put(hosId, redirect);
            alterInRedis(HDP_REDIRECT_HOSPITAL_KEY, hosId, redirect);
        }
        return oldRedirect;
    }

    private static class IpAndPort {
        String ip;
        int port;
        String accessToken;

        public IpAndPort(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public IpAndPort(String ip, int port, String accessToken) {
            this.ip = ip;
            this.port = port;
            this.accessToken = accessToken;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((ip == null) ? 0 : ip.hashCode());
            result = prime * result + port;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            IpAndPort other = (IpAndPort) obj;
            if (ip == null) {
                if (other.ip != null)
                    return false;
            } else if (!ip.equals(other.ip))
                return false;
            if (port != other.port)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "[ip=" + ip + ", port=" + port + ", accessToken=" + accessToken + "]";
        }

    }

    private static class  RedirectHospital{
        public String redirectHosId; // 目标前置机配置的通道id（一般配置hosId）
        public List<String> redirectCmds; // 要转发的对接指令列表。（当为空时，转发所有指令）
    }

    static {
        hdpAccessTokens.put("_", "98608d4679a28b719815ee03f7c404e0");
    }


}



