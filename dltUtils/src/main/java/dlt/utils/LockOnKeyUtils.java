package dlt.utils;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 根据关键key进行锁同步操作
 * 实现：全局一个lock，通过Condition控制同步操作
 * 
 * Created by denglt on 2017/8/22.
 */
public class LockOnKeyUtils {

    private static final Lock lock = new ReentrantLock();
    private static final Map<Object, ConditionWapper> conditionMap = new WeakHashMap<>();
    private static final ConditionWapper emptyCondition = new ConditionWapper(null);

    /**
     * Acquires the lock
     * 使用 Condition 进行 wait
     * @param lockKey
     */
    public static void lock(Object lockKey) {
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

    public static boolean tryLock(Object  lockKey) {
        try {
            lock.lock();
            ConditionWapper condition = conditionMap.get(TryKeyWapper.wapper(lockKey));
            if (condition == null) {
                conditionMap.put(lockKey, emptyCondition);
            }
            return condition == null ? true : false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * release lock 
     * 
     * 使用 Condition 进行 signal 
     * @param lockKey
     */
    public void unlock(Object lockKey) {
        try {
            lock.lock();
            if (conditionMap.remove(TryKeyWapper.wapper(lockKey)) != null) {
                return;
            }
            ConditionWapper condition = conditionMap.get(lockKey);
            if (condition != null)
                condition.signal();
        } finally {
            lock.unlock();
        }
    }

    private static ConditionWapper newCondition() {
        Condition condition = lock.newCondition();
        return new ConditionWapper(condition);
    }



    private static class ConditionWapper {
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

    private static class TryKeyWapper{
        private Object lockKey;

        private TryKeyWapper(Object lockKey){
           this.lockKey = lockKey;
        }

        public static TryKeyWapper wapper(Object lockKey){
            return new TryKeyWapper(lockKey);
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TryKeyWapper that = (TryKeyWapper) o;

            return lockKey != null ? lockKey.equals(that.lockKey) : that.lockKey == null;
        }

        @Override
        public int hashCode() {
            return lockKey != null ? lockKey.hashCode() : 0;
        }
    }
}
