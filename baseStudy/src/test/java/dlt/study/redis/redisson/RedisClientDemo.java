package dlt.study.redis.redisson;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisClientConfig;
import org.redisson.client.RedisConnection;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.codec.JsonJacksonCodec;

public class RedisClientDemo {


    /**
     *   Low level Redis client
     *
     *   RedisClient :包装了netty client的实现 ( like HdpClient)
     */
    @Test
    public void  redisClient(){

        // Use shared EventLoopGroup only if multiple clients are used
        EventLoopGroup group = new NioEventLoopGroup();

        RedisClientConfig config = new RedisClientConfig();
        config.setAddress("redis://localhost:6379") // or rediss:// for ssl connection
                // .setPassword("myPassword")
                //.setConnectTimeout()
                //.setCommandTimeout()
                .setDatabase(0)
                .setClientName("dltClient")
                .setSocketChannelClass(NioSocketChannel.class)
                .setGroup(group);

        RedisClient client = RedisClient.create(config);
        RedisConnection conn = client.connect();

        conn.sync(StringCodec.INSTANCE, RedisCommands.SET, "test", 0);
        // or
        conn.async(JsonJacksonCodec.INSTANCE, RedisCommands.SET, "test2",1);

        conn.closeAsync().awaitUninterruptibly();


    }
}
