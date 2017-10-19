package dlt.study.guava.concurrency;

import com.google.common.util.concurrent.ListenableFutureTask;
import dlt.study.log4j.Log;

import java.util.concurrent.Callable;

public class Tasks {

    public static ListenableFutureTask<Integer> newTask() {
        ListenableFutureTask<Integer> task = ListenableFutureTask.create(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Log.info("开始计算");
                int sum = 0;
                for (int i = 1; i < 10000; i++)
                    sum = sum + i;
                Log.info("计算完成");
                return sum;
            }
        });
        return task;
    }

    public static Callable<Integer> newCallable() {
        return new Callable() {
            @Override
            public Integer call() throws Exception {
                Log.info("开始计算");
                Thread.sleep(6000); // 延时执行
                int sum = 0;
                for (int i = 1; i < 10000; i++)
                    sum = sum + i;
                Log.info("计算完成");
                return sum;
            }
        };
    }

    public static Runnable newRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                Log.info("开始计算");
                try {
                    Thread.sleep(6000); // 延时执行
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                int sum = 0;
                for (int i = 1; i < 10000; i++)
                    sum = sum + i;
                Log.info("计算完成! result=" + sum);
            }
        };
    }
}
