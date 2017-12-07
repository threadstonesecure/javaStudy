package watchhdp;

import com.yuntai.hdp.access.ResultPack;
import dlt.utils.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogDataHandler implements DataHandler {
    private static Log log = LogFactory.getLog(LogDataHandler.class);
    @Override
    public ResultPack dealData(Object msg) {
        log.info( msg.getClass().getSimpleName() + ":" + JsonUtils.toJson(msg));
        return null;
    }
}
