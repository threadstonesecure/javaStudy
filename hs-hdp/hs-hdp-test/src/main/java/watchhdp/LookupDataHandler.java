package watchhdp;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import dlt.utils.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LookupDataHandler implements DataHandler {
    private static Log log = LogFactory.getLog(LookupDataHandler.class);

    @Override
    public ResultPack dealData(Object msg) {
        if (msg instanceof RequestPack) {
            RequestPack request = (RequestPack) msg;
            if (request.getHosId().equals("226")) {
                if (request.getCmd().equals(IAccessTypeConstants.ACCESSS_RemainQueryBusiness)) {
                    log.info("请求->" + JsonUtils.toJson(msg));
                }

                if (request.getCmd().equals(IAccessTypeConstants.ACCESSS_TimeSlotRemainQueryBusiness)) {
                    log.info("请求->" + JsonUtils.toJson(msg));
                }
            }

        }

        return null; // 返回null将不发送数据到server
    }
}
