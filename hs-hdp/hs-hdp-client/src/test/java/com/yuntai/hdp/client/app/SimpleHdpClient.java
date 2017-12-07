package com.yuntai.hdp.client.app;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.client.HdpClient;
import com.yuntai.hdp.client.Receiver;
import com.yuntai.hdp.client.ResultFuture;

public class SimpleHdpClient {

	public static void main(String[] args) throws Exception {

		final HdpClient hdpClient = new HdpClient()
				.remoteAddress("127.0.0.1", 8088).hosId("10000") // 设置医院ID
				.reconnectDelay(10) // 设置重连接间隔（秒）
				.ssl(true) // 启用ssl安全通讯
				.receiver(new Receiver() {
					@Override
					public void receive(RequestPack data) {
						System.out.println(data);
					}
					@Override
					public void receive(ResultPack data) {
						System.out.println(data);
					}
				}) // 设置业务数据接收器
				.connect(); // 连接HDP Server

		RequestPack data = new RequestPack();
		data.setBody("test");
		data.setCmd("0700");


		if (hdpClient.synSendData(data)) { // 阻塞，发送完毕返回
			System.out.println("数据发送成功！");
		} else {
			System.out.println("数据发送失败");
		}

		ResultFuture<Boolean> result = hdpClient.asynSendData(data);// 不等待发送完毕直接返回
		  	/*你的其他代码*/
		if (result.get()) { // 阻塞，等待发送完毕返回
			System.out.println("数据发送成功！");
		} else {
			System.out.println("数据发送失败");
		}
		
		System.in.read();
		hdpClient.close();
	}

}
