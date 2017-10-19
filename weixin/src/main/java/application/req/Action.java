package application.req;

import weixin.req.RequestKind;
import weixin.resp.BaseResponse;

/**
 * Created by denglt on 2015/12/1.
 */
public interface Action<T> {

	public boolean canDo(RequestKind kind);

	public BaseResponse doAction(T request);
}
