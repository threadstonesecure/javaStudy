package weixin.resp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by denglt on 2015/11/17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class NewsResponse extends BaseResponse {


    // 图文消息个数，限制为10条以内
    @XmlElement(name="ArticleCount")
    private int articleCount;

    @XmlElement(name="Articles")
    private List<Article> articles;

    public NewsResponse() {
        setMsgType("news");
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        this.articleCount = articles.size();
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }
}
