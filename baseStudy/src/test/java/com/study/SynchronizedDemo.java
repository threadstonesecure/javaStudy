package com.study;

import dlt.study.log4j.Log;
import org.junit.Test;
import org.redisson.remote.ResponseEntry;

import java.util.concurrent.*;

public class SynchronizedDemo {



    protected static final ConcurrentMap<String, String> responses;

    static {
        responses =  new ConcurrentHashMap<>();
    }

    @Test
    public void synchronizedTest() throws Exception {
        Log.info("开始");
        ExecutorService tpe = Executors.newFixedThreadPool(300);
        for (int i = 0; i < 300; i++) {
            tpe.execute(new SyncRun());
        }

        Thread.currentThread().join();
    }
}

class SyncRun implements Runnable {

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();
        long endTime;
        synchronized (SynchronizedDemo.responses) {
            endTime = System.currentTimeMillis();
            String entry = SynchronizedDemo.responses.get("name");
            if (entry == null) {
                entry = "denglt";
                String oldEntry = SynchronizedDemo.responses.putIfAbsent("name", entry);
                if (oldEntry != null) {
                    entry = oldEntry;
                }
            }

        }
        Log.info("Duration -> " + (endTime - startTime));

    }
}