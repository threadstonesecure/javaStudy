package dlt.study.redis.shard;

import redis.clients.util.Sharded;

import java.util.List;

/**
 * Created by denglt on 2016/2/1.
 */
public class ShardedPipeline extends Sharded<Pipeline, PipelineShardInfo>
		implements IPipleOpt {

	public ShardedPipeline(List<PipelineShardInfo> shards) {
		super(shards);
	}

	@Override
	public String put(String key, String value) {
		Pipeline p = this.getShard(key);
		return p.put(key, value);
	}

	@Override
	public String get(String key) {
		Pipeline p = this.getShard(key);
		return p.get(key);
	}
}
