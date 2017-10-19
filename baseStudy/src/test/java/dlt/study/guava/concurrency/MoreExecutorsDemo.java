package dlt.study.guava.concurrency;

import com.google.common.util.concurrent.*;
import dlt.study.log4j.Log;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.concurrent.*;


public class MoreExecutorsDemo {


    @Test
    public void directExecutor() throws Exception {
        Executor executor = MoreExecutors.directExecutor();
        ListenableFutureTask<Integer> task = Tasks.newTask();
        executor.execute(task);
        System.out.println("计算结果：" + task.get());
    }

    @Test
    public void listeningDecorator() throws Exception {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));
       // ListeningScheduledExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newScheduledThreadPool(5));
        System.out.println(listeningExecutorService);
        ListenableFuture<Integer> listenableFuture = listeningExecutorService.submit(Tasks.newCallable());
        listenableFuture.addListener(() -> {
            try {
                Log.info("计算完毕，结果:" + listenableFuture.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }, MoreExecutors.directExecutor());

        Futures.addCallback(listenableFuture, new FutureCallback<Integer>() {
            @Override
            public void onSuccess(@Nullable Integer result) {
                Log.info("计算完毕，结果2:" + result);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.error(t);
            }
        }, MoreExecutors.directExecutor());

        System.in.read();
       // MoreExecutors.listeningDecorator()
    }


}


