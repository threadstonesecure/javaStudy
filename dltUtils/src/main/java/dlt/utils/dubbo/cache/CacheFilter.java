package dlt.utils.dubbo.cache;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.CacheFactory;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import dlt.utils.dubbo.URLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dlt.utils.dubbo.cache.Constants.*;

/**
 * 由于dubbo的CacheFilter对于method上的cache支持有问题(只能使用LruCache),
 * 故重写CacheFilter
 * <p>
 * Created by denglt on 16/10/21.
 */
@Activate(group = {Constants.CONSUMER, Constants.PROVIDER}, value = Constants.CACHE_KEY)
public class CacheFilter implements Filter {
    private final NullValue empty = NullValue.INSTANCE;

    protected static Log log = LogFactory.getLog(CacheFilter.class);

    static {
        log.info("start dubbo extend cache!");
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = invoker.getUrl().addParameter(Constants.METHOD_KEY, invocation.getMethodName());
        String cachetype = URLUtils.getParameter(url, Constants.CACHE_KEY);
        boolean useCache = isUseCache(url, invocation.getMethodName());
        if (!StringUtils.isEmpty(cachetype) && useCache) {
            CacheFactory cacheFactory = ExtensionLoader.getExtensionLoader(CacheFactory.class).getExtension(cachetype);
            Cache cache = cacheFactory.getCache(url);
            if (cache != null) {
                log.debug("dubbo request:" + url.toFullString());
                String key = toArgumentString(invocation.getArguments());
                log.debug("argumets:" + key);
                Object value = cache.get(key);
                if (value != null) {
                    log.debug("dubbon request hit cache!");
                    return new RpcResult(empty.equals(value) ? null : value);
                }
                Result result = invoker.invoke(invocation);
                if (!result.hasException()) {
                    log.debug("cache dubbo result!");
                    cache.put(key, result.getValue() == null ? empty : result.getValue());
                }
                return result;
            }
        }
        return invoker.invoke(invocation);
    }

    private String toArgumentString(Object[] args) {
        if (args == null || args.length == 0) return "no args";
        return JSON.toJSONString(args, SerializerFeature.SortField);
    }

    /**
     * 判断是否使用cache
     *
     * @param url
     * @param methodName
     * @return
     */
    private static boolean isUseCache(URL url, String methodName) {
        String useCacheValue = URLUtils.getMethodParameter(url, USECACHE_KEY);
        if (StringUtils.isEmpty(useCacheValue)) { // 没有显示设置
            //检查CLOSE_CACHE_METHOD_KEY是否通过
            List<String> closeCacheMethodValues = URLUtils.getUnionParameter(url, CLOSE_CACHE_METHOD_KEY);
            List<String> closeCacheMethodNames = new ArrayList<>();
            for (String closeCacheMethodValue : closeCacheMethodValues) {
                String[] methodValues = closeCacheMethodValue.split(",");
                closeCacheMethodNames.addAll(Arrays.asList(methodValues));
            }
            for (String closeCacheMethodName : closeCacheMethodNames) {
                if (methodName.matches(closeCacheMethodName.replace("*", ".*"))) {
                    return false;
                }
            }
            return true;
        } else {  //  显示设置USECACHE_KEY
            if (useCacheValue.toLowerCase().equals("true")) {
                return true;
            } else {
                return false;
            }
        }

    }

    public static void main(String[] args) {
        String methodName = "saveDocDisease";
        URL url = new URL("dubbo", "127.0.0.1", 9000);
        url = url.addParameter(Constants.DEFAULT_KEY_PREFIX + CLOSE_CACHE_METHOD_KEY, "save*,add*,insert*")
                .addParameter(CLOSE_CACHE_METHOD_KEY, "getDocDisease,getIcdDisease")
                .addParameter(methodName + "." + USECACHE_KEY, "true")
                .addParameter(Constants.METHOD_KEY,methodName);
        System.out.println(isUseCache(url, methodName));

    }
}
