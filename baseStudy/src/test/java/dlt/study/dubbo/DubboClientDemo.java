package dlt.study.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.RegistryService;
import com.alibaba.dubbo.registry.integration.RegistryDirectory;
import com.alibaba.dubbo.rpc.*;

import dlt.domain.model.User;
import dlt.domain.model.service.UserService;
import dlt.utils.spring.SpringContextUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class DubboClientDemo {

    @BeforeClass
    public static void init() {
        System.setProperty("java.net.preferIPv4Stack", "true"); // -Djava.net.preferIPv4Stack=true
    }

    @Test
    public void clientInvoker() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        String[] paths = new String[]{"dubbotest/*client.xml"};
        SpringContextUtils.init(paths);
        UserService userService = SpringContextUtils.getBean("userService");
        System.out.println(userService.getClass());

        // System.out.println("ok :" + userService.getUsers());
    }

    @Test
    public void registryService() throws Exception {
        String[] paths = new String[]{"dubbotest/*client.xml"};
        SpringContextUtils.init(paths);
        RegistryService registryService = SpringContextUtils.getBean("registryService");
        System.out.println(registryService.getClass());

        URL url = new URL("temp", "127.0.0.1", 80);

        registryService.subscribe(url.setServiceInterface("*"), (urls) -> System.out.println("subscribe:" + urls));

        Thread.sleep(3000);
        List<URL> lookups;
        // lookups = registryService.lookup(url.setServiceInterface("*"));
        //lookups.forEach((r) -> System.out.println("lookups *:" + r));

        System.out.println("============");
        lookups = registryService.lookup(url.setServiceInterface("dlt.domain.model.service.UserService"));
        lookups.forEach((r) -> System.out.println("lookups UserService:" + r));
        Thread.currentThread().join();
    }

    /**
     * 使用底层对象调用
     * @throws Exception
     */
    @Test
    public void registryDirectory() throws Throwable{
        String[] paths = new String[]{"dubbotest/*client.xml"};
        SpringContextUtils.init(paths);
        RegistryService registryService = SpringContextUtils.getBean("registryService");
        RegistryDirectory<UserService> registryDirectory = new RegistryDirectory(UserService.class, new URL("multicast","224.5.6.7",1234).setServiceInterface("dddd"));
        registryDirectory.setProtocol(ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension());
       /* registryDirectory.setRegistry();
        registryDirectory.subscribe(url); 用下面 registryService.subscribe 代替*/

        URL url = new URL("temp", "127.0.0.1", 80);
        registryService.subscribe(url.setServiceInterface("dlt.domain.model.service.UserService"), registryDirectory);

        Thread.sleep(3000);
        List<Invoker<UserService>> invokersForUserService = registryDirectory.list(new RpcInvocation("getUsers", new Class[]{Void.class}, null));
        //System.out.println("getUsers:" + getUsers);
        invokersForUserService.forEach(System.out::println);

        Result result = invokersForUserService.get(0).invoke(new RpcInvocation("getUsers", new Class[]{}, new Object[]{}));
        System.out.println(result.recreate());

        ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();
        UserService proxy = proxyFactory.getProxy(invokersForUserService.get(0));
        List<User> users = proxy.getUsers();
        System.out.println(users);

        Thread.currentThread().join();
    }

    @Test
    public  void test() throws Exception{
        UserService.class.getMethod("getUsers");
    }
}
