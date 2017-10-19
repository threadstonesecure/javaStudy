package weixin.resp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by denglt on 2015/11/17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class VideoResponse extends BaseResponse {

    @XmlElement(name="Video")
    private Video video;

    public VideoResponse(){
        setMsgType("video");
    }
    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
