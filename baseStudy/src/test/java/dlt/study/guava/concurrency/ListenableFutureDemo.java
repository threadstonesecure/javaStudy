package dlt.study.guava.concurrency;

import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.MoreExecutors;
import dlt.study.log4j.Log;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class ListenableFutureDemo {


    /**
     * 监听task完成
     *
     * @throws Exceptionn
     */
    @Test
    public void listenableFutureTask() throws Exception {
        ListenableFutureTask<Integer> task = Tasks.newTask();
        task.addListener(() -> {
            try {
                Log.info("计算完毕，结果:" + task.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }, MoreExecutors.directExecutor() /*如果task没有执行完毕，将使用task运行的thread执行listener*/);
        task.addListener(() -> {
            try {
                Log.info("计算完毕，结果2:" + task.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }, MoreExecutors.directExecutor());

        new Thread(task).start();
        System.in.read();
    }
}
