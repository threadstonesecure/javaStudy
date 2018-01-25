package watchhdp;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;

/**
 * Created by denglt on 16/10/17.
 */
public  abstract class DataHandler {
    public ResultPack dealData(Object msg) {
        if (filter(msg)) return null;
        return deal(msg);
    }

    protected abstract ResultPack deal(Object msg);

    public boolean filter(Object msg) {
        if (msg instanceof RequestPack) {
            RequestPack request = (RequestPack) msg;
            return request.getBody().contains("{\"monitor\":\"true\"");
        }
        return false;
    }
}
