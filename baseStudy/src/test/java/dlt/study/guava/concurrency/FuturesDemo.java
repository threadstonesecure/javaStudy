package dlt.study.guava.concurrency;

import com.google.common.util.concurrent.*;
import dlt.study.log4j.Log;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FuturesDemo {

    @Test
    public void addCallback() throws Exception {
        ListenableFutureTask<Integer> task = Tasks.newTask();  //ListenableFutureTask.create

        Futures.addCallback(task, new FutureCallback<Integer>() {
            @Override
            public void onSuccess(@Nullable Integer result) {
                Log.info("计算完毕，结果:" + result);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.error(t);
            }
        }, MoreExecutors.directExecutor());

        new Thread(task).start();

        Thread.sleep(10000);

        //Futures.immediateCancelledFuture()
        //Futures.getDone()
        //Futures.immediateFuture()

    }

    @Test
    public void allAsList() throws ExecutionException, InterruptedException {  // 合并Future的结果


        /**
         * Returns a ListenableFuture whose value is a list containing the values of each of the input futures, in order.
         * If any of the input futures fails or is cancelled, this concurrency fails or is cancelled.
         */
        //  Futures.allAsList()
        ListenableFutureTask<Integer> task = Tasks.newTask();
        ListenableFutureTask<Integer> task2 = Tasks.newTask();
        ListenableFutureTask<Integer> task3 = Tasks.newTask();
        new Thread(task).start();
        new Thread(task2).start();
        new Thread(task3).start();
        ListenableFuture<List<Integer>> listListenableFuture = Futures.allAsList(task, task2, task3);
        List<Integer> integers = null;
        try {
            integers = listListenableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        integers.forEach(System.out::println);
        /**
         * Returns a ListenableFuture whose value is a list containing the values of each of the successful input futures, in order.
         * The values corresponding to failed or cancelled futures are replaced with null.
         */
         // Futures.successfulAsList()

    }

    @Test
    public void getChecked() { //检查Future,抛出自己定义的错误
         //Futures.getChecked()
       // Futures.makeChecked()
    }


    @Test
    public void transform() throws Exception {
        ListenableFutureTask<Integer> task = Tasks.newTask();
        ListenableFuture<String> stringListenableFuture = Futures.transform(task, (i) -> {
            Log.info("转换结果");
            return "结果：" + i;
        }, MoreExecutors.directExecutor());
        stringListenableFuture.addListener(() -> {

            try {
                Log.info(stringListenableFuture.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
        new Thread(task).start();
        System.out.println(stringListenableFuture.get());
    }

    @Test
    public void transformAsync() {
        //Futures.transformAsync()
    }

}
