package dlt.study.netflix.hystrix;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import dlt.study.log4j.Log;

public class LogCommandExecutionHook extends HystrixCommandExecutionHook {

    private static HystrixCommandExecutionHook instance = new LogCommandExecutionHook();

    public static HystrixCommandExecutionHook get() {
        return instance;
    }

    @Override
    public <T> void onStart(HystrixInvokable<T> commandInstance) {
        Log.info("onStart :" + commandInstance);
    }

    @Override
    public <T> T onEmit(HystrixInvokable<T> commandInstance, T value) {
        return super.onEmit(commandInstance, value);
    }
}
