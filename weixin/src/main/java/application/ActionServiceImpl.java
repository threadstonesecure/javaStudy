package application;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weixin.CoreService;
import weixin.req.BaseRequest;
import weixin.resp.BaseResponse;
import application.req.Action;

/**
 * Created by denglt on 2015/12/1.
 */

public class ActionServiceImpl implements CoreService {
    private static Log log = LogFactory.getLog(ActionServiceImpl.class);
	private List<Action> actions;

	@Override
	public BaseResponse process(BaseRequest request) {
        log.info("正在处理请求:" + request);
		for (Action action : actions) {
			if (action.canDo(request.getRequestKind())) {
				BaseResponse respMessage = action.doAction(request);
				return respMessage;
			}
		}
        log.info("not action process the request:" + request);
		return null;
	}



    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
