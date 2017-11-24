package dlt.study.redis.redisson;

import com.beust.jcommander.internal.Lists;
import dlt.domain.model.User;
import dlt.study.spring.JUnit4Spring;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.client.protocol.RedisStrictCommand;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.command.CommandSyncService;
import org.redisson.connection.ConnectionManager;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RedissonEvalDemo extends JUnit4Spring {

    @Resource
    RedissonClient redissonClient;


    @Test
    public void scprit() {
        RScript script = redissonClient.getScript();
        String eval_script = "local oldV = redis.call('get',KEYS[1]); redis.call('set','vvvvvv' ,ARGV[2]);" +
                "redis.call('set',KEYS[1],ARGV[1]); " +
                "return oldV;";
        User user = new User();
        user.setUserName("dengltdddd by scprit");
        User oldUser = script.eval(RScript.Mode.READ_ONLY,
                JsonJacksonCodec.INSTANCE,
                eval_script, RScript.ReturnType.VALUE,
                Lists.newArrayList("user"),
                user,
                "dengltzyy"
        );
        System.out.println("old user -> " + oldUser);
        String sha = script.scriptLoad(eval_script);
        System.out.println("script sha -> " + sha);
        //script.scriptExists()
    }

    @Test
    public void getset() throws Exception {
        ConnectionManager connectionManager = ((Redisson) redissonClient).getConnectionManager();
        CommandSyncService commandExecutor = connectionManager.getCommandExecutor();
        User user = new User();
        user.setUserName("dengltdddd");
        String eval_script = "local oldV = redis.call('get',KEYS[1]); redis.call('set','vvvvvv' ,ARGV[2]);" +
                "redis.call('set',KEYS[1],ARGV[1]); " +
                "return oldV;";
/*        User o = commandExecutor.evalWrite("user",
                JsonJacksonCodec.INSTANCE,
                RedisCommands.EVAL_OBJECT,
                eval_script, Lists.newArrayList("user"),
                JsonJacksonCodec.INSTANCE.getValueEncoder().encode(user));
        System.out.println("old user -> " + o);
                */
        RFuture<User> objectRFuture = commandExecutor.evalWriteAsync("user",
                JsonJacksonCodec.INSTANCE,
                RedisCommands.EVAL_OBJECT,
                eval_script, Lists.newArrayList("user"),
                JsonJacksonCodec.INSTANCE.getValueEncoder().encode(user), "dengltzyy");
        System.out.println("old user -> " + objectRFuture.get());
    }


    /**
     * 分析数据在redis的存储结构
     *
     * @throws Exception
     */
    @Test
    public void struck() throws Exception {
        RBlockingQueue<User> myqueue = redissonClient.getBlockingQueue("myDelayedQueueStuck", JsonJacksonCodec.INSTANCE);
        RDelayedQueue<User> delayedQueue = redissonClient.getDelayedQueue(myqueue);
        User user = new User();
        user.setUserName("dengltzyy");
        delayedQueue.offer(user, 3000, TimeUnit.SECONDS);

    }

    @Test
    public void struct_pack() {
        RedisStrictCommand<String> MY_EVAL_STRING = new RedisStrictCommand<String>("EVAL", new Decoder<String>() {
            @Override
            public String decode(ByteBuf buf, State state) throws IOException {
                String status = buf.readBytes(buf.bytesBefore((byte) '\r')).toString(CharsetUtil.UTF_8);
                buf.skipBytes(2);
                return status;
            }
        });
        String eval_script = "local value = struct.pack('dLc0', tonumber(ARGV[2]), string.len(ARGV[1]), ARGV[1]);"  //  "dLc0" 数值 和 不定长度字符串
                + "redis.call('set',KEYS[1],value); "
                + "return value ;";
        ConnectionManager connectionManager = ((Redisson) redissonClient).getConnectionManager();
        CommandSyncService commandExecutor = connectionManager.getCommandExecutor();
        String v = commandExecutor.evalWrite(null, null,
                RedisCommands.EVAL_STRING_DATA, // 在netty中handler进行解码
                eval_script,
                Lists.newArrayList("structcontent"), "dengltzyy", 1000);
        System.out.println(v);

        System.out.println("使用script方法");
        RScript script = redissonClient.getScript();
        v = script.eval(RScript.Mode.READ_ONLY,
                StringCodec.INSTANCE,
                eval_script,
                RScript.ReturnType.VALUE,
                Lists.newArrayList("structcontent"),
                "dengltzyy", 1000);
        System.out.println(v);
    }

    @Test
    public void struct_uppack() {
        ConnectionManager connectionManager = ((Redisson) redissonClient).getConnectionManager();
        CommandSyncService commandExecutor = connectionManager.getCommandExecutor();
        String v = commandExecutor.evalWrite(null, null,
                RedisCommands.EVAL_STRING_DATA,
                "local value = redis.call('get',KEYS[1]);" +
                        "if value ~= false then " +
                        "  local i,v = struct.unpack('dLc0',value);" +
                        "  return i .. v ;" +
                        "end;" +
                        "return nil;",
                Lists.newArrayList("structcontent"));
        System.out.println(v);

    }

    @Test
    public void pakcAndUnpack() {
        ConnectionManager connectionManager = ((Redisson) redissonClient).getConnectionManager();
        CommandSyncService commandExecutor = connectionManager.getCommandExecutor();
        String keyName = "structcontent";
        String v = commandExecutor.evalWrite(null, null,
                RedisCommands.EVAL_STRING_DATA,
                "local value = struct.pack('Lc0Lc0', string.len(ARGV[1]), ARGV[1], string.len(ARGV[2]), ARGV[2]);"  //  'Lc0Lc0' 两个不定长度字符串
                        + "redis.call('set',KEYS[1],value); "
                        + "return value;",
                Lists.newArrayList(keyName), "denglt", "zyy");
        System.out.println("set " + keyName + " -> " + v);

        v = commandExecutor.evalWrite(null, null,
                RedisCommands.EVAL_STRING_DATA,
                "local value = redis.call('get',KEYS[1]);" +
                        "if value ~= false then " +
                        "  local v1,v2 = struct.unpack('Lc0Lc0',value);" +
                        "  return v1 .. '->' .. v2 ;" +
                        "end;" +
                        "return nil;",
                Lists.newArrayList(keyName));
        System.out.println("read " + keyName + " -> " + v);

    }

}
