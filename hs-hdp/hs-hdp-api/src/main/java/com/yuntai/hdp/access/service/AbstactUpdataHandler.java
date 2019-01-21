package com.yuntai.hdp.access.service;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultKind;
import com.yuntai.hdp.access.ResultPack;

/**
 * @Description:
 * @Package: com.yuntai.hdp.access.service
 * @Author: denglt
 * @Date: 2019/1/4 1:36 PM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public abstract class AbstactUpdataHandler implements UpdataHandler {
    @Override
    public ResultPack process(RequestPack request) {
        if (checkData(request)) {
            return handleRequest(request);
        } else {
            ResultPack resultPack = newResult(request);
            resultPack.setKind(ResultKind.ERROR_DATAFORMAT.getKind());
            resultPack.setMsg(ResultKind.ERROR_DATAFORMAT.getMessage(requestBodyBeanClass != null ? requestBodyBeanClass.getName() : "正确数据格式"));
            return resultPack;
        }

    }

    public abstract ResultPack handleRequest(RequestPack requestPack);


    private ResultPack newResult(RequestPack request) {
        ResultPack resultPack = new ResultPack();
        resultPack.setSeqno(request.getSeqno());
        resultPack.setHosId(request.getHosId());
        resultPack.setCmd(request.getCmd());
        resultPack.setHdpSeqno(request.getHdpSeqno());
        resultPack.setCallMode(request.getCallMode());
        resultPack.setReturnTime(System.currentTimeMillis());
        return resultPack;
    }

    public void setRequestBodyBeanClass(Class requestBodyBeanClass) {
        this.requestBodyBeanClass = requestBodyBeanClass;
    }

    private Class requestBodyBeanClass;
}
