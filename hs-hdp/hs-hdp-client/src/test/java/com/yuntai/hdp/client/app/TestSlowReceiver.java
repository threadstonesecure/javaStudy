package com.yuntai.hdp.client.app;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.client.HdpClient;
import com.yuntai.hdp.client.Receiver;
import io.netty.util.internal.SystemPropertyUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestSlowReceiver {
	private static Log log = LogFactory.getLog(HdpClient.class);

	public static void main(String[] args) throws Exception {
		String ip = "127.0.0.1"; // "121.40.225.50";
		int threadCount = Math.max(1, SystemPropertyUtil.getInt(
				"io.netty.eventLoopThreads", Runtime.getRuntime()
						.availableProcessors() * 2));
		System.out.println("线程数量：" + threadCount);
		final HdpClient hdpClient = new HdpClient().remoteAddress(ip, 8088)
				.hosId("10000") // 设置医院ID
				.reconnectDelay(10) // 设置重连接间隔（秒）
				.ssl(true) // 启用ssl安全通讯
				.receiver(new Receiver() {
					@Override
					public void receive(RequestPack data) {
						log.info("收到RequestPack。。。。");
						try {
							Thread.currentThread().sleep(1000 * 60);
						} catch (Exception e) {

						}
						log.info("处理完RequestPack！");
					}

					@Override
					public void receive(ResultPack data) {
						log.info("收到返回结果："+data);
					}
				}) // 设置业务数据接收器
				.connect(); // 连接HDP Server
		while (true) {
			RequestPack rp = new RequestPack();
			rp.setCmd("console");
			rp.setBody("aaaaaaaaaa");
			if (hdpClient.synSendData(rp)) {
				System.out.println("发送数据成功！");
			}
			try {
				Thread.currentThread().sleep(1000 * 10);
			} catch (Exception ex) {

			}
		}
		// System.in.read();

	}
}
