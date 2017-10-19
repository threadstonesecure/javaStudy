package dlt.study.jcache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

/**
 * Created by denglt on 16/10/24.
 */
public class JCacheDemo2 {
    public static void main(String[] args) throws Exception {
        CachingProvider cachingProvider =  Caching.getCachingProvider(); // -Djavax.cache.spi.CachingProvider="xxxx"  or  ServiceLoader.load(CachingProvider.class)
        System.out.println(cachingProvider);
        CachingProvider cachingProvider2 = Caching.getCachingProvider("org.ehcache.jcache.JCacheCachingProvider");
        System.out.println(cachingProvider == cachingProvider2);
        CacheManager cacheManager = cachingProvider.getCacheManager();
        CacheManager cacheManager2  =  cachingProvider2.getCacheManager();
        System.out.println(cacheManager == cacheManager2);
        MutableConfiguration<String, MyObject> jcacheConfig = new MutableConfiguration<>();
        jcacheConfig.setTypes(String.class, MyObject.class);
        Cache<String,MyObject> cache = cacheManager.createCache("mycache", jcacheConfig);
        Cache<String,MyObject> cache2 = cacheManager2.getCache("mycache", String.class, MyObject.class);
        System.out.println( cache ==cache2);
        cache.put("1",new MyObject("1"));
        cache.put("2",new MyObject("1"));
        cache.put("3",new MyObject("1"));
        cache.put("4",new MyObject("1"));
        System.in.read();
    }
}
