package dlt.study.dubbo.extension;

import com.alibaba.dubbo.cache.CacheFactory;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.RegistryFactory;
import com.alibaba.dubbo.rpc.Filter;
import dlt.utils.dubbo.InvokeFilterWrapper;
import dlt.utils.dubbo.cache.CacheFilter;
import org.junit.Test;

/**
 * Created by denglt on 16/10/19.
 */
public class ExtensionDemo {

    @Test
    public void loader() {
        ExtensionLoader<InvokeFilterWrapper> filterLoader = ExtensionLoader.getExtensionLoader(InvokeFilterWrapper.class);
    }

    @Test
    public void getAdaptiveExtension() {

        ExtensionLoader<Filter> filterLoader = ExtensionLoader.getExtensionLoader(Filter.class);
        filterLoader.getDefaultExtensionName(); // 触发器 ExtensionLoader.loadExtensionClasses

        String extensionName = filterLoader.getExtensionName(Filter.class);
        System.out.println(extensionName);
        extensionName = filterLoader.getExtensionName(CacheFilter.class);
        System.out.println(extensionName);
       // Filter filter = filterLoader.getAdaptiveExtension();//Adaptive对象的目的： 当扩展点有依赖例外一个扩展点时，通过private T injectExtension(T instance) 方法注入的依赖扩展点是一个Adaptive实例

    }

    @Test
    public void getAdaptiveExtension2() {
        ExtensionLoader<CacheFactory> cacheFactorys = ExtensionLoader.getExtensionLoader(CacheFactory.class);
        CacheFactory cacheFactory = cacheFactorys.getAdaptiveExtension();
        System.out.println(cacheFactory.getClass());
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


