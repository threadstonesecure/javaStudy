package weixin.req;

/**
 * Created by denglt on 2015/11/17.
 */
public class VideoRequest extends BaseRequest {
    //	视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
    private String mediaId;
    //	视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
    private String thumbMediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
