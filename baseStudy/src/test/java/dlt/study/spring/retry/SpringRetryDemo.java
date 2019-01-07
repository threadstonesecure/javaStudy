package dlt.study.spring.retry;

import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Collections;
import java.util.concurrent.Callable;

/**
 * @Description:
 * @Package: dlt.study.spring.retry
 * @Author: denglt
 * @Date: 2019/1/7 11:08 AM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class SpringRetryDemo {

    public static void main(String[] args) throws Exception {
        // 构建重试模板实例
        RetryTemplate retryTemplate = new RetryTemplate();
        // 设置重试策略，主要设置重试次数
        SimpleRetryPolicy policy = new SimpleRetryPolicy(3, Collections.<Class<? extends Throwable>, Boolean>singletonMap(Exception.class, true));
        // 设置重试回退操作策略，主要设置重试间隔时间
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(100);  // milliseconds
        retryTemplate.setRetryPolicy(policy);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);


        // 通过RetryCallback 重试回调实例包装正常逻辑逻辑，第一次执行和重试执行执行的都是这段逻辑
        //RetryContext 重试操作上下文约定，统一spring-try包装
        MyRunner myRunner = new MyRunner(4);
        final RetryCallback<String, Exception> retryCallback = context -> {
            System.out.println("the number of retries :" + context.getRetryCount());
            String result = myRunner.call();
            return result;
        };


        // 通过RecoveryCallback 重试流程正常结束或者达到重试上限后的退出恢复操作实例
        final RecoveryCallback<String> recoveryCallback = context -> {
            System.out.println("do recory operation");
            return "RecoveryCallback do";
        };

        // 由retryTemplate 执行execute方法开始逻辑执行
        String result = retryTemplate.execute(retryCallback, recoveryCallback);
        System.out.println("myrunner result is " + result);

    }
}

class MyRunner implements Callable<String> {

    private int succ;
    private int count = 0;

    public MyRunner(int succCount) {
        this.succ = succCount;
    }

    @Override
    public String call() throws Exception {

        if (++count < succ) {
            System.out.println("run on " + count);
            throw new Exception("run fail");
        }
        return "run succ on " + count;
    }
}
