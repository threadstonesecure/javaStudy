package dlt.study.redis.shard;

import redis.clients.util.ShardInfo;

/**
 * Created by denglt on 2016/2/1.
 */
class PipelineShardInfo extends ShardInfo<Pipeline>  {

	private String name;
    private Pipeline pipeline;



	@Override
    protected Pipeline createResource() {
        return  new Pipeline(this);
    }

	@Override
	public String getName() {
		return name;
	}


    public void setName(String name) {
        this.name = name;
    }


}
