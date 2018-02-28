package watchhdp;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import dlt.utils.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LookupDataHandler extends DataHandler {
    private static Log log = LogFactory.getLog(LookupDataHandler.class);

    @Override
    public boolean filter(Object msg) {
        if (super.filter(msg)) {
            if (msg instanceof RequestPack) {
                RequestPack request = (RequestPack) msg;
                return request.getHosId().equals("226");
            }
            if (msg instanceof ResultPack) {
                ResultPack resultPack = (ResultPack) msg;
                return resultPack.getHosId().equals("226");
            }
        }
        return false;
    }

    @Override
    public ResultPack deal(Object msg) {
        String data = JsonUtils.toJson(msg);
        if (data.contains("003451") || data.contains("RealTimeSchQuery"))
            log.info((msg instanceof RequestPack ? "request->" : "result->") + data);
        return null; // 返回null将不发送数据到server
    }
}
