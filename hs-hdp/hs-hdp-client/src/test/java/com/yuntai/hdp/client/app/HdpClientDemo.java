package com.yuntai.hdp.client.app;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.client.HdpClient;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试多线程通过HdpClient发送数据，得出如下结果： 1、发送数据线程安全 2、服务端收到的数据完整
 * 
 * @author denglt
 *
 */
public class HdpClientDemo {
	public static void main(String[] args) throws Exception {

		final HdpClient hdpClient = new HdpClient()
				.remoteAddress("127.0.0.1", 8088)
		        //.remoteAddress("120.55.66.16", 9088)
				.hosId(new Random().nextInt(100) + "").reconnectDelay(10)
				.ssl(true) // 启用ssl安全通讯
				// .receiver(null) //设置业务数据接收器
				.connect();

		final StringBuffer bigData = new StringBuffer();
		for (int i = 0; i < 1000000; i++) {
			bigData.append("a");
		}

		Runnable runer = new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("正在发送 数据...");
/*					@SuppressWarnings("deprecation")
					ResultFuture<Boolean> result = hdpClient
							.asynSendData("begin: " + bigData + " end;");
					System.out.println("发送数据成功：" + result.get());*/
					
					RequestPack rp = new RequestPack();
					rp.setCmd("console");
					rp.setBody("aaaaaaaaaa");
					hdpClient.asynSendData(rp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
		ExecutorService tpe = Executors.newFixedThreadPool(10);
		while (true) {
			if (hdpClient.isActive()) {
				for (int i = 0; i < 20; i++) {
					tpe.execute(runer);
				}
			}
			Thread.sleep(10000);
		}
	}
}
