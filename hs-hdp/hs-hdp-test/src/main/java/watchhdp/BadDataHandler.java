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
        if (msg instanceof RequestPack) {
            RequestPack request = (RequestPack) msg;
            if (request.getCmd().equals(IAccessTypeConstants.ACCESSS_RemainQueryBusiness)) {
                if (request.getBody().contains("030101:000100:2017-12-08:1:002002")) {
                    log.info("请求->" + JsonUtils.toJson(msg));
                    ResultPack result = new ResultPack();
                    result.setSeqno(request.getSeqno());
                    result.setHosId(request.getHosId());
                    result.setCmd(request.getCmd());
                    result.setHdpSeqno(request.getHdpSeqno());
                    result.setCallMode(request.getCallMode());
                    result.setKind("4444");
                    result.setMsg("访问异常！");
                    return result;
                }
            }
        }

        return null; // 返回null将不发送数据到server
    }
}
