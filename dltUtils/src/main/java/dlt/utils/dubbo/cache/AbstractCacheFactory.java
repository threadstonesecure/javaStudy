package dlt.utils.dubbo.cache;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.CacheFactory;
import com.alibaba.dubbo.common.URL;
import dlt.utils.dubbo.URLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by denglt on 2016/11/10.
 */
public abstract class AbstractCacheFactory implements CacheFactory {

    private static Log log = LogFactory.getLog(AbstractCacheFactory.class);
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    public synchronized Cache getCache(URL url) {
        try {
            String key = URLUtils.toKeyStr(url);
            Cache cache = caches.get(key);
            if (cache == null) {
                cache = createCache(url);
                if (cache != null)
                    caches.put(key, createCache(url));
            }
            return cache;
        } catch (Exception ex) {
            log.error("get cache fail :", ex);
            return null;
        }
    }

    protected abstract Cache createCache(URL url);

    public void remove(Cache cache) {
        List<String> cacheKeys = new ArrayList<>();
        Set<Map.Entry<String, Cache>> entries = caches.entrySet();
        for (Map.Entry<String, Cache> entry : entries) {
            if (entry.getValue().equals(cache)) {
                cacheKeys.add(entry.getKey());
            }
        }

        for (String cacheKey : cacheKeys) {
            caches.remove(cacheKey);
        }
    }

    public Map<String, Cache> getCaches() {
        return caches;
    }
}
