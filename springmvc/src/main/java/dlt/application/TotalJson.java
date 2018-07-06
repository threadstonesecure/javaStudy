package dlt.application;

import java.io.Serializable;
import java.util.List;

public class TotalJson  implements Serializable {
    private static final long serialVersionUID = -5622834524793130935L;
    private long results;//数据长度
    private List items;//数据条目

    public long getResults() {
        return results;
    }
    public void setResults(long results) {
        this.results = results;
    }

    public List getItems() {
        return items;
    }
    public void setItems(List items) {
        this.items = items;
    }
}