package dlt.study.dubbo.extension;

import com.alibaba.dubbo.cache.CacheFactory;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.RegistryFactory;
import com.alibaba.dubbo.rpc.Filter;
import org.junit.Test;

/**
 * Created by denglt on 16/10/19.
 */
public class ExtensionDemo {

    @Test
    public void getAdaptiveExtension() {

        ExtensionLoader<Filter> filterLoader = ExtensionLoader.getExtensionLoader(Filter.class);
        Filter filter = filterLoader.getAdaptiveExtension();

    }

    @Test
    public void getAdaptiveExtension2() {
        ExtensionLoader<CacheFactory> cacheFactorys = ExtensionLoader.getExtensionLoader(CacheFactory.class);
        CacheFactory cacheFactory = cacheFactorys.getAdaptiveExtension();
        System.out.println(cacheFactory);
        URL url = new URL("dubbo", "127.0.0.1", 20888);
        url = url.addParameter("cache", "jcache");
        System.out.println(url.toFullString());
        //Cache cache = cacheFactory.getCache(url);

        ExtensionLoader<Filter> filterLoader = ExtensionLoader.getExtensionLoader(Filter.class);
        Filter filter = filterLoader.getExtension("cache");
        System.out.println(filter);
        Filter traceFilter = filterLoader.getExtension("trace");

        System.out.println(traceFilter);

    }

    @Test
    public void getRegistryFactoryAdaptiveExtension() {
        ExtensionLoader<RegistryFactory> extension = ExtensionLoader.getExtensionLoader(RegistryFactory.class);
        RegistryFactory registryFactory = extension.getAdaptiveExtension();
        System.out.println(registryFactory);

    }
}


