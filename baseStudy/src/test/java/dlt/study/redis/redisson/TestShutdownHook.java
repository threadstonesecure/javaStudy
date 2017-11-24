package dlt.study.redis.redisson;

import java.util.Set;

public class TestShutdownHook {
    public static void main(final String[] args) throws InterruptedException {
        Thread thread = Thread.currentThread();
        System.out.println(thread);
        System.out.println(thread.getClass().getName());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Thread thread = Thread.currentThread();
                System.out.println(thread.getClass().getName());

                System.out.println("Shutdown hook ran!");
            }
        });

        while (true) {
            Thread.sleep(1000);
        }
    }
}