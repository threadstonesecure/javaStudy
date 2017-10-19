package dlt.study.jcache;

import org.ehcache.jcache.JCacheManager;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import javax.management.MBeanServer;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created by denglt on 16/10/21.
 */
public class JCacheDemo {

    @Test
    public  void cache() throws Exception {
        //CachingProvider cachingProvider =  Caching.getCachingProvider("org.ehcache.jcache.JCacheCachingProvider2");
        CachingProvider cachingProvider =  Caching.getCachingProvider(); // -Djavax.cache.spi.CachingProvider="xxxx"  or  ServiceLoader.load(CachingProvider.class)
        System.out.println(cachingProvider);

        CacheManager cacheManager = cachingProvider.getCacheManager();
        if (cacheManager instanceof JCacheManager){
            JCacheManager jCacheManager = (JCacheManager)cacheManager;
            Field field = JCacheManager.class.getDeclaredField("cacheManager");
            field.setAccessible(true);
            Object manager = field.get(jCacheManager);
            System.out.println(manager.getClass());
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
           // ManagementService.registerMBeans((net.sf.ehcache.CacheManager)manager, mBeanServer, true, false, false, false);
        }

        MutableConfiguration<String, MyObject> jcacheConfig = new MutableConfiguration<>();
        jcacheConfig.setTypes(String.class, MyObject.class);
        jcacheConfig.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR));
        jcacheConfig.setStoreByValue(false); // 如果数据不被修改设置为false,可以提高性能;为true时,对象必须为Serializable

        jcacheConfig.setManagementEnabled(true); // 启动JMX监控  CacheStatistics
        jcacheConfig.setStatisticsEnabled(true); // 启动JMX监控  CacheConfiguration
        Cache<String,MyObject> cache = cacheManager.createCache("mycache",jcacheConfig);
        String key = UUID.randomUUID().toString();
        String key2 = UUID.randomUUID().toString();
        cache.put(key, new MyObject(key));
        cache.get(key);
        MyObject inserted = cache.get(key);
        System.out.println(inserted);
        inserted.setName("denglt");
        Thread.sleep(30000);
        System.out.println(cache.get(key));
        cache.put(key2, new MyObject(key2));
        Thread.sleep(40000);
        System.out.println(cache.get(key));
        System.out.println(cache.get(key2));
        Thread.sleep(20100);
        System.out.println(cache.get(key2));
        cache.clear();
        System.out.println(cache.get(key2));
        cache.put(key2, new MyObject(key2));
        System.out.println(cache.get(key2));
        System.in.read();
    }

    @Test
    public void putNull(){
        CachingProvider cachingProvider =  Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        MutableConfiguration<String, Object> jcacheConfig = new MutableConfiguration<>();
        jcacheConfig.setTypes(String.class, Object.class);
        jcacheConfig.setStoreByValue(true);
        Cache<String,Object> cache = cacheManager.createCache("mycache",jcacheConfig);
        NullValue nullValue = NullValue.INSTANCE;
        System.out.println(nullValue);
        cache.put("1",nullValue);
        Object nullValue1 = cache.get("1");
        System.out.println(nullValue1);
        System.out.println("(nullValue ==  nullValue1) = " + (nullValue == nullValue1));
        System.out.println("nullValue.equals(nullValue1)="+ nullValue.equals(nullValue1));
        MyObject obj1 =  new MyObject("1");
        System.out.println(obj1);
        cache.put("2", obj1);
        System.out.println(cache.get("2"));
    }
}

class MyObject implements Serializable {
    private String name;
    public MyObject(String name){
        this.name = name;
    }


    @Override
    public String toString() {
        return "MyObject{" +
                "name='" + name + '\'' +
                '}' + super.toString();
    }

    public void setName(String name){
        this.name = name;
    }
}





