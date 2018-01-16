package watchhdp;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import dlt.utils.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BadDataHandler implements DataHandler {
    private static Log log = LogFactory.getLog(WatchHospital.class);

    @Override
    public ResultPack dealData(Object msg) {
        //log.info( msg.getClass().getSimpleName() + ":" + JsonUtils.toJson(msg));
        boolean intercept = false;
        if (msg instanceof RequestPack) {
            RequestPack request = (RequestPack) msg;
            if (request.getCmd().equals(IAccessTypeConstants.ACCESSS_RemainQueryBusiness)) {
                log.info("请求->" + JsonUtils.toJson(msg));
                //intercept = request.getBody().contains("36010101:003451:2018-01-18:2:002002");
            }

            if (request.getCmd().equals(IAccessTypeConstants.ACCESSS_TimeSlotRemainQueryBusiness)) {
                log.info("请求->" + JsonUtils.toJson(msg));
                intercept = request.getBody().contains("36010101:003451:2018-01-18:2:002002");
            }

            if (intercept) {
                ResultPack result = new ResultPack();
                result.setSeqno(request.getSeqno());
                result.setHosId(request.getHosId());
                result.setCmd(request.getCmd());
                result.setHdpSeqno(request.getHdpSeqno());
                result.setCallMode(request.getCallMode());
                //result.setKind("4444");
                //result.setMsg("访问异常！");
                result.setKind("610010000100017");
                result.setMsg(UDBErrorCode.LOGGOUT_MSG_610010001105003);
                return result;
            }
        }

        return null; // 返回null将不发送数据到server
    }
}
