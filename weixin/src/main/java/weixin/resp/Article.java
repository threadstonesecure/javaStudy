package weixin.resp;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by denglt on 2015/11/17.
 */
public class Article {
    @XmlElement(name="Items")
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
