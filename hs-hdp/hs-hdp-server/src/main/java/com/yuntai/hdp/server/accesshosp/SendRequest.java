package com.yuntai.hdp.server.accesshosp;

import com.yuntai.hdp.access.RequestPack;

/**
 * @Description 发送数据接口
 * @author denglt@hundsun.com
 * @CopyRight: 版权归Hundsun 所有
 */
public interface SendRequest {

	boolean send(RequestPack request);

	boolean send(RequestPack request, String redirectHosId);
}
