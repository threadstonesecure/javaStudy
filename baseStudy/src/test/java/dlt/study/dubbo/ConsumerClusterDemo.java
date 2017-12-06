package dlt.study.dubbo;

import com.alibaba.dubbo.rpc.RpcContext;
import dlt.domain.model.service.UserService;
import dlt.utils.spring.SpringContextUtils;
import org.junit.Test;

public class ConsumerClusterDemo {

    @Test
    public void clientInvoker(){
        String[] paths = new String[] { "dubbotest/consumer-cluster/*client.xml" };
        SpringContextUtils.init(paths);
        UserService userService = SpringContextUtils.getBean("userService");

        System.out.println("ok");
    }
}
