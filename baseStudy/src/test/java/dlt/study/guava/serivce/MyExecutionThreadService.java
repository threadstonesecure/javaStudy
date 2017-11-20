package dlt.study.guava.serivce;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import dlt.study.guava.concurrency.Tasks;
import dlt.study.log4j.Log;

import java.util.concurrent.Executor;

public class MyExecutionThreadService extends AbstractExecutionThreadService {


    @Override
    protected void startUp() throws Exception {
        Log.info("MyExecutionThreadService->startUp()");
    }

    @Override
    protected void shutDown() throws Exception {
        Log.info("MyExecutionThreadService->shutDown()");
    }

    @Override
    protected void triggerShutdown() {
        Log.info("MyExecutionThreadService->triggerShutdown()");
    }

    @Override
    protected Executor executor() {
        return super.executor();
    }

    @Override
    protected void run() throws Exception {
        Log.info("MyExecutionThreadService->开始计算");
        Thread.sleep(6000); // 延时执行
        int sum = 0;
        for (int i = 1; i < 10000; i++) {
            if (this.state() == State.STOPPING)
                break;
            sum = sum + i;
        }
        if (this.state() == State.STOPPING)
            Log.info("MyExecutionThreadService->计算被终止");
        else{
            Log.info("MyExecutionThreadService->计算完成");
            Log.info("MyExecutionThreadService->计算结果：" + sum);
        }
    }
}
