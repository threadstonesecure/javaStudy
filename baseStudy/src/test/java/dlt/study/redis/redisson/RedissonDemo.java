package dlt.study.redis.redisson;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dlt.domain.model.User;
import dlt.study.log4j.Log;
import dlt.study.spring.JUnit4Spring;
import dlt.utils.ConfigUtils;
import dlt.utils.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.internal.PlatformDependent;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.StatusListener;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.redisson.codec.CodecProvider;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public class RedissonDemo extends JUnit4Spring {

    @Resource
    RedissonClient redissonClient;

    @Test
    public void config() throws Exception {
        Config config = new Config();
        config.setUseLinuxNativeEpoll(true);
        config.useClusterServers()
                //可以用"rediss://"来启用SSL连接
                .addNodeAddress("redis://127.0.0.1:7181", "redis://127.0.0.1:7182");

/*        config.useSentinelServers().addSentinelAddress()
        config.useCustomServers();
        config.useMasterSlaveServers();
        config.useSingleServer();*/
        System.out.println(config.toJSON());
        System.out.println(config.toYAML());
        System.out.println(config.toString());

    }

    @Test
    public void info() {
        CodecProvider codecProvider = redissonClient.getCodecProvider();
        System.out.println(codecProvider);

        Codec codec = redissonClient.getConfig().getCodec(); // 默认 Codec (JsonJacksonCodec)
        System.out.println(codec);

        Redisson redisson = (Redisson) redissonClient;

/*
        ExecutorService executor = redissonClient.getConfig().getExecutor();
        System.out.println("EventLoopGroup - > " + eventLoopGroup);
        System.out.println("ExecutorService -> " + executor);
*/



    }



    @Test
    public void getAtomicLong() throws Exception {
        System.out.println(this.redissonClient);
        RAtomicLong longObject = redissonClient.getAtomicLong("myLong");
        // longObject.set(3);
        boolean b = longObject.compareAndSet(3, 401);  // 同步
        System.out.println(b);

        RFuture<Boolean> booleanRFuture = longObject.compareAndSetAsync(401, 402); // 异步
        booleanRFuture.addListener(new FutureListener<Boolean>() {
            @Override
            public void operationComplete(Future<Boolean> future) throws Exception {
                if (future.isSuccess()) {
                    // 取得结果
                    Boolean result = future.getNow();
                    // ...
                } else {
                    // 对发生错误的处理
                    Throwable cause = future.cause();
                }
            }
        });
        //booleanRFuture.handle()  // 1.8 后使用

        System.out.println(booleanRFuture.get());
    }

    @Test
    public void map() {
        RMap<Object, Object> map = redissonClient.getMap("hdp:config:token");
        User user = new User();
        user.setAge(100);
        user.setUserName("denglt");
        //map.put(null,user);  map key can't be null
        map.put(1,user);
        System.out.println(map.getClass());
        Object o = map.get(1200L);
        System.out.println(o);
        System.out.println(o.getClass());
        map.get(1200L);
        Object o2 = map.get(1200L);
        System.out.println(o == o2);

    }

    @Test
    public void map2() {
        RMap<String, String> map = redissonClient.getMap("hdp:config:token2", StringCodec.INSTANCE);
        map.expire(60, TimeUnit.SECONDS); // 整个map 存活60s
        map.put("name", "denglt");
        map.put("age", "10");
        System.out.println(map.get("name"));
        System.out.println(map.get("age"));
    }


    /**
     * Multimap的多值实现，使用指定的Multimap名称 + Map 的键值 作为redis里的对象(List or Set)的key.
     * eg: {multimap}:Baaymosk0bNsX3suSnn9bQ
     *    1) {} 里为Multimap的名称
     *    2） : 后为 Hash.hashToBase64 处理Map Key的结果
     */
    @Test
    public void listMultimap() {

        RListMultimap<String, String> multimap = redissonClient.getListMultimap("multimap");
        multimap.put("name","denglt1");
        multimap.put("name","denglt2");
        multimap.put("name","denglt3");
        System.out.println(multimap.get("name").get(0).getClass());

    }

    @Test
    public void listMultimap2() {
        RListMultimap<String, String> multimap = redissonClient.getListMultimap("multimap", StringCodec.INSTANCE);
        multimap.put("name", "denglt4");
        multimap.put("name", "denglt5");
        multimap.put("age", "100");
        multimap.put("age", "200");

        System.out.println(multimap.get("name").get(0).getClass());

    }

    @Test
    public void jackCodec() throws Exception {
        System.out.println(JSON.toJSONString(100L));
        System.out.println(JsonUtils.toJson(100L));
        Codec jsonCodec = new JsonJacksonCodec();
        Encoder valueEncoder = jsonCodec.getValueEncoder();
        ByteBuf byteBuf = valueEncoder.encode(100L);
        System.out.println(new String(ByteBufUtil.getBytes(byteBuf)));

        User user = new User();
        user.setAge(100);
        user.setUserName("denglt");

        byteBuf = valueEncoder.encode(user);
        System.out.println(new String(ByteBufUtil.getBytes(byteBuf))); // {"@class":"dlt.domain.model.User","age":100,"income":0.0,"married":false,"userName":"denglt"}

        List<User> users = Lists.newArrayList(user,user);
        byteBuf = valueEncoder.encode(users);
        System.out.println(new String(ByteBufUtil.getBytes(byteBuf)));//["java.util.ArrayList",[{"@class":"dlt.domain.model.User","age":100,"income":0.0,"married":false,"userName":"denglt"},{"@class":"dlt.domain.model.User","age":100,"income":0.0,"married":false,"userName":"denglt"}]]

        Decoder<Object> valueDecoder = jsonCodec.getValueDecoder();
        Object v = valueDecoder.decode(byteBuf, null);
        System.out.println(v);
        System.out.println(v.getClass());
    }

    public void list(){
        RList<String> myList = redissonClient.getList("myList",StringCodec.INSTANCE);
        //myList.remove()

    }




    /**
     * pub/sub
     * @throws Exception
     */
    @Test
    public void topic() throws Exception{
        RTopic<String> mytopic = redissonClient.getTopic("redisson_delay_queue_channel:{myDelayeQueue}", StringCodec.INSTANCE);
        mytopic.addListener(new StatusListener() {  // 执行 SUBSCRIBE
            @Override
            public void onSubscribe(String channel) {
                Log.info("onSubscribe ->" + channel);
            }

            @Override
            public void onUnsubscribe(String channel) {
                Log.info("onUnsubscribe ->" + channel );
            }
        });

        mytopic.addListener(new MessageListener<String>() {
            @Override
            public void onMessage(String channel, String msg) {
                Log.info("onMessage -> " + msg);
            }
        });

       mytopic.publish("hello world");
       System.in.read();
    }



    public void t(){
         //redissonClient.getScoredSortedSet()
        //redissonClient.getSortedSet()
    }
}
