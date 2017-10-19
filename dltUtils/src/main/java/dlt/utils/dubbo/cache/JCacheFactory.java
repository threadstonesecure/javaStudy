package dlt.utils.dubbo.cache;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import dlt.utils.dubbo.URLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static dlt.utils.dubbo.cache.Constants.*;

/**
 * 由于dubbo的JCacheFactory、JCache是基于cache-api 0.40实现的,
 * 而ehcache-jcache等是基于cache-api 1.0.0实现的
 * 故重写JCacheFactory、JCache
 * <p>
 * Created by denglt on 16/10/21.
 */
public class JCacheFactory extends AbstractCacheFactory {
    private static Log log = LogFactory.getLog(JCacheFactory.class);


    private ConcurrentHashMap<String, CachingProvider> providers = new ConcurrentHashMap<>();


    @Override
    protected com.alibaba.dubbo.cache.Cache createCache(URL url) {
        RpcContext rpcContext = RpcContext.getContext();
        boolean isProvider = rpcContext.isProviderSide();
        String providerClassName = URLUtils.getParameter(url, PROVIDER_KEY);
        providerClassName = providerClassName == null ? "null" : providerClassName;
        CachingProvider cachingProvider = providers.get(providerClassName);
        if (cachingProvider == null) {
            cachingProvider = providerClassName.equals("null") ? Caching.getCachingProvider() : Caching.getCachingProvider(providerClassName);
            providers.put(cachingProvider.getClass().getName(), cachingProvider);
            providers.putIfAbsent("null", cachingProvider);
        }

        CacheManager cacheManager = cachingProvider.getCacheManager();
        MutableConfiguration<Object, Object> cacheConfig = new MutableConfiguration<>();

        if (isProvider) {
            cacheConfig.setStoreByValue(false);
        } else {
            String readOnly = URLUtils.getParameter(url, READONLY_KEY, "false");
            if (readOnly.toLowerCase().equals("true")) {
                cacheConfig.setStoreByValue(false);
            }
        }
        String expiry = URLUtils.getParameter(url, EXPIRY_KEY);
        if (!StringUtils.isEmpty(expiry)) {
            try {
                cacheConfig.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, Long.parseLong(expiry))));
            } catch (NumberFormatException ex) {
                log.error("cache.expiry is not number", ex);
                log.warn("no expire cache:" + url.toFullString());
            }
        }

        cacheConfig.setTypes(Object.class, Object.class);
        String cacheKey = URLUtils.toKeyStr(url);
        Cache<Object, Object> cache = cacheManager.getCache(cacheKey, Object.class, Object.class);
        if (cache == null)
            cache = cacheManager.createCache(cacheKey, cacheConfig);
        return new JCache(cache, this);
    }
}
