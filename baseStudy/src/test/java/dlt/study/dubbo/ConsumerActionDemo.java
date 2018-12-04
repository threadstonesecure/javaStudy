package dlt.study.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;
import com.alibaba.dubbo.registry.integration.RegistryDirectory;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.cluster.Cluster;
import dlt.domain.model.User;
import dlt.domain.model.service.UserService;

import java.util.List;

/**
 * 分析ReferenceConfig后，使用底层的Api来实现Consumer调用Provider的过程
 * 即一个简化版ReferenceConfig的内部实现过程，方便理解dubbo的架构
 */
public class ConsumerActionDemo {

    public static void main(String[] args) throws Throwable {
        // 获取 注册中心 服务    RegistryFactory
        RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
        URL registryURL = new URL("multicast", "224.5.6.7", 1234);  //multicast://224.5.6.7:1234
        Registry registry = registryFactory.getRegistry(registryURL);
        System.out.println(registry.getClass()); // com.alibaba.dubbo.registry.multicast.MulticastRegistry

        // 注册中心字典
        URL directoryURL = new URL("这个URL就没啥用", "*", 1234).setServiceInterface("这个必须要");
        RegistryDirectory<UserService> registryDirectory = new RegistryDirectory(UserService.class/*Object.class DubboProtocol 没用这个参数*/, directoryURL);
        System.out.println(registryDirectory);
        registryDirectory.setRegistry(registry);
        registryDirectory.setProtocol(ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension());
        URL serviceUrl = new URL("temp", "127.0.0.1", 80);
        registryDirectory.subscribe(serviceUrl.setServiceInterface("dlt.domain.model.service.UserService")); //订阅服务
       // registry.subscribe(serviceUrl,registryDirectory);

        List<Invoker<UserService>> invokersForUserService = registryDirectory.list(new RpcInvocation("getUsers", new Class[]{Void.class}, null));
        invokersForUserService.forEach(System.out::println);

        Result result = invokersForUserService.get(0).invoke(new RpcInvocation("getUsers", new Class[]{}, new Object[]{}));
        System.out.println(result.recreate());

        // 上面不好用， 使用ProxyFactory生成UserSevice代理
        ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();
        UserService userService = proxyFactory.getProxy(invokersForUserService.get(0));
        List<User> users = userService.getUsers();
        System.out.println(users);

        // 上面没有使用Cluster
        Cluster cluster = ExtensionLoader.getExtensionLoader(Cluster.class).getAdaptiveExtension();
        Invoker<UserService> clusterInvoker = cluster.join(registryDirectory);
        userService = proxyFactory.getProxy(clusterInvoker);
        users = userService.getUsers();
        System.out.println(users);

    }
}
