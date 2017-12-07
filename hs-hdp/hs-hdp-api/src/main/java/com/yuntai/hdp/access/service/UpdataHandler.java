package com.yuntai.hdp.access.service;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;

/**
 * @Description 前置机上传数据到HDP后，HDP调用的处理数据的接口。由业务系统实现并发布为dubbo服务
 * @author denglt
 * @CopyRight: 版权归Hundsun 所有
 */
public interface UpdataHandler {

	/**
	 * 校验数据的正确性
	 * @param data
	 * @return  
	 */
    public boolean checkData(RequestPack data);
	/**
	 * @Description 处理RequestPack请求，并返回处理结果数据
	 * @param request
	 * @return 返回处理后的数据结果
	 */
	public ResultPack process(RequestPack request);
}
