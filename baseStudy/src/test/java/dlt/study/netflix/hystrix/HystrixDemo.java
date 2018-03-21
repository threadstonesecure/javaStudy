package dlt.study.netflix.hystrix;

import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HystrixDemo {


    @BeforeClass
    public static void init() {
        HystrixRequestContext.initializeContext();
    }

    @Test
    public void helloword() throws Exception {
        String s = new CommandHelloWorld("Bob").execute();
        Future<String> future = new CommandHelloWorld("Bob1").queue();
        System.out.println(future.get());
        Observable<String> observable = new CommandHelloWorld("Bob2").observe();  // 响应式编程(Reactive Pattern)  RxJava
        observable.subscribe(System.out::println);
        //observable.subscribe( t -> System.out.println(t));

        Thread.currentThread().join();
    }

    /**
     * observe() — returns a “hot” Observable that executes the command immediately,
     * though because the Observable is filtered through a ReplaySubject you are not in danger of losing any items that it emits before you have a chance to subscribe
     * toObservable() — returns a “cold” Observable that won’t execute the command
     * and begin emitting its results until you subscribe to the Observable
     */
    @Test
    public void observe() throws Exception {
        Observable<String> observable = new CommandHelloWorld("Bob", 500).observe();
        observable.subscribe(System.out::println);
        observable.subscribe(System.out::println);
        System.out.println(observable.toBlocking().toFuture().get());

/*      Observable<String> observable = new CommandHelloWorld("Bob", 500).toObservable(); // 不会执行
        observable.subscribe(System.out::println);
        // observable.subscribe(System.out::println); // CommandHelloWorld command executed multiple times - this is not permitted.
        */
        Thread.currentThread().join();
    }

    @Test
    public void fallback() {
        String s = new CommandHelloWorld("Bob", 3000).execute();
        System.out.println("结果：" + s);  // Hello Failure Bob!

        System.out.println("finish!");
    }


    @Test
    public void cache() {
        try (HystrixRequestContext context = HystrixRequestContext.initializeContext()) {
            System.out.println(HystrixRequestContext.getContextForCurrentThread());
            new CommandHelloWorld("cache").execute();
            new CommandHelloWorld("cache").execute();
            new CommandHelloWorld("cache2").execute();
            new CommandHelloWorld("cache2").execute();
            // clear cache
            CommandHelloWorld commandHelloWorld = new CommandHelloWorld("sss");
            HystrixRequestCache.getInstance(commandHelloWorld.getCommandKey(), HystrixConcurrencyStrategyDefault.getInstance()).clear("cache");
            new CommandHelloWorld("cache").execute();   // run,not cache
            new CommandHelloWorld("cache2").execute(); // not run, get value from cache
        }
        System.out.println(HystrixRequestContext.getContextForCurrentThread());
    }

    /**
     * isolation.strategy : Semaphore
     *
     * @throws Exception
     */
    @Test
    public void maxSemaphore() throws Exception {
        try (HystrixRequestContext context = HystrixRequestContext.initializeContext()) {
            System.out.println(HystrixRequestContext.getContextForCurrentThread());

            ExecutorService executorService = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 8; i++) {
                String cmd = "cmd" + i;
                executorService.execute(() -> new CommandHelloWorld(cmd, 100, 3).queue());
            }
            Thread.sleep(2000);
            for (int i = 0; i < 5; i++) {
                String cmd = "cmd" + i;
                executorService.execute(() -> new CommandHelloWorld(cmd, 100, 3).queue());
            }

/*            new CommandHelloWorld("cmd1", 3000, 3).queue();
            new CommandHelloWorld("cmd2", 3000, 3).queue();
            new CommandHelloWorld("cmd3", 3000, 3).queue();
            new CommandHelloWorld("cmd4", 3000, 3).queue();*/

        }
        Thread.currentThread().join();
    }

    /**
     * isolation.strategy :Thread
     *
     * @throws Exception
     */

    @Test
    public void thread() throws Exception {
        try (HystrixRequestContext context = HystrixRequestContext.initializeContext()) {
            System.out.println(HystrixRequestContext.getContextForCurrentThread());
            Future<String> cmd1 = new CommandHelloWorld("cmd1", 1000, 3, 10).queue();
            Future<String> cmd2 = new CommandHelloWorld("cmd2", 1000, 3, 10).queue();
            Future<String> cmd3 = new CommandHelloWorld("cmd3", 1000, 3, 10).queue();
            Future<String> cmd4 = new CommandHelloWorld("cmd4", 1000, 3, 10).queue();

            System.out.println(cmd1.get());
            System.out.println(cmd2.get());
            System.out.println(cmd3.get()); // timeout
            System.out.println(cmd4.get()); // timeout
        }
    }

    @Test
    public void circuitBreaker() throws Exception{
        new CommandHelloWorld("hello world!").execute();
    }

    @Test
    public void plugin() throws Exception{
        HystrixPlugins.getInstance().registerCommandExecutionHook(LogCommandExecutionHook.get());
        new CommandHelloWorld("hello world!").execute();
        new CommandHelloWorld("hello world2!").execute();
    }

}
