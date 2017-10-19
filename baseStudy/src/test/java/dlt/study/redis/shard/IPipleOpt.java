package dlt.study.redis.shard;

/**
 * Created by denglt on 2016/2/1.
 */
public interface IPipleOpt {
    String put(String key, String value);


    String get(String key);
}
