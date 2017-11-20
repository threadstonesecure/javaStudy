package dlt.study.guava.cache;

import com.google.common.base.Ticker;
import com.google.common.cache.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import dlt.study.guava.graph.City;
import dlt.study.log4j.Log;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;


public class CacheDemo {


    static LoadingCache<String, City> cityCache;

    static ConcurrentMap<String, City> cityConcurrentHashMap;

    private static City newCity(String key) {
        Log.info(String.format("new city by key[%s]", key));
        return new City(key, "未知");
    }

    @BeforeClass
    public static void init() {
        cityCache = CacheBuilder.newBuilder()
/*                .maximumWeight()
                .weigher( new Weigher<String,City>(){
                    @Override
                    public int weigh(String key, City value) {
                        return 0;
                    }
                })*/
                .maximumSize(1000) // cache will try to evict entries that haven't been used recently or very often.
                .expireAfterWrite(10, TimeUnit.SECONDS)// Expire entries after the specified duration has passed since the entry was created, or the most recent replacement of the value
                .expireAfterAccess(10, TimeUnit.SECONDS)// Only expire entries after the specified duration has passed since the entry was last accessed by a read or a write
                .ticker(Ticker.systemTicker())
                .removalListener(new RemovalListener<String, City>() { // 如果onRemoval动作比较耗时，可以使用RemovalListeners.asynchronous包装为异步
                    @Override
                    public void onRemoval(RemovalNotification<String, City> notification) {
                        System.out.println(String.format("remove key[%s] value[%s],cause is [%s]",
                                notification.getKey(), notification.getValue(), notification.getCause()));

                    }
                })
                // .refreshAfterWrite()
                .build(
                        new CacheLoader<String, City>() {

                            private Map<String, City> existCity = ImmutableMap.of(
                                    City.BEIJIN.getCode(), City.BEIJIN,
                                    City.GUANGZHOU.getCode(), City.GUANGZHOU,
                                    City.SHANGHAI.getCode(), City.SHANGHAI,
                                    City.SHENZHEN.getCode(), City.SHENZHEN);

                            public City load(String key) {
                                System.out.println("loading " + key);
                                City city = existCity.get(key);
                                return city != null ? city : newCity(key);
                            }

                            @Override
                            public ListenableFuture<City> reload(String key, City oldValue) throws Exception {
                                System.out.println(String.format("begin reloading key[%s] oldValue[%s]", key, oldValue));
                                if (City.FIXED_CITY.get(key) != null)
                                    return Futures.immediateFuture(oldValue);
                                else { // 这儿进行异步 reload
                                    // asynchronous!
                                    ListenableFutureTask<City> task = ListenableFutureTask.create(new Callable<City>() {
                                        @Override
                                        public City call() throws Exception {
                                            Log.info("asynchronous load!");
                                            Thread.sleep(1000);
                                            return newCity(key);
                                        }
                                    });
                                    // task.addListener(); 可以增加一个完成监听
                                    new Thread(task).start();
                                    return task;
                                }
                            }
                        });

        System.out.println(cityCache.getClass()); // com.google.common.cache.LocalCache$LocalLoadingCache
        cityConcurrentHashMap = cityCache.asMap();
    }

    @Test
    public void get() throws Exception {
        cityCache.get(City.BEIJIN.getCode());
        cityCache.get(City.BEIJIN.getCode());
        cityCache.get("hangzhou");
        cityCache.get("hangzhou");
        Thread.sleep(5 * 1000);
        cityCache.get(City.BEIJIN.getCode());
        cityCache.get("hangzhou");
        Thread.sleep(5 * 1000);
        cityCache.get(City.BEIJIN.getCode());
        cityCache.get("hangzhou");

        // cityCache.getUnchecked();

    }

