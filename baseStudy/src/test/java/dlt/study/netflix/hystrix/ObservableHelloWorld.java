package dlt.study.netflix.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import dlt.study.log4j.Log;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ObservableHelloWorld extends HystrixObservableCommand<String> {
    private final String name;

    public ObservableHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected Observable<String> construct() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        // a real example would do work like a network call here
                        Log.info("observer begin");
                        observer.onNext("Hello");
                        observer.onNext(name + "!");
                        observer.onNext(name + "!");
                      //  observer.onError(new Exception());
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }


    @Override
    protected Observable<String> resumeWithFallback() {
        return Observable.just("fail!"); // Observable.empty();
    }

    public static void main(String[] args) throws Exception {
  /*      ObservableHelloWorld helloWorld = new ObservableHelloWorld("denglt");
        Observable<String> observe = helloWorld.observe();// 开始执行
        observe.subscribe(s -> Log.info(s));
        //System.out.println(observe.toBlocking().toFuture().get());
        Log.info(observe.reduce((t1, t2) -> t1 + " " + t2).toBlocking().toFuture()*//*等候完成*//*.get());
        //Thread.currentThread().join();*/
        ObservableHelloWorld helloWorld = new ObservableHelloWorld("denglt");
        Observable<String> observe = helloWorld.toObservable();
        observe.toBlocking().toFuture();
        Thread.currentThread().join();
    }
}
