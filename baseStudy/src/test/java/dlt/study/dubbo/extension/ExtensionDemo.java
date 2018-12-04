package dlt.study.dubbo.extension;

import com.alibaba.dubbo.cache.CacheFactory;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.RegistryFactory;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Cluster;
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
        String defaultExtensionName = filterLoader.getDefaultExtensionName();// 触发器 ExtensionLoader.loadExtensionClasses
        System.out.println(defaultExtensionName);

        String extensionName = filterLoader.getExtensionName(Filter.class);
        System.out.println(extensionName);
        extensionName = filterLoader.getExtensionName(CacheFilter.class);
        System.out.println(extensionName);
        // error Filter filter = filterLoader.getAdaptiveExtension();//Adaptive对象的目的： 当扩展点有依赖例外一个扩展点时，通过private T injectExtension(T instance) 方法注入的依赖扩展点是一个Adaptive实例

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
    public void getAdaptiveRegistryFactory() {
        ExtensionLoader<RegistryFactory> extension = ExtensionLoader.getExtensionLoader(RegistryFactory.class);
        RegistryFactory registryFactory = extension.getAdaptiveExtension();
        System.out.println(registryFactory);//com.alibaba.dubbo.registry.RegistryFactory$Adpative@307f6b8c
    }

    @Test
    public void getAdaptiveProtocol() {
        ExtensionLoader<Protocol> extensionLoader = ExtensionLoader.getExtensionLoader(Protocol.class);
        Protocol adaptiveExtension = extensionLoader.getAdaptiveExtension();
        System.out.println(adaptiveExtension); // com.alibaba.dubbo.rpc.Protocol$Adpative@491cc5c9
        //adaptiveExtension.refer()
    }

    @Test
    public void getAdaptiveCuster() {
        ExtensionLoader<Cluster> extensionLoader = ExtensionLoader.getExtensionLoader(Cluster.class);
        //extensionLoader.getDefaultExtension()
        Cluster adaptiveExtension = extensionLoader.getAdaptiveExtension();
        System.out.println(adaptiveExtension.getClass());
    }

    public class Protocol$Adpative implements com.alibaba.dubbo.rpc.Protocol {
        public void destroy() {
            throw new UnsupportedOperationException("method public abstract void com.alibaba.dubbo.rpc.Protocol.destroy() of interface com.alibaba.dubbo.rpc.Protocol is not adaptive method!");
        }

        public int getDefaultPort() {
            throw new UnsupportedOperationException("method public abstract int com.alibaba.dubbo.rpc.Protocol.getDefaultPort() of interface com.alibaba.dubbo.rpc.Protocol is not adaptive method!");
        }

        @Override
        public com.alibaba.dubbo.rpc.Invoker refer(java.lang.Class arg0, com.alibaba.dubbo.common.URL arg1) throws RpcException {
            if (arg1 == null) throw new IllegalArgumentException("url == null");
            com.alibaba.dubbo.common.URL url = arg1;
            String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
            if (extName == null)
                throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.rpc.Protocol) name from url(" + url.toString() + ") use keys([protocol])");
            com.alibaba.dubbo.rpc.Protocol extension = (com.alibaba.dubbo.rpc.Protocol) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.rpc.Protocol.class).getExtension(extName);
            return extension.refer(arg0, arg1);
        }

        @Override
        public com.alibaba.dubbo.rpc.Exporter export(com.alibaba.dubbo.rpc.Invoker arg0) throws RpcException {
            if (arg0 == null) throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument == null");
            if (arg0.getUrl() == null)
                throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument getUrl() == null");
            com.alibaba.dubbo.common.URL url = arg0.getUrl();
            String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
            if (extName == null)
                throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.rpc.Protocol) name from url(" + url.toString() + ") use keys([protocol])");
            com.alibaba.dubbo.rpc.Protocol extension = (com.alibaba.dubbo.rpc.Protocol) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.rpc.Protocol.class).getExtension(extName);
            return extension.export(arg0);
        }
    }


    public class RegistryFactory$Adpative implements com.alibaba.dubbo.registry.RegistryFactory {

        public com.alibaba.dubbo.registry.Registry getRegistry(com.alibaba.dubbo.common.URL arg0) {
            if (arg0 == null) throw new IllegalArgumentException("url == null");
            com.alibaba.dubbo.common.URL url = arg0;
            String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
            if (extName == null)
                throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.registry.RegistryFactory) name from url(" + url.toString() + ") use keys([protocol])");
            com.alibaba.dubbo.registry.RegistryFactory extension = (com.alibaba.dubbo.registry.RegistryFactory) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.registry.RegistryFactory.class).getExtension(extName);
            return extension.getRegistry(arg0);
        }
    }


    public class Cluster$Adpative implements com.alibaba.dubbo.rpc.cluster.Cluster {
        public com.alibaba.dubbo.rpc.Invoker join(com.alibaba.dubbo.rpc.cluster.Directory arg0) throws RpcException {
            if (arg0 == null)
                throw new IllegalArgumentException("com.alibaba.dubbo.rpc.cluster.Directory argument == null");
            if (arg0.getUrl() == null)
                throw new IllegalArgumentException("com.alibaba.dubbo.rpc.cluster.Directory argument getUrl() == null");
            com.alibaba.dubbo.common.URL url = arg0.getUrl();
            String extName = url.getParameter("cluster", "failover");
            if (extName == null)
                throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.rpc.cluster.Cluster) name from url(" + url.toString() + ") use keys([cluster])");
            com.alibaba.dubbo.rpc.cluster.Cluster extension = (com.alibaba.dubbo.rpc.cluster.Cluster) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.rpc.cluster.Cluster.class).getExtension(extName);
            return extension.join(arg0);
        }
    }
}


