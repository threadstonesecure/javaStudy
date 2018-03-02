package dlt.study.concurrent.future;

import dlt.study.log4j.Log;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {

    @Test
    public void runAsync() {
        //  CompletableFuture.runAsync();
    }

    @Test
    public void supplyAsync() throws Exception {
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
            try {
                return new MyCall().call();
            } catch (Exception e) {
                //  e.printStackTrace();
                throw new RuntimeException(e);
            }
            //return 0;
        });
        cf.whenCompleteAsync((u, e) -> Log.info("result async ->" + u));
        cf.whenComplete((u, e) -> Log.info("result sync 1 ->" + u));
        //cf.whenComplete((u,t) -> Log.info("result sync 2->" + u));
        CompletableFuture<Integer> cfExcep = cf.exceptionally((e) -> {
            Log.info("exception -> " + e);
            return -1;
        });

        CompletableFuture<String> stringCompletableFuture = cf.thenApply((u) -> "result = " + u); // 可以进行数据转换

        Log.info("result  main ->" +  cf.get());
        Log.info("result exception -> " + cfExcep.get());
        System.out.println(cfExcep.isCompletedExceptionally());
        System.out.println(stringCompletableFuture.get());

    }

}


class MyCall implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Log.info("run MyClass");
        int n = 0;
        for (int i = 1; i <= 100000; i++) {
            // Thread.sleep(10);
            if (Thread.interrupted()) {   //响应中断
                throw new InterruptedException();
            }
            n = n + i;
          // if (n > 20000) throw new InterruptedException();
        }
        Log.info("计算完毕：" + n);
        return n;
    }
}
