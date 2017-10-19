package dlt.utils.dubbo.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * Created by denglt on 2016/11/8.
 */
public class RedisCache implements ExCache {
    private static Log log = LogFactory.getLog(RedisCache.class);

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Object> opsForHash;
    private String redisKey;
    private AbstractCacheFactory cacheFactory;

    public RedisCache(String redisKey, RedisTemplate<String, Object> redisTemplate) {
        this(redisKey, redisTemplate, null);
    }

    public RedisCache(String redisKey, RedisTemplate<String, Object> redisTemplate, AbstractCacheFactory cacheFactory) {
        this.redisTemplate = redisTemplate;
        this.redisKey = redisKey;
        this.opsForHash = redisTemplate.opsForHash();
        this.cacheFactory = cacheFactory;
    }

    @Override
    public void put(Object key, Object value) {
        try {
            opsForHash.put(redisKey, key.toString(), value);
        } catch (Exception ex) {
            log.error("put into cache:", ex);
        }
    }

    @Override
    public Object get(Object key) {
        try {
            Object result = opsForHash.get(redisKey, key.toString());
            if (result == TTLRedisSerializer.EXPIRED_OBJ) {
                opsForHash.delete(redisKey, key.toString());
                result = null;
            }
            return result;
        } catch (Exception ex) {

            log.error("get from cache:", ex);
            return null;
        }
    }

    @Override
    public void clear() {
        redisTemplate.delete(redisKey);
        if (cacheFactory != null)
            cacheFactory.remove(this);
    }

    @Override
    public void remove(Object key) {
        opsForHash.delete(redisKey, key.toString());
    }

    @Override
    public long size() {
        return opsForHash.size(redisKey);
    }

    @Override
    public List<Object> getKeys(int top) {
        Set<String> keys = opsForHash.keys(redisKey);
        int i = 0;
        List<Object> resultKeys = new ArrayList<>();
        for (String key : keys) {
            i++;
            resultKeys.add(key);
            if (i >= top) break;
        }
        return resultKeys;
    }

    @Override
    public Map<Object, Object> get(int top) {
        Map<String, Object> datas = opsForHash.entries(redisKey);
        Set<Map.Entry<String, Object>> entries = datas.entrySet();
        Map<Object, Object> resultMap = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, Object> entry : entries) {
            i++;
            resultMap.put(entry.getKey(), entry.getValue());
            if (i >= top) break;
        }
        return resultMap;
    }

    @Override
    public boolean containsKey(Object key) {
        Object result = opsForHash.get(redisKey, key.toString());
        if (result == TTLRedisSerializer.EXPIRED_OBJ) {
            opsForHash.delete(redisKey, key.toString());
            return false;
        }
        return true;
    }
}
