package hdpclient;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.client.HdpClient;
import com.yuntai.hdp.client.Receiver;
import com.yuntai.hdp.client.ResultFuture;
import com.yuntai.hdp.client.SynAccessHospital;

/**
 * 测试HdpClient代码的兼容性
 */
public class HdpClientDemo2 {
	public static void main(String[] args) {

		final HdpClient hdpClient = new HdpClient()
				.remoteAddress("127.0.0.1", 8088)
				.hosId("10000").ssl(true).receiver(new Receiver() {
					@Override
					public void receive(RequestPack request) {
						// 不建议使用
					}
					@Override
					public void receive(ResultPack result) {
						//不建议使用
					}
				}).businessThreadPoolSize(20) // 配置业务线程数量
				.synAccessHospital(new SynAccessHospital() {
                    @Override
                    public ResultPack getHospitalResult(RequestPack request) {
                        return null;
                    }
                }) // 2015-12-20 增加配置同步访问医院
				.connect();

		RequestPack request = new RequestPack();
		try {
			boolean b = hdpClient.synSendData(request);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		ResultFuture<Boolean> resultFuture = hdpClient.asynSendData(request);
		boolean isActive = hdpClient.isActive();
		try {
			hdpClient.close();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		// 2015-12-20  增加同步发送数据
		ResultPack resultPack = hdpClient.sendData(request, 60);
	}
}
