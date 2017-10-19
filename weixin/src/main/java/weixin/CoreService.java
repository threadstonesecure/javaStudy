package weixin;

import weixin.req.BaseRequest;
import weixin.resp.BaseResponse;

/**
 * Created by denglt on 2015/12/1.
 */
public interface CoreService {

     public BaseResponse process(BaseRequest reqeust);
}
