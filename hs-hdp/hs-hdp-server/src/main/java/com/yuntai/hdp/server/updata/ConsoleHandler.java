package com.yuntai.hdp.server.updata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.UpdataHandler;

import java.util.Random;

/**
 * 仅测试用
 * 
 * @Description 控制台DataHandler，仅在控制台显示数据
 * @author denglt@hundsun.com
 * @CopyRight: 版权归Hundsun 所有
 */
@Service
public class ConsoleHandler implements UpdataHandler {

	private static Log log = LogFactory.getLog(ConsoleHandler.class);

	@Override
	public boolean checkData(RequestPack data) {
		return true;
	}

	@Override
	public ResultPack process(RequestPack data) {
		log.info("Deal data:" + data);
        Random r = new Random();
        int i = r.nextInt(10);
        try {
            Thread.sleep((i + 1) * 1000);
        }catch(Exception ex){
            ex.printStackTrace();
        }
		ResultPack resultPack = new ResultPack();
		resultPack.setSeqno(data.getSeqno());
		resultPack.setHosId(data.getHosId());
		resultPack.setCmd(data.getCmd());
		resultPack.setKind("0");
		resultPack.setMsg("处理成功！" + String.format("耗时[%d]s",i+1));
		resultPack.setBody("data is dealed by ConsoleHandler!");
		resultPack.setReturnTime(System.currentTimeMillis());
		return resultPack;
	}

}
