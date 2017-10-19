package dlt.utils;

import java.util.Map;
import java.util.WeakHashMap;
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
    private Map<String, ConditionWapper> conditionMap = new WeakHashMap<>();
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
                condition = newCondition();
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

    private ConditionWapper newCondition() {
        Condition condition = lock.newCondition();
        return new ConditionWapper(condition);
    }

    private ConditionWapper emptyCondition = new ConditionWapper(null);

    private class ConditionWapper {
        private Condition condition;
        private int count = 0;

        public ConditionWapper(Condition condition) {
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
            condition.signal();
        }
    }

}
