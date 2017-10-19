package dlt.study.guava.serivce;

import com.google.common.util.concurrent.AbstractScheduledService;
import dlt.study.guava.concurrency.Tasks;
import dlt.study.log4j.Log;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 调度程序服务
 */
public class MyScheduledService extends AbstractScheduledService {

    @Override
    protected void startUp() throws Exception {
        Log.info("MyScheduledService startUp()");
    }

    @Override
    protected void shutDown() throws Exception {
        Log.info("MyScheduledService shutDown()");
    }

    @Override
    protected void runOneIteration() throws Exception {
        Tasks.newRunnable().run();
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0,10, TimeUnit.SECONDS);
    }

    @Override
    protected ScheduledExecutorService executor() {
        // 可以在这儿配置 schedule
        return super.executor();
    }
}
