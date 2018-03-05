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
        Observable<String> observable = new CommandHelloWorld("Bob").observe();  // 响应式编程(Reactive Pattern)  RxJava

        Thread.currentThread().join();
    }
}