    @Test
    public void refresh() throws Exception {
        cityCache.get(City.BEIJIN.getCode());
        System.out.println("========refresh=========");
        cityCache.refresh(City.BEIJIN.getCode());//If an exception is thrown while refreshing, the old value is kept, and the exception is logged and swallowed.
        cityCache.get(City.BEIJIN.getCode());
        City oldCity = cityCache.get("ddddd");
        cityCache.refresh("ddddd");
        City newCity = cityCache.get("ddddd");
        System.out.println(oldCity == newCity);
        Thread.sleep(1000);
        newCity = cityCache.get("ddddd");
        System.out.println(oldCity == newCity);
        cityCache.stats();

    }

    @Test
    public void autoRefresh() throws Exception {
        cityCache = CacheBuilder.newBuilder()
                .removalListener(new RemovalListener<String, City>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, City> notification) {
                        Log.info(String.format("remove key[%s] value[%s],cause is [%s]",
                                notification.getKey(), notification.getValue(), notification.getCause()));
                    }
                })
                .refreshAfterWrite(10, TimeUnit.SECONDS)
                .build(
                        new CacheLoader<String, City>() {

                            private Map<String, City> existCity = ImmutableMap.of(
                                    City.BEIJIN.getCode(), City.BEIJIN,
                                    City.GUANGZHOU.getCode(), City.GUANGZHOU,
                                    City.SHANGHAI.getCode(), City.SHANGHAI,
                                    City.SHENZHEN.getCode(), City.SHENZHEN);

                            public City load(String key) {
                                Log.info("loading " + key);
                                City city = existCity.get(key);
                                return city != null ? city : newCity(key);
                            }

                            @Override
                            public ListenableFuture<City> reload(String key, City oldValue) throws Exception {
                                Log.info(String.format("begin reloading key[%s] oldValue[%s]", key, oldValue));
                                throw new RuntimeException("无法刷新");
/*                                ListenableFutureTask<City> task = ListenableFutureTask.create(new Callable<City>() {
                                    @Override
                                    public City call() throws Exception {
                                        Log.info("asynchronous load!");
                                        Thread.sleep(1000);
                                        throw new RuntimeException("无法刷新");
                                       // return newCity(key);
                                    }
                                });
                                // task.addListener(); 可以增加一个完成监听
                                new Thread(task).start();
                                return task;*/
                            }
                        });

        City temp = cityCache.get("temp");
        Thread.sleep(11 * 1000);
        City temp2 = cityCache.get("temp");
        System.out.println(temp == temp2); // true
        Thread.sleep(2000);
        temp2 = cityCache.get("temp");  // reload 错误不会影响get失败，get可以取回旧值
        System.out.println(temp == temp2); // false  refresh 完成
        cityCache.get("temp");
        System.in.read();
    }

    @Test
    public void getCallable() throws Exception {
        City city = cityCache.get("hangzhou2", () -> new City("hangzhou2", "hangzhou2"));
        System.out.println(city == cityCache.get("hangzhou2"));
        Thread.sleep(10 * 1000);
        cityCache.get("hangzhou2");
    }


    /**
     * 因为这个例子中key为String,故GC没有把cache清空
     *
     * @throws Exception
     */
    @Test
    public void weakKeys() throws Exception {
        cityCache = CacheBuilder.newBuilder()
                .weakKeys()
                .build(
                        new CacheLoader<String, City>() {

                            private Map<String, City> existCity = ImmutableMap.of(
                                    City.BEIJIN.getCode(), City.BEIJIN,
                                    City.GUANGZHOU.getCode(), City.GUANGZHOU,
                                    City.SHANGHAI.getCode(), City.SHANGHAI,
                                    City.SHENZHEN.getCode(), City.SHENZHEN);

                            public City load(String key) {
                                System.out.println("loading " + key);
                                City city = existCity.get(key);
                                return city != null ? city : new City(key, "未知");
                            }
                        });
        cityCache.get("beijin2");
        cityCache.get("beijin2");
        System.gc();
        cityCache.get("beijin2");  // 这儿不会有 loading beijin2

    }


    @Test
    public void weakValues() throws Exception {
        cityCache = CacheBuilder.newBuilder()
                .weakValues()  // .softValues()
                .build(
                        new CacheLoader<String, City>() {

                            private Map<String, City> existCity = ImmutableMap.of(
                                    City.BEIJIN.getCode(), City.BEIJIN,
                                    City.GUANGZHOU.getCode(), City.GUANGZHOU,
                                    City.SHANGHAI.getCode(), City.SHANGHAI,
                                    City.SHENZHEN.getCode(), City.SHENZHEN);

                            public City load(String key) {
                                System.out.println("loading " + key);
                                City city = existCity.get(key);
                                return city != null ? city : new City(key, "未知");
                            }
                        });
        cityCache.get("beijin2");
        cityCache.get("beijin2");  //// 这儿没有 loading beijin2
        System.gc();
        cityCache.get("beijin2"); // 这儿会有 loading beijin2
    }


    /**
     * 验证：
     * This (weakKeys()) causes the whole cache to use identity (==) equality to compare keys, instead of equals().
     */
    @Test
    public void identityKeyInWeakKeys() throws Exception {
        LoadingCache<Integer, City> cityCache = CacheBuilder.newBuilder()
                .weakKeys()
                .build(
                        new CacheLoader<Integer, City>() {

                            private Map<Integer, City> existCity = ImmutableMap.of(
                                    new Integer(1), City.BEIJIN,
                                    new Integer(2), City.GUANGZHOU,
                                    new Integer(3), City.SHANGHAI,
                                    new Integer(4), City.SHENZHEN);

                            public City load(Integer key) {
                                System.out.println("loading " + key);
                                City city = existCity.get(key);
                                return city != null ? city : new City("" + key, "未知" + key);
                            }
                        });

        cityCache.get(new Integer(1));
        cityCache.get(new Integer(1));
    }

    /**
     * 验证：
     * This(weakValues()、softValues()) causes the whole cache to use identity (==) equality to compare keys, instead of equals()
     */
    @Test
    public void identityValue() throws Exception {
        LoadingCache<Integer, City> cityCache = CacheBuilder.newBuilder()
                .weakValues()
                .build(
                        new CacheLoader<Integer, City>() {

                            private Map<Integer, City> existCity = ImmutableMap.of(
                                    new Integer(1), City.BEIJIN,
                                    new Integer(2), City.GUANGZHOU,
                                    new Integer(3), City.SHANGHAI,
                                    new Integer(4), City.SHENZHEN);

                            public City load(Integer key) {
                                System.out.println("loading " + key);
                                City city = existCity.get(key);
                                return city != null ? city : new City("" + key, "未知" + key);
                            }
                        });
        // 目前没有找到测试方法
        System.out.println(cityCache.get(new Integer(1)));
        System.out.println(cityCache.get(new Integer(1)));

        // cityCache.invalidate*();
    }


    @Test
    public void weakRef() {
        City city = new City("temp", "temp");
        WeakReference<City> weakReference = new WeakReference<>(city);
        city = null; // 这个必须
        System.gc();
        System.out.println(weakReference.get());
    }

    @Test
    public void weakRefWithStr() {
        String temp = "denglt";
        WeakReference<String> weakReference = new WeakReference<>(temp); // WeakReference 对 String 不起作用
        temp = null; // 这个必须
        System.gc();
        System.out.println(weakReference.get());
    }

    @Test
    public void weakRefWithInteger() {
        // int i =1000000;
        Integer i = 127; // new Integer(100);
        WeakReference<Integer> weakReference = new WeakReference<>(i);
        i = null;
        System.gc();
        System.out.println(weakReference.get());
    }

    @Test
    public void weakRefWithInt() {
        WeakReference<City> weakReference = new WeakReference<>(new City("asdfsd", "afdsf"));
        System.gc();
        System.out.println(weakReference.get());
    }


    @Test
    public void testString() {
        String a = new String("temp");
        String b = new String("temp");
        System.out.println(a == b);

        Integer i = new Integer(1);
        Integer i2 = new Integer(1);
        System.out.println(i == i2);
    }
}
