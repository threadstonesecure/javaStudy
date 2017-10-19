package application.req;

import org.springframework.stereotype.Service;
import weixin.req.Event;
import weixin.req.RequestKind;
import weixin.resp.BaseResponse;
import weixin.resp.TextResponse;
import weixin.webapi.WxUtil;

/**
 * Created by denglt on 2015/12/2.
 */
@Service
public class EventAction implements Action<Event> {
	@Override
	public boolean canDo(RequestKind kind) {
		return RequestKind.EVENT == kind;
	}

	@Override
	public BaseResponse doAction(Event request) {
		switch (request.getEvent()) {
		case "subscribe": {
			TextResponse response = WxUtil.createResponse(request,
					TextResponse.class);
			if (request.getEventKey() != null) { // 未关注用户扫描二维码
				// TODO
			} else
				response.setContent("感谢关注！敬请期待^-^");

			return response;
		}
		case "unsubscribe": { // 取消关注
			String username = request.getFromUserName();
			System.out.println(username + " leave !");
			return null;
		}
		case "SCAN": { // 已关注用户扫描二维码
			// TODO
			return null;
		}
		case "LOCATION": { // 上报地理位置
			double latitude = request.getLatitude();
			double longitude = request.getLatitude();
			System.out.println(request.getFromUserName()
					+ String.format(" locate in (%d,%d)", latitude, longitude));
			return null;
		}
		default: {
			TextResponse response = WxUtil.createResponse(request,
					TextResponse.class);
			response.setContent("感谢关注！敬请期待^-^");
			return response;
		}
		}
	}
}
