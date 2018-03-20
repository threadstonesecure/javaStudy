package dlt.study.rxjava;

import com.beust.jcommander.internal.Lists;
import dlt.study.log4j.Log;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Observable：发射源，英文释义“可观察的”，在观察者模式中称为“被观察者”或“可观察对象”；
 * <p>
 * Observer：接收源，英文释义“观察者”，没错！就是观察者模式中的“观察者”，可接收Observable、Subject发射的数据；
 * <p>
 * Subject：Subject是一个比较特殊的对象，既可充当发射源，也可充当接收源，
 * <p>
 * Subscriber：“订阅者”，也是接收源，那它跟Observer有什么区别呢？Subscriber实现了Observer接口，比Observer多了一个最重要的方法unsubscribe( )，用来取消订阅，当你不再想接收数据了，可以调用unsubscribe( )方法停止接收，Observer 在 subscribe() 过程中,最终也会被转换成 Subscriber 对象，一般情况下，建议使用Subscriber作为接收源；
 * <p>
 * *Subscription **：Observable调用subscribe( )方法返回的对象，同样有unsubscribe( )方法，可以用来取消订阅事件；
 * <p>
 * Action0：RxJava中的一个接口，它只有一个无参call（）方法，且无返回值，同样还有Action1，Action2...Action9等，Action1封装了含有* 1 个参的call（）方法，即call（T t），Action2封装了含有 2 *个参数的call方法，即call（T1 t1，T2 t2），以此类推；
 * <p>
 * Func0：与Action0非常相似，也有call（）方法，但是它是有返回值的，同样也有Func0、Func1...Func9;
 */
public class RxJavaDemo {

    @Test
    public void hello() throws Exception {
        Integer[] numbers = {1, 2, 3, 4, 5, 6, 7};
        List<Integer> lists = Arrays.asList(numbers);
        Observable<Integer> integerObservable = Observable.from(numbers);//.concatWith(Observable.error());

        //被观察者是惰性的，在没有订阅者监听之前它不会做任何事情。
        integerObservable.subscribe(new MySubscriber(2));
        // Thread.sleep(60000);
        integerObservable.subscribe(new MySubscriber(4));

        integerObservable.filter((t) -> t % 2 == 0).subscribe(new MySubscriber(0));
        Observable<Integer> doOnUnsubscribe = integerObservable.doOnUnsubscribe(() -> System.out.println("doOnUnsubscribe"));

        doOnUnsubscribe.subscribe(new MySubscriber(2));

/*        MySubscriber all = new MySubscriber(2);
        all.add(new MySubscriber(4));
        all.add(new MySubscriber(2));
        integerObservable.subscribe(all);*/
        Thread.currentThread().join();
    }

    @Test
    public void async() throws Exception {
        Integer[] numbers = {1, 2, 3, 4, 5, 6, 7};
        List<Integer> lists = Arrays.asList(numbers);
        Observable<Integer> integerObservable = Observable.from(numbers);
        Observable<Integer> asyncIntegerObservable = integerObservable.observeOn(Schedulers.io()); //integerObservable.subscribeOn(Schedulers.io());
        integerObservable.subscribe(new MySubscriber(2));
        asyncIntegerObservable.subscribe(new MySubscriber(4));
        asyncIntegerObservable.filter((t) -> t % 2 == 0).subscribe(new MySubscriber(0));
        asyncIntegerObservable.subscribe(new MySubscriber(0));
        Thread.currentThread().join();

    }

    @Test
    public void advance() throws Exception {
        Log.info("being...");
        Observable.fromCallable(() -> Lists.newArrayList(1, 2, 3, 4, 5, 6, 7))
                .flatMap((t) -> Observable.from(t)).flatMap((t) -> Observable.just(t))
                .subscribeOn(Schedulers.io())
                .limit(4)  // only first 4 item, same take
                .subscribe(new MySubscriber(0));
        Thread.currentThread().join();

    }


    @Test
    public void createObservable() {
        Observable<String> stringObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("create1"); //发射一个"create1"的String
                subscriber.onNext("create2"); //发射一个"create2"的String
                subscriber.onCompleted();//发射完成,这种方法需要手动调用onCompleted，才会回调Observer的onCompleted方法
            }
        });

        //使用defer( )，有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable：
        Observable.defer(new Func0<Observable<String>>() {
            @Override
            //注意此处的call方法没有Subscriber参数
            public Observable<String> call() {
                return Observable.just("deferObservable");
            }
        });

        //interval( ),创建一个按固定时间间隔发射整数序列的Observable，可用作定时器
        Observable.interval(1, TimeUnit.MINUTES);

        //.使用range( ),创建一个发射特定整数序列的Observable，第一个参数为起始值，第二个为发送的个数，如果为0则不发送，负数则抛异常：
        Observable.range(10, 5);//将发送整数10，11，12，13，14

        //使用timer( ),创建一个Observable，它在一个给定的延迟后发射一个特殊的值，等同于Android中Handler的postDelay( )方法：

        Observable.timer(3, TimeUnit.SECONDS);  //3秒后发射一个值

        // 使用repeat( ),创建一个重复发射特定数据的Observable:
        Observable.just("repeatObservable").repeat(3);//重复发射3次

    }

    @Test
    public void test() throws Exception {

        //Observable.interval(1, TimeUnit.SECONDS).subscribe((t) -> Log.info(t));
        Log.info("begin");
        Observable.timer(3, TimeUnit.SECONDS).subscribe(t -> Log.info(t));

        // Observable.just("repeatObservable").repeat(3).subscribe( (t) -> Log.info(t));
        Thread.currentThread().join();
    }

    @Test
    public void test2() throws Exception {
        Observable<String> stringObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println(subscriber);
                Log.info("发送数据create1");
                subscriber.onNext("create1");
                Log.info("发送数据create2");
                subscriber.onNext("create2");
                Log.info("发送数据Exception");
               // subscriber.onError(new Exception());
                Log.info("发送数据create3");
                subscriber.onNext("create3");
                Log.info("发送数据完成!");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()); // subscribeOn(发射线程),observeOn(接受线程)
        //stringObservable.subscribe((t) -> Log.info(" 收到 ->" + t));
        stringObservable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println(this);
                Log.info("收到2 -> onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.info("收到2 -> " + e);
            }

            @Override
            public void onNext(String s) {
                Log.info("收到2 -> " + s);
            }
        });

        Thread.currentThread().join();
    }

    private class MySubscriber extends Subscriber<Integer> {

        private int stop;

        public MySubscriber(int stop) {
            this.stop = stop;
        }

        @Override
        public void onNext(Integer data) {
            Log.info("Rx onNext:" + data);
            if (data.intValue() == stop) this.unsubscribe();
        }

        @Override
        public void onCompleted() {
            Log.info("Rx Complete!");
        }

        @Override
        public void onError(Throwable e) {
            Log.error(e);
        }
    }
}

