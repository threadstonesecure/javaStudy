package dlt.study.redis;

import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisDemo {
	private final String ip = "172.16.108.139";
	private final int port = 6379;

	@Test
	public void setKey() {
		Jedis jedis = new Jedis(ip, port);
		//jedis.auth(password);
        jedis.select(0);
		String key = "foo";
		jedis.set(key, "bar");
		String value = jedis.get(key);

		jedis.close();
	}

	@Test
	public void jedisPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setBlockWhenExhausted(false);//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
		config.setMaxWaitMillis(-1);//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
		//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		config.setMinEvictableIdleTimeMillis(1800000);
		//最小空闲连接数, 默认0
		config.setMinIdle(0);
		//最大连接数, 默认8个
		config.setMaxTotal(8);
		//在获取连接的时候检查有效性, 默认true
		config.setTestOnBorrow(false);
		
		//在空闲时检查有效性, 默认false
		config.setTestWhileIdle(false);
		//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		config.setTimeBetweenEvictionRunsMillis(-1);
		
		JedisPool pool = new JedisPool(config, ip, port);// You
																		// can
																		// store
																		// the
																		// pool
																		// somewhere
																		// statically,
																		// it is
		Jedis jedis = pool.getResource();
		
		try { // thread-safe.

			// / ... do stuff here ... for example
			jedis.set("foo", "bar");
			jedis.set("foo".getBytes(),"bar".getBytes());
			String foobar = jedis.get("foo");
			jedis.zadd("sose", 0, "car");
			jedis.zadd("sose", 0, "bike");
			Set<String> sose = jedis.zrange("sose", 0, -1);

		} finally {
			jedis.close();
			//pool.returnResource(jedis);
		}
		// / ... when closing your application:
		pool.destroy();
	}

	@Test
	public void jedisPool2() {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), ip, port);
		try (Jedis jedis = pool.getResource();) { // 退出try会自动调用jedis.close;

			// / ... do stuff here ... for example
			jedis.set("foo", "bar");
			String foobar = jedis.get("foo");
			jedis.zadd("sose", 0, "car");
			jedis.zadd("sose", 0, "bike");
			Set<String> sose = jedis.zrange("sose", 0, -1);

		}
		// / ... when closing your application:
		pool.destroy();
	}
	
	@Test
	public void slaveOf(){
		Jedis jedis = new Jedis(ip, 6380);
		jedis.get("foo");
		jedis.slaveof(ip, port);
	}
	public void  testAutoCloseable(){
/*		try(String a = "dfasdfsadf"){
			
		}*/
	}
}
