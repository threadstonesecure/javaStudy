package com.yuntai.hdp.client;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;

/**
 * HdpClient同步访问hospital数据接口，由前置机实现注入到HdpClient实例
 * @see  com.yuntai.hdp.client.HdpClient#synAccessHospital(SynAccessHospital) synAccessHospital
 * Created by denglt on 2015/12/14.
 */
public interface SynAccessHospital {
    public ResultPack getHospitalResult(RequestPack request);
}
