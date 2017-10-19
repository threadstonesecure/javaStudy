package weixin.req;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by denglt on 2015/11/30.
 */
public enum RequestKind {
	TEXT("text", TextRequest.class), IMAGE("image", ImageRequest.class), VOICE(
			"voice", VoiceRequest.class), VIDEO("video", VideoRequest.class), SHORTVIDEO(
			"shortvideo", ShortvideoRequest.class), LOCATION("location",
			LocationRequest.class), LINK("link", LinkRequest.class), EVENT(
			"event", Event.class);
	private static Log log = LogFactory.getLog(RequestKind.class);

	private String type;
	private Class<? extends BaseRequest> beanClass;

	private RequestKind(String type, Class<? extends BaseRequest> aclass) {
		this.type = type;
		this.beanClass = aclass;
	}

	public static RequestKind getType(String msgType) {
		RequestKind[] kinds = RequestKind.values();
		for (RequestKind kind : kinds) {
			if (kind.type.equals(msgType)) {
				return kind;
			}
		}
		throw new RuntimeException("不支持的消息类型");
	}

	public BaseRequest newMessage() {
		try {
			BaseRequest message = beanClass.newInstance();
			message.setMsgType(type);
			return message;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}
}
