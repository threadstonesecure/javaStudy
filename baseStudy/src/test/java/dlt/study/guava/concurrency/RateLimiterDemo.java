package dlt.study.guava.concurrency;

import com.google.common.util.concurrent.RateLimiter;
import dlt.study.log4j.Log;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;
// 令牌桶算法(Token Bucket)和 Leaky Bucket(漏桶) 效果一样但方向相反的算法,更加容易理解.
// 随着时间流逝,系统会按恒定1/QPS时间间隔(如果QPS=100,则间隔是10ms)往桶里加入Token(想象和漏洞漏水相反,有个水龙头在不断的加水),如果桶已经满了就不再加了.新请求来临时,会各自拿走一个Token,如果没有Token可拿了就阻塞或者拒绝服务.

/**
 * RateLimiter使用的是一种叫令牌桶的流控算法，RateLimiter会按照一定的频率往桶里扔令牌，线程拿到令牌才能执行，
 * 比如你希望自己的应用程序QPS不要超过1000，那么RateLimiter设置1000的速率后，就会每秒往桶里扔1000个令牌。
 * <p>
 * RateLimiter经常用于限制对一些物理资源或者逻辑资源的访问速率。
 * 与Semaphore 相比，Semaphore 限制了并发访问的数量而不是使用速率
 * <p>
 * The RateLimiter class is a construct that allows us to regulate the rate at which some processing happens.
 * If we create a RateLimiter with N permits – it means that process can issue at most N permits per second.
 */
public class RateLimiterDemo {


    private static void doSomeLimitedOperation() {
        Log.info("do what that you like ");
    }

    @Test
    public void givenLimitedResource_whenUseRateLimiter_thenShouldLimitPermits() {
        // given
        RateLimiter rateLimiter = RateLimiter.create(100);

        // when
        long startTime = ZonedDateTime.now().getSecond();
        IntStream.range(0, 1000).forEach(i -> {
            rateLimiter.acquire();
            doSomeLimitedOperation();
        });
        long elapsedTimeSeconds = ZonedDateTime.now().getSecond() - startTime;

        // then
        Assert.assertTrue(elapsedTimeSeconds >= 10);
    }


    @Test
    public void lookActionOnLimitPermits() {
        RateLimiter rateLimiter = RateLimiter.create(2);
        IntStream.range(0, 1000).forEach(i -> {
            rateLimiter.acquire();
            doSomeLimitedOperation();
        });
    }
}
