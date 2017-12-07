package com.yuntai.hdp.client;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;

/**
 * 数据接收器接口 {@link com.yuntai.hdp.client.HdpClient#receiver(Receiver)}
 * @author denglt
 */
public interface Receiver {
	/**
	 * 接收到请求包
	 * @param request
	 */
	public void receive(RequestPack request);

	/**
	 * 接收到处理结果包
	 * @param result
	 */
	public void receive(ResultPack result);

}
