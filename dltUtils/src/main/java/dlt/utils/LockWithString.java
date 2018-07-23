package dlt.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 根据字符串关键字进行锁同步操作
 * Created by denglt on 2016/6/2.
 */
public class LockWithString {
    public static LockWithString GLOBAL_LOCK = new LockWithString();
    private Lock lock = new ReentrantLock();
    private Map<String, ConditionWapper> conditionMap = new HashMap<>();
    private final String TRYLOCK_PREFIX = "TRYLOCK-";

    public LockWithString() {

    }

    /**
     * 加锁
     *
     * @param lockKey
     */
    public void lock(String lockKey) {
        try {
            lock.lock();
            ConditionWapper condition = conditionMap.get(lockKey);
            if (condition == null) {
                condition = newCondition(lockKey);
                conditionMap.put(lockKey, condition);
            }
            condition.await();
        } finally {
            lock.unlock();
        }

    }


    public boolean tryLock(String lockKey) {
        lockKey = TRYLOCK_PREFIX + lockKey;
        try {
            lock.lock();
            ConditionWapper condition = conditionMap.get(lockKey);
            if (condition == null) {
                conditionMap.put(lockKey, emptyCondition);
            }
            return condition == null ? true : false;
        } finally {
            lock.unlock();
        }
    }

    public void unlock(String lockKey) {
        try {
            lock.lock();
            if (conditionMap.remove(TRYLOCK_PREFIX + lockKey) != null) {
                return;
            }
            ConditionWapper condition = conditionMap.get(lockKey);
            if (condition != null)
                condition.signal();
        } finally {
            lock.unlock();
        }
    }

    private ConditionWapper newCondition(String lockKey) {
        Condition condition = lock.newCondition();
        return new ConditionWapper(lockKey, condition);
    }

    private ConditionWapper emptyCondition = new ConditionWapper(null, null);

    private class ConditionWapper {
        private Condition condition;
        private String lockKey;
        private int count = 0;

        public ConditionWapper(String key, Condition condition) {
            this.lockKey = key;
            this.condition = condition;
        }


        public void await() {
            count++;
            if (count > 1) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void signal() {
            if (count > 0)
                count--;
            if (count == 0 && lockKey != null) conditionMap.remove(lockKey);
            condition.signal();
        }
    }

}
