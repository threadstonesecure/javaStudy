package weixin.req;

/**
 * Created by denglt on 2015/11/17.
 */
public class VoiceRequest extends BaseRequest {
	// 语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String mediaId;
	// 语音格式，如amr，speex等
	private String format;

	// 开通语音识别后，将有该字段信息  Recognition为语音识别结果，使用UTF8编码。
	private String Recongnition;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRecongnition() {
		return Recongnition;
	}

	public void setRecongnition(String recongnition) {
		this.Recongnition = recongnition;
	}
}
