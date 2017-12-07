package com.yuntai.hdp.client.app;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.client.HdpClient;

import java.util.Random;

/**
 * 模拟多HdpClient操作
 * 
 * @author denglt
 *
 */
public class MultHdpClientDemo {
	public static void main(String[] args) throws Exception {
		//String ip = "121.40.225.50";
		String ip = "127.0.0.1";
		final HdpClient hdpClient = new HdpClient(ip, 8088)
				.hosId(new Random().nextInt(100) + "").reconnectDelay(10)
				.ssl(true)
				// .receiver(null) //设置业务数据接收器
				.connect();

		RequestPack data = new RequestPack();
		data.setBody("test");
		data.setCmd("console");
		hdpClient.synSendData(data);
		data.setCmd("no command");

		hdpClient.synSendData(data);

		data.setCmd("");
		hdpClient.synSendData(data);

		new HdpClient(ip, 8088).hosId(new Random().nextInt(100) + "").ssl(true)
				.reconnectDelay(10).connect();

		new HdpClient(ip, 8088).hosId(new Random().nextInt(100) + "").ssl(true)
				.reconnectDelay(10).connect();
		new HdpClient(ip, 8088).hosId(new Random().nextInt(100) + "").ssl(true)
				.reconnectDelay(10).connect();
		new HdpClient(ip, 8088).hosId(new Random().nextInt(100) + "").ssl(true)
				.reconnectDelay(10).connect();

		new HdpClient(ip, 8088).hosId("10000").reconnectDelay(10).ssl(true)
				.connect();

		new HdpClient(ip, 8088).hosId("10000").reconnectDelay(10).ssl(true)
				.connect();

	}
}
