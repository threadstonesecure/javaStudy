package com.yuntai.hdp.access.service;

import com.yuntai.hdp.access.RequestPack;

/**
 * @Description 云服务下发数据到前置机的服务接口
 * 请使用 AccessHospitalHandler 接口
 *
 * 目前仅数据同步程序程序使用到(数据同步程序在使用Hdp做为数据通道改造时，不会在使用该接口(已经跟邱峰确认))
 * 在HdpServer级联模式下不支持该方法调用
 * @author denglt@hundsun.com
 * @CopyRight: 版权归Hundsun 所有
 */
@Deprecated
public interface DowndataHandler {

    /**
     * 下发数据到前置机器
     * @param request
     * @return
     */
	public Boolean downData(RequestPack request);
}
