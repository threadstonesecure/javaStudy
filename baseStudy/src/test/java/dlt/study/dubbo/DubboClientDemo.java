package dlt.study.dubbo;

import com.alibaba.dubbo.rpc.RpcContext;

import dlt.domain.model.service.UserService;
import dlt.utils.spring.SpringContextUtils;

public class DubboClientDemo {

	public static void main(String[] args) {
		String[] paths = new String[] { "dubbotest/*client.xml" };
		SpringContextUtils.init(paths);

		UserService userService = SpringContextUtils.getBean("userService");
		RpcContext rpcContext = RpcContext.getContext();
		System.out.println("RpcContext :" + rpcContext);
		userService.getUsers();
		//userService.getUserCount();
		System.out.println("ok");
	}
}
