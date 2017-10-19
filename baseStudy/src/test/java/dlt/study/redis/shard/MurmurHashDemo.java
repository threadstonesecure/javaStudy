package dlt.study.redis.shard;

import redis.clients.util.Hashing;

/**
 * Created by denglt on 2016/2/2.
 */
public class MurmurHashDemo {
	public static void main(String[] args) {
		Hashing hash = Hashing.MURMUR_HASH;
		for (int i = 0; i < 10; i++)
			System.out.println(hash.hash("denglt" + i));
	}
}
