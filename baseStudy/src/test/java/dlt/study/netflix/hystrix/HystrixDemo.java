package dlt.study.netflix.hystrix;

import dlt.study.log4j.Log;
import org.junit.Test;
import rx.Observable;
import java.util.concurrent.Future;

public class HystrixDemo {

    @Test
    public void  helloword() throws Exception{
        String s = new CommandHelloWorld("Bob").execute();
        Future<String> future = new CommandHelloWorld("Bob").queue();
        System.out.println(future.get());
        Observable<String> observable = new CommandHelloWorld("Bob").observe();  // 响应式编程(Reactive Pattern)  RxJava
        observable.subscribe( t -> System.out.println(t));
        //observable.subscribe( t -> System.out.println(t));

        Thread.currentThread().join();
    }

    @Test
    public void observe(){
        Observable<String> observable = new CommandHelloWorld("Bob",1000).observe();
        observable.subscribe(t -> System.out.println(t));

    }
}
