package dlt.study.jcache;

import java.io.Serializable;

/**
 * 解决缓存null值问题
 * Created by denglt on 2016/10/28.
 */
public final class NullValue implements Serializable {
    private static final long serialVersionUID = 3275611004748969018L;
    static final NullValue INSTANCE = new NullValue();

    private NullValue() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() == o.getClass()) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return ("" + serialVersionUID).hashCode();
    }
}
