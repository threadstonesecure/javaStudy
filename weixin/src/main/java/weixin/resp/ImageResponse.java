package weixin.resp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by denglt on 2015/11/17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ImageResponse extends BaseResponse {

    @XmlElement(name="Image")
    private Image image;
    public ImageResponse(){
        setMsgType("image");
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
