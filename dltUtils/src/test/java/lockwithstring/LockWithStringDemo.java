package lockwithstring;

import dlt.utils.LockWithString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by denglt on 2016/6/1.
 */
public class LockWithStringDemo {
    public static void main(String[] args) {
        ExecutorService tpe = Executors.newFixedThreadPool(10);
            for (int i = 0; i < 10; i++) {
                for (int schId = 1; schId <= 1; schId++) {
                    tpe.execute(new PatReg("pat" + 0, schId));
                    tpe.execute(new PatReg("pat" + 1, schId));
                }
            }

    }
}

class PatReg implements Runnable {
    private static Log log = LogFactory.getLog(PatReg.class);
    private String patKey;
    private long schId;

    public PatReg(String patKey, long schId) {
        this.patKey = patKey;
        this.schId = schId;
    }

    @Override
    public void run() {
        LockWithString lock = LockWithString.GLOBAL_LOCK;
        try {
            lock.lock(patKey + "--" + schId);
            log.info(String.format("患者[%s]开始进行排班[%d]预约。。。", patKey, schId));

            Thread.sleep(1000 * 30);

            log.info(String.format("患者[%s]完成排班[%d]预约！", patKey, schId));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock(patKey + "--" + schId);
        }
    }
}
