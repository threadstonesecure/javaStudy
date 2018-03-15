package dlt.study.rxjava;

import dlt.study.log4j.Log;
import org.junit.Test;
import rx.Observer;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class SubjectDemo {

    /**
     * Observer会接收AsyncSubject的```onComplete()``之前的最后一个数据
     * (AsyncSubject仅发送Observable 发送的最后一个数据，并且仅在Observable完成之后。
     * 然而如果当Observable因为异常而终止，AsyncSubject将不会发送任何数据，但是会向Observer传递一个异常通知。
     * )
     */
    @Test
    public void asyncSubject() throws Exception {
        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext("asyncSubject1");
        asyncSubject.onNext("asyncSubject2");
        asyncSubject.onNext("asyncSubject3");
        asyncSubject.onCompleted();
        asyncSubject.observeOn(Schedulers.io()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.info("asyncSubject onCompleted");  //输出 asyncSubject onCompleted
            }

            @Override
            public void onError(Throwable e) {
                Log.info("asyncSubject onError");  //不输出（异常才会输出）
            }

            @Override
            public void onNext(String s) {
                Log.info("asyncSubject:" + s);  //输出asyncSubject:asyncSubject3
            }
        });
        asyncSubject.onNext("asyncSubject4");
        //asyncSubject.onCompleted();
        asyncSubject.subscribeOn(Schedulers.io()).subscribe(s -> Log.info(s));

        Thread.currentThread().join();

    }

    /**
     * Observer接收的是BehaviorSubject被订阅前发送的最后一个数据，且之后还会继续接收数据
     */
    @Test
    public void behaviorSubject() throws Exception {
        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create("default");
        behaviorSubject.onNext("behaviorSubject1");
        behaviorSubject.onNext("behaviorSubject2");
        behaviorSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.info("behaviorSubject:complete");
            }

            @Override
            public void onError(Throwable e) {
                Log.info("behaviorSubject:error");
            }

            @Override
            public void onNext(String s) {
                Log.info("behaviorSubject:" + s);
            }
        });
        //behaviorSubject.subscribeOn() 有问题,无法获取到数据
        behaviorSubject.observeOn(Schedulers.io()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.info("behaviorSubject:complete2");
            }

            @Override
            public void onError(Throwable e) {
                Log.info("behaviorSubject2:error");
            }

            @Override
            public void onNext(String s) {
                Log.info("behaviorSubject2:" + s);
            }
        });
        behaviorSubject.onNext("behaviorSubject3");
        behaviorSubject.onNext("behaviorSubject4");

        Thread.currentThread().join();
    }

    /**
     * Observer只会接收到PublishSubject被订阅之后发送的数据
     */
    @Test
    public void publishSubject() throws Exception {

        PublishSubject<String> publishSubject = PublishSubject.create();
        publishSubject.onNext("publishSubject1");
        publishSubject.onNext("publishSubject2");
        publishSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
                Log.info("publishSubject observer1:" + s);
            }
        });
        // publishSubject.subscribeOn(Schedulers.io()) 没有生效
        publishSubject.observeOn(Schedulers.io()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
                Log.info("publishSubject observer2:" + s);
            }
        });
        publishSubject.onNext("publishSubject3");
        publishSubject.onNext("publishSubject4");
        Thread.currentThread().join();
    }

    /**
     * ReplaySubject会发射所有数据给观察者，无论它们是何时订阅的
     */
    @Test
    public void replaySubject() {

    }

}
