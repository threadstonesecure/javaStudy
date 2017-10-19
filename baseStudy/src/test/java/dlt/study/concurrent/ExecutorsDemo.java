package dlt.study.concurrent;

import java.util.concurrent.*;

import dlt.infrastructure.ThreadOut;
import dlt.study.guava.concurrency.Tasks;
import org.junit.Test;

public class ExecutorsDemo {

    @Test
    public void newFixedThreadPool() throws Exception {

        ExecutorService tpe = Executors.newFixedThreadPool(5);
        tpe.execute(Tasks.newRunnable());
        //  tpe.shutdown();
        System.out.println(tpe.awaitTermination(1, TimeUnit.MINUTES));
        System.out.println("finish!");
    }

    @Test
    public void newFixedThreadPool2() throws Exception {

        ExecutorService tpe = Executors.newFixedThreadPool(5);
        Future<Integer> future = tpe.submit(Tasks.newCallable());
        System.out.println("计算结果 = " + future.get());
        tpe.shutdown();

        System.out.println("finish!");
    }

    @Test
    public void newScheduledThreadPool() throws Exception {
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(5);
       // Future<Integer> future = scheduled.submit(Tasks.newCallable());
       // System.out.println("计算结果 = " + future.get());
        //ScheduledFuture<?> scheduledFuture = scheduled.scheduleAtFixedRate(Tasks.newRunnable(), 0, 10, TimeUnit.SECONDS);
        ScheduledFuture<?> scheduledFuture = scheduled.scheduleWithFixedDelay(Tasks.newRunnable(), 0, 10, TimeUnit.SECONDS);
        System.out.println("计算结果 = " + scheduledFuture.get());
    }

}
