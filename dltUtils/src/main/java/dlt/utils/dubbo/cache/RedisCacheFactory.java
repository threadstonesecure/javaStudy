package dlt.utils.dubbo.cache;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import dlt.utils.dubbo.URLUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.DigestUtils;

import static dlt.utils.dubbo.cache.Constants.EXPIRY_KEY;
import static dlt.utils.dubbo.cache.Constants.REDIS_CONNECTION_FACTORY;

/**
 * Created by denglt on 2016/11/8.
 */


public class RedisCacheFactory extends AbstractCacheFactory {


    private RedisConnectionFactory connectionFactory;

    @Override
    protected Cache createCache(URL url) {
        String redisFactoryName =  URLUtils.getParameter(url,REDIS_CONNECTION_FACTORY);
        RedisConnectionFactory connectionFactory =  getConnectionFactory(redisFactoryName);
        if (connectionFactory == null){
            CacheFilter.log.error("not find RedisConnectionFactory config!");
            return null;
        }

        RpcContext rpcContext = RpcContext.getContext();
        boolean isProvider = rpcContext.isProviderSide();
        String expiryStr = URLUtils.getParameter(url, EXPIRY_KEY);
        int expiry = 0;
        if (!StringUtils.isEmpty(expiryStr)) {
            try {
                expiry = Integer.parseInt(expiryStr);
            } catch (NumberFormatException ex) {

            }
        }
        String md5Url = DigestUtils.md5DigestAsHex(URLUtils.toKeyStr(url).getBytes());
        String redisKey = "cache:" + url.getServiceInterface() + ":" + (isProvider ? "P:" : "C:") + URLUtils.getParameter(url, Constants.METHOD_KEY) + ":" + md5Url;
        TTLRedisTemplate ttlRedisTemplate = new TTLRedisTemplate(expiry,connectionFactory);
        Cache cache = new RedisCache(redisKey, ttlRedisTemplate,this);
        cache.put("url",url.toFullString());
        cache.put("md5",URLUtils.toKeyStr(url));
        return cache;
    }

    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    protected RedisConnectionFactory getConnectionFactory(String name){
        RedisConnectionFactory connectionFactory = SpringRedisHelper.getRedisConnectionFactory(name);
        return connectionFactory != null ? connectionFactory :this.connectionFactory;
    }
}
