package dlt.utils.dubbo.cache;

import com.alibaba.dubbo.cache.Cache;

import java.util.List;
import java.util.Map;

/**
 * Created by denglt on 2016/11/8.
 */
public interface ExCache extends Cache {

    void clear();

    void remove(Object key);

    long size();

    List<Object> getKeys(int top);

    Map<Object,Object> get(int top);

    boolean containsKey(Object key);

}
