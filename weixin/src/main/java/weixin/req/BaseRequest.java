package weixin.req;

/**
 * Created by denglt on 2015/11/17.
 */
public class BaseRequest {
	// 开发者微信号
	private String toUserName;
	// 发送方帐号（一个OpenID）
	private String fromUserName;
	// 消息创建时间 （整型）
	private long createTime;
	// 消息类型
	private String msgType;
	// 消息id
	private String msgId;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public RequestKind getRequestKind() {
		return RequestKind.getType(msgType);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("BaseRequest{");
		sb.append("fromUserName='").append(fromUserName).append('\'');
		sb.append(", toUserName='").append(toUserName).append('\'');
		sb.append(", createTime=").append(createTime);
		sb.append(", msgType='").append(msgType).append('\'');
		sb.append(", msgId='").append(msgId).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
