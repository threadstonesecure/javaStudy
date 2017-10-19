package weixin.req;

/**
 * Created by denglt on 2015/11/17.
 */
public class TextRequest extends BaseRequest {
	// 消息内容
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    @Override
    public String toString() {
        return "TextRequest{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
