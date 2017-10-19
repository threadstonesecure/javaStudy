package weixin.resp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by denglt on 2015/11/17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TextResponse extends BaseResponse {

    @XmlElement(name="Content")
	private String content;

	public TextResponse() {
		setMsgType("text");
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    @Override
    public String toString() {
        return "TextResponse{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
