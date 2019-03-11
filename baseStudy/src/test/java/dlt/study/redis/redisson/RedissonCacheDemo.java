package dlt.study.redis.redisson;

import dlt.study.log4j.Log;
import dlt.study.spring.JUnit4Spring;
import org.junit.Test;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/*
  Redisson 支持 spring cache :  RedissonSpringCacheManager  ( like RedisCacheManager)
 */
public class RedissonCacheDemo extends JUnit4Spring {


    @Resource
    RedissonClient redissonClient;

    /**
     * Map eviction
     * 这个缓存通过Hash存储数据，并使用Lua脚本来实现（性能比我写的RedisCache好），同时能够设置IdleTime
     * 可以指定每个entry的失效时间
     */
    @Test
    public void mapCache() throws Exception {
        RMapCache<String, String> myMapCache = redissonClient.getMapCache("myMapCache", StringCodec.INSTANCE);
        // myMapCache.setMaxSize();
        //myMapCache.addListener()
        myMapCache.put("one", "denglt", 10, TimeUnit.SECONDS);
        myMapCache.put("two", "zyy", 100, TimeUnit.SECONDS, 2, TimeUnit.SECONDS);

        while (1 == 1) {
            String one = myMapCache.get("one");
            Log.info("one -> " + one);
            String two = myMapCache.get("two");
            Log.info("two -> " + two);
            Thread.sleep(5000);
        }
    }

    /**
     * Map local cache
     * 把redis的数据cache在本地(可以设置本地cache失效策略)
     * @throws Exception
     */
    @Test
    public void localCachedMap() throws Exception {
        LocalCachedMapOptions options = LocalCachedMapOptions.<String, String>defaults()
                // LFU, LRU, SOFT, WEAK and NONE policies are available
                .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.LFU) //SOFT、WEAF
                // If cache size is 0 then local cache is unbounded.
                .cacheSize(1000)
                // if value is `ON_CHANGE`, `ON_CHANGE_WITH_CLEAR_ON_RECONNECT` or `ON_CHANGE_WITH_LOAD_ON_RECONNECT`
                // corresponding map entry is removed from cache across all RLocalCachedMap instances
                // during every invalidation message sent on each map entry update/remove operation
                .invalidationPolicy(LocalCachedMapOptions.InvalidationPolicy.ON_CHANGE_WITH_CLEAR_ON_RECONNECT)
                //.invalidateEntryOnChange()
                // time to live for each map entry in local cache
                .timeToLive(10000)
                // or
                .timeToLive(10, TimeUnit.SECONDS)
                // max idle time for each map entry in local cache
                .maxIdle(10000)
                // or
                .maxIdle(10, TimeUnit.SECONDS);
        RLocalCachedMap<String, String> localCachedMap = redissonClient.getLocalCachedMap("localCachedMap", StringCodec.INSTANCE, options);
        localCachedMap.put("name", "denglt");
        String name = localCachedMap.get("name");
        Log.info("name ->" + name);
        Thread.sleep(5000);
        name = localCachedMap.get("name");
        Log.info("name ->" + name);

        Thread.sleep(11000);
        name = localCachedMap.get("name");
        Log.info("name ->" + name);
        localCachedMap.destroy();
    }

    @Test
    public  void listMultimapCache(){
        RListMultimapCache<String, String> myListMultimapCache = redissonClient.getListMultimapCache("myListMultimapCache", StringCodec.INSTANCE);
        //myListMultimapCache.expireKey()  boolean expireKey(K key, long timeToLive, TimeUnit timeUnit);
    }

    public void setCache(){
        RSetCache<String> mySetCache = redissonClient.getSetCache("mySetCache", StringCodec.INSTANCE);
        // mySetCache.add(); boolean add(V value, long ttl, TimeUnit unit);
        //  mySetCache.iterator();
    }
    /**
     * Map local cache for expiring entries
     *
     * Map eviction 与 Map local cache 的结合
     *
     * This feature available only in Redisson PRO edition.
     */

     public void localCachedMapCache(){
     }


}
