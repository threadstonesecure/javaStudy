package weixin.resp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by denglt on 2015/11/17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseResponse {

    // 开发者微信号
    @XmlElement(name="ToUserName")
    private String toUserName;
    // 发送方帐号（一个OpenID）
    @XmlElement(name="FromUserName")
    private String fromUserName;
    // 消息创建时间 （整型）
    @XmlElement(name="CreateTime")
    private long createTime;
    // 消息类型
    @XmlElement(name="MsgType")
    private String msgType;

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

    @Override
    public String toString() {
        return "BaseResponse{" +
                "toUserName='" + toUserName + '\'' +
                ", fromUserName='" + fromUserName + '\'' +
                ", createTime=" + createTime +
                ", msgType='" + msgType + '\'' +
                '}';
    }
}
