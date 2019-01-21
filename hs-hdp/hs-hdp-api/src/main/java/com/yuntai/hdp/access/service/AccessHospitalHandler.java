package com.yuntai.hdp.access.service;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;

/**
 * @author denglt@hundsun.com Created by denglt on 2015/12/11.
 * @Description 云服务同步对接Hospital前置机的服务接口
 */
public interface AccessHospitalHandler {
    /**
     * 获取医院对接数据
     *
     * @param request
     * @param timeout 时间单位秒
     * @return
     */
    ResultPack getHospitalResult(RequestPack request, int timeout);
}
