package com.yuntai.hdp.server.accesshosp;

import com.yuntai.hdp.access.ResultKind;
import com.yuntai.hdp.server.HdpServer;
import com.yuntai.hdp.server.HdpServer2HdpServer;
import com.yuntai.hdp.server.HospitalManager;
import com.yuntai.hdp.future.FutureResult;
import com.yuntai.hdp.server.NodeConfig;
import com.yuntai.util.HdpHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.AccessHospitalHandler;

import javax.annotation.Resource;

/**
 * @author denglt@hundsun.com Created by denglt on 2015/12/11.
 * @Description 云服务同步对接Hospital前置机的服务
 */
@Service("accessHospitalHandler")
public class AccessHospitalHandlerImpl implements AccessHospitalHandler {
    private static Log log = LogFactory.getLog(AccessHospitalHandlerImpl.class);

    @Resource
    private SendRequest sender;

    @Resource
    private NodeConfig nodeConfig;

    @Resource(name="hdpServer2HdpServer")
    private AccessHospitalHandler  hdpServer2HdpServer;

    /**
     * 获取医院对接数据
     *
     * @param request
     * @param timeout 时间单位秒
     * @return
     */
    @Override
    public ResultPack getHospitalResult(RequestPack request, int timeout) {
        request.setHdpSeqno(HdpHelper.getUUID());
        request.setCallMode(1);
        if (timeout >= 60) {
            timeout = 60;
        }

       // log.info(String.format("===>收到云服务同步对接请求:%s", request.toKeyString()));
       // log.debug(String.format("请求内容：%s", request));

        if (StringUtils.isEmpty(request.getHosId())) {
            ResultPack resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_MISS_KEYFIELD.getKind());
            resultPack.setMsg(ResultKind.ERROR_MISS_KEYFIELD.getMessage("hosId"));
            log.error(String.format("===>请求失败,数据错误:%s", resultPack.toKeyString()));
            return resultPack;
        }

        if (StringUtils.isEmpty(request.getCmd())) {
            ResultPack resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_MISS_KEYFIELD.getKind());
            resultPack.setMsg(ResultKind.ERROR_MISS_KEYFIELD.getMessage("cmd"));
            log.error(String.format("===>请求失败,数据错误:%s", resultPack.toKeyString()));
            return resultPack;
        }

        if (StringUtils.isEmpty(request.getBody())) {
            ResultPack resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_MISS_KEYFIELD.getKind());
            resultPack.setMsg(ResultKind.ERROR_MISS_KEYFIELD.getMessage("body"));
            log.error(String.format("===>请求失败,数据错误:%s", resultPack.toKeyString()));
            return resultPack;
        }

        if (nodeConfig.isToHosByCascade()){
            return hdpServer2HdpServer.getHospitalResult(request, timeout);
        }

        String redirectHosid = HospitalManager.getRedirectHosId(request.getHosId(), request.getCmd());
        if (redirectHosid != null){
            log.info(String.format("Hospital[%s] Request redirect to Hospital[%s]! hdpSeqno=%s", request.getHosId(), redirectHosid, request.getHdpSeqno()));
        }

        String targetHosId = redirectHosid != null ? redirectHosid : request.getHosId();
        if (HospitalManager.getConnect(targetHosId) == null) {
            ResultPack resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_NOEXISTS_HOSP.getKind());
            resultPack.setMsg(ResultKind.ERROR_NOEXISTS_HOSP.getMessage(targetHosId));
            log.error(String.format("===>请求对接失败:%s", resultPack.toKeyString()));
            return resultPack;
        }

        FutureResult<ResultPack> futureResult = HdpServer.resultPackManager.newFutureResult(request.getHdpSeqno());
        if (!sender.send(request, redirectHosid)) {
            HdpServer.resultPackManager.remove(request.getHdpSeqno());
            ResultPack resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_NET.getKind());
            resultPack.setMsg(ResultKind.ERROR_NET.getMessage(""));
            log.error(String.format("===>请求对接失败:%s", resultPack.toKeyString()));
            return resultPack;
        }

        ResultPack resultPack = futureResult.get(timeout);
        if (resultPack == null) {
            resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_TIMEOUT.getKind());
            resultPack.setMsg(ResultKind.ERROR_TIMEOUT.getMessage(timeout + ""));
            log.error(String.format("===>请求对接超时：%s", resultPack.toKeyString()));
            return resultPack;
        }

        //log.debug(String.format("对接返回内容：%s", resultPack));
        //log.info(String.format("===>对接完成,对接返回内容:%s", resultPack.toKeyString()));
        return resultPack;
    }

}
