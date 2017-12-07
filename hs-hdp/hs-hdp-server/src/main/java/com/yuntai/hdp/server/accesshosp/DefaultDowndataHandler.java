package com.yuntai.hdp.server.accesshosp;

import javax.annotation.Resource;

import com.yuntai.util.HdpHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.service.DowndataHandler;

/**
 * @Description HDP默认的数据下发服务
 * @author denglt@hundsun.com
 * @CopyRight: 版权归Hundsun 所有
 */
@Service("downdataHandler")
public class DefaultDowndataHandler implements DowndataHandler {
    private static Log log = LogFactory.getLog(DefaultDowndataHandler.class);

	/**
	 * 数据发送者
	 */
	@Resource
	private SendRequest sender;

	@Override
	public Boolean downData(RequestPack request) {
        request.setCallMode(0);
        request.setHdpSeqno(HdpHelper.getUUID());
        log.info(String.format("===>收到云服务异步下发数据请求:%s", request.toKeyString()));
        log.debug(String.format("请求内容：%s", request));
        if (request.getSendTime() == 0){
            request.setSendTime(System.currentTimeMillis());
        }

		return sender.send(request);

	}

    public SendRequest getSender() {
        return sender;
    }

    public void setSender(SendRequest sender) {
        this.sender = sender;
    }
}
