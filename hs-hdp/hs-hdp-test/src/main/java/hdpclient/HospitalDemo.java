package hdpclient;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.client.HdpClient;
import com.yuntai.hdp.client.Receiver;
import com.yuntai.hdp.client.SynAccessHospital;

import java.util.Random;

/**
 * Created by denglt on 2015/12/17.
 */
public class HospitalDemo {
	public static void main(String[] args) throws Exception {

		final HdpClient hdpClient = new HdpClient()
                .remoteAddress("127.0.0.1", 8088)
				//.remoteAddress("10.26.190.124", 8088)
				.hosId("100009") // 设置医院ID
				.reconnectDelay(10) // 设置重连接间隔（秒）
				.ssl(true) // 启用ssl安全通讯
				.businessThreadPoolSize(100).receiver(new Receiver() {
					@Override
					public void receive(RequestPack data) {
						System.out.println(data);
					}

					@Override
					public void receive(ResultPack data) {
						System.out.println(data);
					}
				}) // 设置业务数据接收器
				.synAccessHospital(new AccessHospitalDemo()).connect(); // 连接HDP
																		// Server
		System.in.read();
		hdpClient.close();
	}

	private static class AccessHospitalDemo implements SynAccessHospital {
		@Override
		public ResultPack getHospitalResult(RequestPack request) {
			ResultPack result = new ResultPack();
			result.setHosId(request.getHosId());
			result.setCmd(request.getCmd());
			Random r = new Random();
			int i = r.nextInt(3);
			try {
				Thread.sleep((i + 1) * 1000);
                //Thread.sleep(500);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			result.setReturnTime(System.currentTimeMillis());
			result.setKind("0");
			result.setBody("result:true");
			result.setMsg(String.format("耗时[%d]s", i + 1));
			return result;
		}
	}
}
