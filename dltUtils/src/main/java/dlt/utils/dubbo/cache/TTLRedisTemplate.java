package dlt.utils.dubbo.cache;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by denglt on 2016/11/9.
 */
public class TTLRedisTemplate extends RedisTemplate<String,Object> {

    public TTLRedisTemplate(int expiry){
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        RedisSerializer<Object> ttlRedisSerializer =  new TTLRedisSerializer(expiry);
        setKeySerializer(stringSerializer);
        setValueSerializer(ttlRedisSerializer);
        setHashKeySerializer(stringSerializer);
        setHashValueSerializer(ttlRedisSerializer);
    }

    public TTLRedisTemplate(int expiry, RedisConnectionFactory connectionFactory){
        this(expiry);
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }
}
