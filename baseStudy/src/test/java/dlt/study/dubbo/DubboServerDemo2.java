package dlt.study.dubbo;

import dlt.utils.spring.SpringContextUtils;

public class DubboServerDemo2 {

	public static void main(String[] args) throws Exception {
		String[] paths = new String[] { "dubbotest/*-server2.xml" };
		SpringContextUtils.init(paths);
		System.out.println("dubbo server start");
		System.in.read(); // 按任意键退出
	}
}
