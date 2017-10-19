package dlt.utils.dubbo.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * Constants
 * <p>
 * Created by denglt on 2016/11/8.
 */
public class Constants {

    public final static String PROVIDER_KEY     = "cache.provider";

    public final static String READONLY_KEY     = "cache.readOnly"; // true|false

    public final static String EXPIRY_KEY       = "cache.expiry";  // 单位秒

    public final static String USECACHE_KEY     = "cache.use";  // true|false

    public final static String CLOSE_CACHE_METHOD_KEY = "cache.close.method";

    public final static String REDIS_CONNECTION_FACTORY =  "cache.redis.connect.factory";

    public final static List<String> CACHE_NAMES;

    static {
        CACHE_NAMES = new ArrayList<>();
        CACHE_NAMES.add("redis");
        CACHE_NAMES.add("ex_jcache");
    }
}
