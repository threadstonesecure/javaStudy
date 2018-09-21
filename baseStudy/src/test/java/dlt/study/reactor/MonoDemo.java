package dlt.study.reactor;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @Description:
 * @Package: dlt.study.reactor
 * @Author: denglt
 * @Date: 2018/9/12 下午4:17
 * @Copyright: 版权归 HSYUNTAI 所有
 */

/**
 * Reactor有两种类型，Flux<T>和Mono<T>。Flux类似RaxJava的Observable，它可以触发零到多个事件，并根据实际情况结束处理或触发错误。
 * <p>
 * Mono最多只触发一个事件，它跟RxJava的Single和Maybe类似，所以可以把Mono<Void>用于在异步任务完成时发出通知。
 */
public class MonoDemo {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Scheduler scheduler;
    private static Scheduler scheduler2;

    @BeforeClass
    public static void init() {
        scheduler = Schedulers.fromExecutor(Executors.newCachedThreadPool(new DefaultThreadFactory("denglt-1")));
        scheduler2 = Schedulers.fromExecutor(Executors.newCachedThreadPool(new DefaultThreadFactory("denglt-2")));
    }

    @Test
    public void just() {
        Mono<String> just = Mono.just("Hello world!");
        just.subscribe(System.out::println);
    }

    @Test
    public void fromSupplier() throws Exception {
        Mono.fromSupplier(() -> {
            logger.info("run supplier");
            return "hello world";
        }).publishOn(scheduler2).subscribeOn(scheduler)
                .subscribe((t) -> {
                    logger.info("run consumer");
                    logger.info(t);
                });
        Thread.currentThread().join();
    }

    //delay(Duration duration)和 delayMillis(long duration)：创建一个 Mono 序列，在指定的延迟时间之后，产生数字 0 作为唯一值。
    @Test
    public void delay() throws Exception {
        Mono.delay(Duration.ofSeconds(2)).subscribe(System.out::println); // 延时2s执行
        Thread.currentThread().join();
    }

    @Test
    public void create() {
        Mono.create((sink) -> sink.success("create MonoSink")).subscribe(System.out::println);
    }
}


