package application.req;

import org.springframework.stereotype.Service;
import weixin.req.RequestKind;
import weixin.req.TextRequest;
import weixin.resp.BaseResponse;
import weixin.resp.TextResponse;
import weixin.webapi.WxUtil;

/**
 * Created by denglt on 2015/12/1.
 */
@Service
public class TextAction implements Action<TextRequest> {
	@Override
	public boolean canDo(RequestKind kind) {
		return RequestKind.TEXT == kind;
	}

	@Override
	public BaseResponse doAction(TextRequest request) {
		String content = request.getContent();
		TextResponse response = WxUtil.createResponse(request,
				TextResponse.class);
		response.setContent(content + " from Server");
		return response;
	}
}
