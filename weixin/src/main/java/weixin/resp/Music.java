package weixin.resp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by denglt on 2015/11/17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Music {

    //	否	音乐标题
    @XmlElement(name="Title")
    private String title;
    //	否	音乐描述
    @XmlElement(name="Description")
    private String description;
    //	否	音乐链接
    @XmlElement(name="MusicURL")
    private String musicURL;

    //	否	高质量音乐链接，WIFI环境优先使用该链接播放音乐
    @XmlElement(name="HQMusicUrl")
    private String HQMusicUrl;
    //	否	缩略图的媒体id，通过素材管理接口上传多媒体文件，得到的id
    @XmlElement(name="ThumbMediaId")
    private String thumbMediaId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMusicURL() {
        return musicURL;
    }

    public void setMusicURL(String musicURL) {
        this.musicURL = musicURL;
    }

    public String getHQMusicUrl() {
        return HQMusicUrl;
    }

    public void setHQMusicUrl(String HQMusicUrl) {
        this.HQMusicUrl = HQMusicUrl;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
