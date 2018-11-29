package hdpclient;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.client.HdpClient;
import com.yuntai.hdp.client.SynAccessHospital;


public class HdpClientDemo {
	public static void main(final String[] args) throws Exception {

		HdpClient hdpClient = new HdpClient()
				.remoteAddress("121.40.182.17", 8088) // 正式环境
				.hosId("999999-test").ssl(true)
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
						System.out.println("成功收到数据!");
                        return resultPack;
                    }
                })
				.connect();
		System.out.println("HdpClientDemo启动成功！");
		Thread.currentThread().join();
	}
}
