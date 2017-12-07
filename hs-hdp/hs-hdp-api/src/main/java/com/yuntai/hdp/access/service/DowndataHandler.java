package com.yuntai.hdp.access.service;

import com.yuntai.hdp.access.RequestPack;

/**
 * @Description 云服务下发数据到前置机的服务接口
 * @author denglt@hundsun.com
 * @CopyRight: 版权归Hundsun 所有
 */
public interface DowndataHandler {

    /**
     * 下发数据到前置机器
     * @param request
     * @return
     */
	public Boolean downData(RequestPack request);
}
