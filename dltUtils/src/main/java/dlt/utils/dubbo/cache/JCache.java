package dlt.utils.dubbo.cache;

import javax.cache.Cache;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by denglt on 16/10/21.
 */
public class JCache implements ExCache {

    private Cache<Object, Object> cache;
    private ReentrantLock lock = new ReentrantLock();
    private AbstractCacheFactory cacheFactory;

    public JCache(Cache cache) {
        this(cache, null);
    }

    public JCache(Cache cache, AbstractCacheFactory cacheFactory) {
        this.cache = cache;
        this.cacheFactory = cacheFactory;
    }

    @Override
    public void put(Object key, Object value) {
        try {
            lock.lock();
            cache.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Object get(Object key) {
        try {
            lock.lock();
            return cache.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            cache.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void remove(Object key) {
        try {
            lock.lock();
            cache.remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long size() {
        try {
            lock.lock();
            long i = 0;
            Iterator iterator = cache.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                i++;
            }
            return i;
        } finally {
            lock.unlock();
        }

    }

    @Override
    public List<Object> getKeys(int top) {
        try {
            lock.lock();
            int i = 0;
            List<Object> keys = new ArrayList<>();
            Iterator<Cache.Entry<Object, Object>> iterator = cache.iterator();
            while (iterator.hasNext() && i < top) {
                Cache.Entry entry = iterator.next();
                keys.add(entry.getKey());
                i++;
            }
            return keys;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<Object, Object> get(int top) {
        try {
            lock.lock();
            int i = 0;
            Map<Object, Object> keyValues = new HashMap();
            Iterator<Cache.Entry<Object, Object>> iterator = cache.iterator();
            while (iterator.hasNext() && i < top) {
                Cache.Entry entry = iterator.next();
                keyValues.put(entry.getKey(), entry.getValue());
                i++;
            }
            return keyValues;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            lock.lock();
            return cache.containsKey(key);
        } finally {
            lock.unlock();
        }

    }
}
