package dlt.study.netflix.hystrix;

import com.netflix.hystrix.*;
import dlt.study.log4j.Log;

public class CommandHelloWorld extends HystrixCommand<String> {
    private String name;
    private int runtime = 0;


    public CommandHelloWorld(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CommandHelloWorld"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("CommandHelloWorld"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("CommandHelloWorld")) // 线程池Key，
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties.Setter()
                                .withCoreSize(10) // This property sets the core thread-pool size.
                                .withMaximumSize(10) //This property sets the maximum thread-pool size.
                                .withMaxQueueSize(100) //This property sets the maximum queue size of the BlockingQueue implementation.
                                //default -1   //If you set this to -1 then SynchronousQueue will be used, otherwise a positive value will be used with LinkedBlockingQueue.
                                //Please note that this setting only takes effect if you also set allowMaximumSizeToDivergeFromCoreSize
                                .withQueueSizeRejectionThreshold(5) // default 5. This property is not applicable if maxQueueSize == -1.
                                .withKeepAliveTimeMinutes(1)  // this property controls how long a thread will go unused before being released
                                .withAllowMaximumSizeToDivergeFromCoreSize(true) // default false
                                .withMetricsRollingStatisticalWindowInMilliseconds(10000)
                                .withMetricsRollingStatisticalWindowBuckets(10)
                )
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                // Execution
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)// 隔离方式
                                .withExecutionTimeoutEnabled(true)
                                .withExecutionTimeoutInMilliseconds(1000)  // default 1000
                                .withExecutionIsolationThreadInterruptOnTimeout(true)  //发生超时时是否应该中断
                                .withExecutionIsolationThreadInterruptOnFutureCancel(false) // default false /发生取消时是否应中断
                                .withExecutionIsolationSemaphoreMaxConcurrentRequests(10) //default 10 This property sets the maximum number of requests allowed to a HystrixCommand.run() method when you are using ExecutionIsolationStrategy.SEMAPHORE.
                                //If this maximum concurrent limit is hit then subsequent requests will be rejected.
                                //Fallback
                                .withFallbackEnabled(true)
                                .withFallbackIsolationSemaphoreMaxConcurrentRequests(10) // default 10
                                // Circuit Breaker
                                .withCircuitBreakerEnabled(true)  // 启动熔断器
                                .withCircuitBreakerRequestVolumeThreshold(20) // 10秒钟内至少20此请求失败，  default 20 (This property sets the minimum number of requests in a rolling window that will trip the circuit.)
                                .withCircuitBreakerSleepWindowInMilliseconds(5000) //default 5000, 熔断跳闸后的5000毫秒内拒绝请求, 5s后进入半打开状态,放部分流量过去重试
                                .withCircuitBreakerErrorThresholdPercentage(50) // 错误率达到50开启熔断保护 default 50
                                .withCircuitBreakerForceOpen(false) // default false   是否强行启动断开，拒绝所有request
                                .withCircuitBreakerForceClosed(false) // default false  是否强行关闭断开，接受request
                                // Metrics
                                .withMetricsRollingStatisticalWindowInMilliseconds(10000) // defalut 10s
                                .withMetricsRollingStatisticalWindowBuckets(10) // defalut 10 个 (metrics.rollingStats.timeInMilliseconds % metrics.rollingStats.numBuckets == 0)
                                //Request Context
                                .withRequestCacheEnabled(true)  // default true
                                .withRequestLogEnabled(true) // default true
                )
        );
        this.name = name;
    }

    public CommandHelloWorld(String name, int runtime, int maxSemaphore) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ExampleGroup"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(3))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(6000)
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(maxSemaphore) // default 10
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE) //为SEMAPHORE时，在调用线程执行run(),性能更好点
                ));
        this.name = name;
        this.runtime = runtime;
    }


    public CommandHelloWorld(String name, int runtime, int coreSize, int maxQueueSize) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                //.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(name))
                .andThreadPoolPropertiesDefaults( // 属性设置 对应ThreadPoolKey
                        HystrixThreadPoolProperties.Setter()
                                .withCoreSize(coreSize)/*.withMaximumSize(maxSize)*/
                                .withMaxQueueSize(maxQueueSize))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionTimeoutInMilliseconds(6000)
                ));
        this.name = name;
        this.runtime = runtime;
    }


    public CommandHelloWorld(String name, int runtime) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(6000)));
        this.name = name;
        this.runtime = runtime;
    }

    @Override
    protected String run() {
        Log.info("CommandHelloWorld is running! on " + name);
        try {
            Thread.sleep(runtime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.info("CommandHelloWorld finish! on " + name);
        return "Hello " + name + "!";
    }


    /**
     * run()失败时，返回结果 (  HystrixBadRequestException  cannot trigger fallback logic !)
     *
     * @return
     */
    @Override
    protected String getFallback() {
        Log.info("CommandHelloWorld -> getFallback ! on " + name);
        return "Hello Failure " + name + "!";
    }

    @Override
    protected String getCacheKey() {
        return name;
    }
}
