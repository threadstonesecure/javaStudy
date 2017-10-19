package demo.redis;

import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;
import demo.utils.SpringContextUtils;

/**
 * Created by denglt on 2016/1/28.
 */
public class RedisDemo {
	public static void main(String[] args) {
		String[] paths = new String[] { "spring/*.xml" };
		SpringContextUtils.init(paths);
		RedisTemplate<String, String> redisTemplate = SpringContextUtils
				.getBean("redisTemplate");

		redisTemplate.opsForValue().set("name", "denglt");
		String name = redisTemplate.opsForValue().get("name");
		System.out.println("name = " + name);

		Pool<Jedis> pool = SpringContextUtils.getBean("jedisPool");
		try (Jedis jedis = pool.getResource()) { // 退出try会自动调用jedis.close;

			name = jedis.get("name");
			System.out.println("name = " + name);
		}
	}

}
