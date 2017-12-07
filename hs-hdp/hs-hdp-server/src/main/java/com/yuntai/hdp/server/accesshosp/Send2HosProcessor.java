package com.yuntai.hdp.server.accesshosp;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.future.FutureResult;
import com.yuntai.hdp.server.HdpServer;
import com.yuntai.hdp.server.HospitalManager;
import com.yuntai.hdp.server.net.Connection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * @author denglt
 * @Description 发送数据到医院
 * @CopyRight: 版权归Hundsun 所有
 */
@Service
public class Send2HosProcessor implements SendRequest {

    private static Log log = LogFactory.getLog(SendRequest.class);

    @Override
    public boolean send(RequestPack request) {
        return send(request,null);
    }

    @Override
    public boolean send(RequestPack request, String redirectHosId) {
        String hosId = request.getHosId();
        if ((hosId == null || hosId.length() == 0) && redirectHosId == null) {
            log.error("Send request fail. Request doesn't contain hosId。  Data content:" + request);
            return false;
        }
        hosId = redirectHosId != null ? redirectHosId : hosId;
        Connection conn = HospitalManager.getConnect(hosId);
        if (conn == null) {
            log.error(String.format("Send request fail. Hospital[%s] dosen't connect to HDP Server!", hosId));
            return false;
        }

        FutureResult<Boolean> result = HdpServer.receiveAnswerManager.newFutureResult(request.getHdpSeqno());

        if (conn.write(request)) {
            Boolean b = result.get(3);
            return b != null;
        } else {
            HdpServer.receiveAnswerManager.remove(request.getHdpSeqno());
            return false;
        }
    }
}
