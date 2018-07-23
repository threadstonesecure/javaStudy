package dlt.study.redis.redisson;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import dlt.domain.model.User;
import dlt.study.log4j.Log;
import dlt.study.spring.JUnit4Spring;
import org.junit.Test;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.SerializationCodec;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

public class RedissonQueueDemo extends JUnit4Spring {


    @Resource
    RedissonClient redissonClient;

    @Test
    public void queue() throws Exception {

        RQueue<String> myqueue = redissonClient.getQueue("myqueue", StringCodec.INSTANCE);
        myqueue.expire(60, TimeUnit.MINUTES);  // 注意：这句放在list中有数据了才会生效
        myqueue.add("denglt"); // rpush
        myqueue.add("zyy");
        myqueue.peek(); // LINDEX key index 实现
        // myqueue.pollLastAndOfferFirstTo("newList"); // RPOPLPUSH source destination
        while (true){
            String v = myqueue.poll(); //lpop
            if (v == null){
                Thread.sleep(100);
            }else{
                System.out.println("pool -> " + v );
            }
        }

    }


    @Test
    public void blockingQueue() throws Exception {
        RBlockingQueue<User> myqueue = redissonClient.getBlockingQueue("myDelayedQueue", JsonJacksonCodec.INSTANCE);
        new SendDataService().startAsync();
       // myqueue.add(new User("denglt"));
        while (true) {
            User value = myqueue.take(); // myqueue.poll(10,TimeUnit.SECONDS);
            Log.info("take -> " + value);
        }
        /*   异步
        RFuture<User> userRFuture = myqueue.takeAsync();
        userRFuture.getNow();
        userRFuture.get();
        */

    }


    @Test
    public void pollFromBlockingQueue(){
        RQueue<String> myqueue = redissonClient.getBlockingQueue("myDelayeQueue", StringCodec.INSTANCE);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(myqueue);
        String v = delayedQueue.poll(); // 不会blocked , 直接返回第一个数据
        System.out.println(v);
    }

    @Test
    public void delayedQueue() throws Exception {
        RBlockingQueue<String> myqueue = redissonClient.getBlockingQueue("myDelayedQueue", StringCodec.INSTANCE);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(myqueue);
        delayedQueue.offer("10",30,TimeUnit.SECONDS); // 消息到期后，消息将lpush -> List(myDelayeQueue)
        delayedQueue.offer("30",40,TimeUnit.SECONDS);
        delayedQueue.offer("denglt",60, TimeUnit.SECONDS);
        //delayedQueue.destroy(); // 这个源代码有问题，会重新建立一个新的Topic
        //myqueue.pollFromAny();
        while (true) {
            //String value = myqueue.takeLastAndOfferFirstTo("workingQueue"); // BRPOPLPUSH  (尾部)
            String value = myqueue.take(); // BLPOP (头部) // 会占用一个实际的通信channel
            Log.info("take -> " + value);
        }
    }

    public void boundedBlockingQueue(){
        RBoundedBlockingQueue<Object> myBoundedBlockingQueue = redissonClient.getBoundedBlockingQueue("myBoundedBlockingQueue");
        myBoundedBlockingQueue.trySetCapacity(100);
    }

    private class SendDataService extends AbstractExecutionThreadService{
        @Override
        protected void run() throws Exception {
            RBlockingQueue<User> myqueue = redissonClient.getBlockingQueue("myDelayedQueue", JsonJacksonCodec.INSTANCE);
            while (true){
                try {
                    myqueue.add(new User("denglt"));
                    myqueue.add(new User("denglt"));
                    myqueue.add(new User("denglt"));
                    Thread.sleep(10000);
                }catch (Exception ex){
                    Log.error(ex);
                }
            }
        }
    }




}
