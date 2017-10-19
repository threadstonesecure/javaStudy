package dlt.study.redis.shard;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by denglt on 2016/2/1.
 */
public class Pipeline implements IPipleOpt {
	private Map<String, String> map = new HashMap<>();


    public Pipeline(PipelineShardInfo shardInfo){

    }
	@Override
	public String put(String key, String value) {
		return map.put(key, value);
	}

	@Override
	public String get(String key) {
		return map.get(key);
	}
}
