package hdpclient;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.client.HdpClient;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 模拟多HdpClient同步发送数据
 * 
 * @author denglt
 *
 */
public class MultSendDataDemo {
	public static void main(String[] args) throws Exception {
		String ip = "127.0.0.1";
		final HdpClient hdpClient = new HdpClient(ip, 8088)
				.hosId(new Random().nextInt(100) + "").reconnectDelay(10)
				.ssl(true).connect();
		ExecutorService tpe = Executors.newFixedThreadPool(10);

		for (int i = 0; i < 200; i++) {
			tpe.execute(new Runnable() {
				@Override
				public void run() {
					RequestPack request = new RequestPack();
					request.setCmd("console");
					request.setBody("dfasdfas");
					request.setSendTime(System.currentTimeMillis());

					ResultPack resultPack = hdpClient.sendData(request, 6);
					if (!resultPack.getHdpSeqno().equals(request.getHdpSeqno())) {
						System.out.println("fuck!!!!!!多线程异常");
					}
				}
			});
		}
	}
}
