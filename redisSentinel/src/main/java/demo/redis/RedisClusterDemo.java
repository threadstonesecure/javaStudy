package demo.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 120.26.110.142 ： 6868 caozg @2a!!DpR 10.117.47.166 6381 6382 6383 hsyt@redis
 * (内网：10.117.47.166 端口 6868 hsyun 密码 H&D8#LA1 )
 *  Created by denglt on 2016/2/22.
 */
public class RedisClusterDemo {
	public static void main(String[] args) throws IOException {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("120.26.110.142", 6381));
		nodes.add(new HostAndPort("120.26.110.142", 6382));
		nodes.add(new HostAndPort("120.26.110.142", 6383));
		JedisPoolConfig config = new JedisPoolConfig();
		config.setBlockWhenExhausted(false);
		config.setMaxWaitMillis(-1);
		config.setMinEvictableIdleTimeMillis(1800000);
		config.setMinIdle(6);
		config.setMaxTotal(8);
		config.setTestOnBorrow(false);
		config.setTestWhileIdle(false);
		config.setTimeBetweenEvictionRunsMillis(-1);

		JedisCluster jedisCluster = new JedisCluster(nodes, config);

		// jedisCluster.auth("hsyt@redis");  cluster 不支持
		String value = jedisCluster.get("foo");
		System.out.println("foo =" + value);
		jedisCluster.set("name", "denglt2");
		/*
		 * for (int i=0;i<100;i++){ jedisCluster.set("foo"+i,"a"+i); }
		 */

		Map<String, JedisPool> poolMap = jedisCluster.getClusterNodes();
		Set<Map.Entry<String, JedisPool>> entries = poolMap.entrySet();
		for (Map.Entry<String, JedisPool> entry : entries) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		System.in.read();
	}
}
