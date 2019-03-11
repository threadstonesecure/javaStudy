package dlt.study.redis.redisson;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import dlt.domain.model.User;
import dlt.study.log4j.Log;
import dlt.study.spring.JUnit4Spring;
import dlt.utils.ByteUtils;
import dlt.utils.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.PatternMessageListener;
import org.redisson.api.listener.StatusListener;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedissonDemo extends JUnit4Spring {

    @Resource
    RedissonClient redissonClient;

    @Test
    public void configCluster() throws Exception {
        Config config = new Config();
        config.setUseLinuxNativeEpoll(false);
        //config.setCodec()
        config.useClusterServers()
                .setPassword("")
                //可以用"rediss://"来启用SSL连接
                //.addNodeAddress("redis://127.0.0.1:7181", "redis://127.0.0.1:7182");
                .addNodeAddress("redis://127.0.0.1:6379");


/*        config.useSentinelServers().addSentinelAddress()
        config.useCustomServers();
        config.useMasterSlaveServers();
        config.useSingleServer();*/

        System.out.println(config.toJSON());
        System.out.println(config.toYAML());
        System.out.println(config.toString());
        RedissonClient redisson = Redisson.create(config);

    }

    @Test
    public void configSingleServer() throws Exception {
        Config config = new Config();
        //config.setLockWatchdogTimeout()
        config.setUseLinuxNativeEpoll(false);
        config.useSingleServer()
                //.setPassword("")
                .setAddress("redis://127.0.0.1:6379");
        System.out.println(config.toJSON());
        System.out.println(config.toYAML());
        System.out.println(config.toString());
        RedissonClient redisson = Redisson.create(config);

    }

    @Test
    public void info() {
      /*  CodecProvider codecProvider = redissonClient.getCodecProvider();
        System.out.println(codecProvider);
*/
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
    public void atomicLong() throws Exception {
        System.out.println(this.redissonClient);
        RAtomicLong longObject = redissonClient.getAtomicLong("myAtomicLong");
        longObject.set(3);
        boolean b = longObject.compareAndSet(3, 401);  // 同步
        System.out.println(b);

        RFuture<Boolean> booleanRFuture = longObject.compareAndSetAsync(401, 402); // 异步
        booleanRFuture.addListener(future -> {
            if (future.isSuccess()) {
                // 取得结果
                Boolean result = future.getNow();
                // ...
            } else {
                // 对发生错误的处理
                Throwable cause = future.cause();
            }
        });
        //booleanRFuture.handle()  // 1.8 后使用

        System.out.println(booleanRFuture.get());


    }

    @Test
    public void createReactive() {
        RedissonReactiveClient reactive = Redisson.createReactive(this.redissonClient.getConfig());
        RAtomicLongReactive longObject = reactive.getAtomicLong("myLong");
        Publisher<Boolean> booleanPublisher = longObject.compareAndSet(401, 402);

        Publisher<Long> longPublisher = longObject.get();
        // longPublisher.subscribe();

    }

    @Test
    public void map() {
        RMap<Object, Object> map = redissonClient.getMap("hdp:config:token");
        User user = new User();
        user.setAge(100);
        user.setUserName("denglt");
        //map.put(null,user);  map key can't be null
        map.put(1, user);
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
        /*map.fastPut();
        map.fastPutIfAbsent();
        map.fastRemove();*/
        System.out.println(map.get("name"));
        System.out.println(map.get("age"));
        //map.getLock()
        // map.getReadWriteLock()
    }


    /**
     * Multimap的多值实现，使用指定的Multimap名称 + Map 的键值 作为redis里的对象(List or Set)的key.
     * eg: {multimap}:Baaymosk0bNsX3suSnn9bQ
     * 1) {} 里为Multimap的名称
     * 2） : 后为 Hash.hashToBase64 处理Map Key的结果
     */
    @Test
    public void listMultimap() {

        RListMultimap<String, String> multimap = redissonClient.getListMultimap("multimap");
        multimap.put("name", "denglt1");
        multimap.put("name", "denglt2");
        multimap.put("name", "denglt3");
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

        List<User> users = Lists.newArrayList(user, user);
        byteBuf = valueEncoder.encode(users);
        System.out.println(new String(ByteBufUtil.getBytes(byteBuf)));//["java.util.ArrayList",[{"@class":"dlt.domain.model.User","age":100,"income":0.0,"married":false,"userName":"denglt"},{"@class":"dlt.domain.model.User","age":100,"income":0.0,"married":false,"userName":"denglt"}]]

        Decoder<Object> valueDecoder = jsonCodec.getValueDecoder();
        Object v = valueDecoder.decode(byteBuf, null);
        System.out.println(v);
        System.out.println(v.getClass());
    }

    public void list() {
        RList<String> myList = redissonClient.getList("myList", StringCodec.INSTANCE);
        //myList.remove()

    }


    /**
     * pub/sub
     *
     * @throws Exception
     */
    @Test
    public void topic() throws Exception {
        RTopic<String> mytopic = redissonClient.getTopic("redisson_delay_queue_channel:{myDelayeQueue}", StringCodec.INSTANCE);
        mytopic.addListener(new StatusListener() {  // 执行 SUBSCRIBE
            @Override
            public void onSubscribe(String channel) {
                Log.info("onSubscribe ->" + channel);
            }

            @Override
            public void onUnsubscribe(String channel) {
                Log.info("onUnsubscribe ->" + channel);
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

        RPatternTopic<String> patternTopic = redissonClient.getPatternTopic("topic*");
        // patternTopic.addListener() PatternStatusListener
        int listenerId = patternTopic.addListener(new PatternMessageListener<String>() {
            @Override
            public void onMessage(String pattern, String channel, String msg) {
            }
        });
    }


    public void t() {
        //redissonClient.getScoredSortedSet()
        //redissonClient.getSortedSet()
    }

    @Test
    public void bucket() {
        RBucket<String> mybucket = redissonClient.getBucket("mybucket", StringCodec.INSTANCE);
        mybucket.set("denglt");
        String s = mybucket.get();
        //mybucket.compareAndSet()
        System.out.println("read from redis ->" + s);

        RBuckets buckets = redissonClient.getBuckets(StringCodec.INSTANCE);
        List<RBucket<String>> rBuckets = buckets.find("mybu"); // 多key操作
        rBuckets.forEach(r -> System.out.println(r.get()));
    }

    @Test
    public void binaryStream() throws Exception {
        RBinaryStream myStream = redissonClient.getBinaryStream("myStream");


        OutputStream outputStream = myStream.getOutputStream();
        outputStream.write(" -> zyy".getBytes());
        outputStream.write(" -> dzy".getBytes());
        outputStream.write(" -> dwx".getBytes());

        byte[] bytes = myStream.get();
        System.out.println("read from redis -> " + new String(bytes));


    }

    //Hyperloglog基数统计
    @Test
    public void hyperLogLog() {
        RHyperLogLog<String> hyperLogLog = redissonClient.getHyperLogLog("hyperLogLog", StringCodec.INSTANCE);
        hyperLogLog.add("denglt");
        hyperLogLog.add("zyy");
        hyperLogLog.add("dzy");
        hyperLogLog.add("dwx");
        System.out.println(hyperLogLog.count());
        System.out.println(hyperLogLog.countWith("hyperLogLog"));
        //hyperLogLog.mergeWith();  合并 HyperLogLog

    }

    @Test
    public void bloomFilter() {
        RBloomFilter<String> myBloomFilter = redissonClient.getBloomFilter("myBloomFilter", StringCodec.INSTANCE);
        // redissonClient.getB
        myBloomFilter.delete();
        myBloomFilter.tryInit(10000, 0.03);
        for (int i = 0; i < 100; i++) {
            myBloomFilter.add("denglt" + i);
            myBloomFilter.add("zyy" + i);
        }
        System.out.println(myBloomFilter.count());
        // for (int i = 0; i < 10; i++) {
        System.out.println(myBloomFilter.contains("denglt5555"));
        System.out.println(myBloomFilter.contains("zyy555"));
        System.out.println(myBloomFilter.contains("zyy55"));
        // }
    }

    /**
     * Redis 的偏移offset是从左到右计算的（与jdk的BitSet是反对的）
     */
    @Test
    public void bitSet() {
        RBitSet myBitSet = redissonClient.getBitSet("myBitSet");
        myBitSet.delete();
        myBitSet.set(1);
        myBitSet.clear();
        //myBitSet.set(4);
        //myBitSet.asBitSet();
        //myBitSet.and();
        byte[] bytes = myBitSet.toByteArray();
        System.out.println(ByteUtils.byteToBit(bytes[0]));
        BitSet bitSet1 = myBitSet.asBitSet();// 注意与toByteArray的字节顺序
        System.out.println(ByteUtils.byteToBit(bitSet1.toByteArray()[0]));

        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bytes = bitSet.toByteArray();
        System.out.println(ByteUtils.byteToBit(bytes[0]));
    }

    @Test
    public void sortedSet() {
        RSortedSet<String> mySortedSet = redissonClient.getSortedSet("mySortedSet", StringCodec.INSTANCE);
/*        mySortedSet.trySetComparator()
        mySortedSet.add()  // 会加个分布式lock*/
    }

    @Test
    public void longAdder() {
        RLongAdder atomicLong = redissonClient.getLongAdder("myLongAdder");
        atomicLong.add(12);
        atomicLong.increment();
        atomicLong.decrement();
        System.out.println(atomicLong.sum());
    }

}
