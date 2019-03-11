package accesshosp;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.common.URL;
import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.AccessHospitalHandler;
import dlt.utils.dubbo.cache.RedisCacheFactory;
import dlt.utils.dubbo.cache.SpringRedisHelper;
import dlt.utils.spring.SpringContextUtils;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Created by denglt on 16/10/19.
 */
public class DubboCacheDemo {

    @Test
    public void dubboCache() throws Exception {
        SpringContextUtils.init(new String[]{"dubbo-client-cache.xml", "redis.xml"});
        final AccessHospitalHandler accessHospitalHandler = SpringContextUtils.getBean("accessHospitalHandler");
        //Thread.sleep(10000);
        for (int i = 0; i < 1; i++) {
            RequestPack requestPack = new RequestPack();
            requestPack.setHosId("999999-test");
            requestPack.setCmd("业务代码:" + 10000);
            requestPack.setBody("ddddd");
            ResultPack result = accessHospitalHandler.getHospitalResult(requestPack, 30);
            System.out.println(result);
            Thread.sleep(30000);
        }
        System.in.read();
    }

    @Test
    public void redisCache() throws Exception {
        SpringContextUtils.init(new String[]{"dubbo-client.xml", "redis.xml"});
        RedisCacheFactory redisCacheFactory = new RedisCacheFactory();
        RedisConnectionFactory redisConnectionFactory = SpringContextUtils.getBean("cacheConnectionFactory");
        RedisConnectionFactory redisConnectionFactory2 = SpringRedisHelper.getRedisConnectionFactory(null);
        assert redisConnectionFactory == redisConnectionFactory2;
        redisCacheFactory.setConnectionFactory(redisConnectionFactory);
        URL url = new URL("dubbo", "127.0.0.1", 8000);
        url = url.setServiceInterface("com.dlt.cache")
                .addParameter("method", "invoke")
                .addParameter("cache.expiry", 2);
        System.out.println(url.toFullString());
        Cache cache = redisCacheFactory.getCache(url);
        cache.put("1", "denglt");
        cache.put("2", "zyy");
        Object result = cache.get("1");
        System.out.println(result);
        Thread.sleep(2000);
        result = cache.get("1");
        System.out.println(result);
    }

}
