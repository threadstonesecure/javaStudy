package com.yuntai.redisson;

import io.netty.util.internal.PlatformDependent;
import org.redisson.Redisson;
import org.redisson.api.RRemoteService;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.connection.ConnectionManager;
import org.redisson.remote.ResponseEntry;

import java.util.concurrent.ConcurrentMap;

public class RemoteServiceHelper {

    protected static final ConcurrentMap<String, ResponseEntry> responses = PlatformDependent.newConcurrentHashMap();

    public static RRemoteService getRemoteService(RedissonClient redissonClient, String name) {
         // return redissonClient.getRemoteService(name);

        Redisson redisson = (Redisson) redissonClient;
        ConnectionManager connectionManager = redisson.getConnectionManager();
        Codec codec = connectionManager.getCodec();
        String executorId = connectionManager.getId().toString();
        return new RedissonRemoteService(codec, redissonClient, name, connectionManager.getCommandExecutor(), executorId, responses);
    }
}
