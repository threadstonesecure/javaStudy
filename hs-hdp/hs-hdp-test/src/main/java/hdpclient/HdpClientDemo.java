package hdpclient;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.client.HdpClient;
import com.yuntai.hdp.client.Receiver;
import com.yuntai.hdp.client.ResultFuture;
import com.yuntai.hdp.client.SynAccessHospital;

import java.io.IOException;

public class HdpClientDemo {
	public static void main(final String[] args) throws Exception {

		HdpClient hdpClient = new HdpClient()
				.remoteAddress("127.0.0.1", 8088)
				//.remoteAddress("120.26.224.231", 8088)   // 预发布环境
				.hosId("999999-test").ssl(false)
				.accessToken("98608d4679a28b719815ee03f7c404e0")
				.synAccessHospital(new SynAccessHospital() {   //配置同步访问医院
                    @Override
                    public ResultPack getHospitalResult(RequestPack request) {
						// 前置机业务代码
						ResultPack resultPack = new ResultPack();
						resultPack.setHosId(request.getHosId());
						resultPack.setCmd(request.getCmd());
						resultPack.setSeqno(request.getSeqno());
						resultPack.setKind("0");
						resultPack.setMsg("成功收到数据!");
                        return resultPack;
                    }
                })
				.connect();
		Thread.sleep(6000);
		int i = 0;
		while (i<1) {
			RequestPack request = new RequestPack();
			request.setCmd("denglt_001");
			request.setBody("dfdfa");
			ResultPack resultPack = hdpClient.sendData(request, 60);
			i++;
		}
		System.in.read();
	}
}
