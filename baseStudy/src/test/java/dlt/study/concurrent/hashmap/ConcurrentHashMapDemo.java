package dlt.study.concurrent.hashmap;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by denglt on 16/9/1.
 */
public class ConcurrentHashMapDemo {
    private static final ConcurrentMap<Integer, String> map = new ConcurrentSkipListMap<>(); // new ConcurrentHashMap<Integer, String>();

    public static void main(String[] args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    map.put(i++, "test");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        Thread.sleep(1000);

        final Set<Map.Entry<Integer, String>> entries = map.entrySet();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("set纪录数:" + entries.size());
                    entries.clear();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("ConcurrentHashMap纪录数:" + map.size());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
