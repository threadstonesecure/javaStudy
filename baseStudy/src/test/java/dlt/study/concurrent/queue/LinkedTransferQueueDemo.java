package dlt.study.concurrent.queue;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import dlt.study.guava.concurrency.MoreExecutorsDemo;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

/**
 * Created by denglt on 2016/6/7.
 */
public class LinkedTransferQueueDemo  {

    @Test
    public void  transfer() throws Exception {
        LinkedTransferQueue<String> queue = new LinkedTransferQueue();


        ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));
        executor.execute(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("task->" + queue.take());
                System.out.println("task->" + queue.take());
                //System.out.println("task->" + queue.take());
                System.out.println("poll->"+queue.poll());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        queue.put("test");
        queue.put("test");
       // queue.tryTransfer()
       // queue.hasWaitingConsumer()
       // queue.getWaitingConsumerCount()
        queue.transfer("test"); // blocked，until take or poll it;

        System.out.println("test is taked!");
    }

    public static void main(String[] args) throws Exception {
        LinkedTransferQueue<String> queue = new LinkedTransferQueue();
        queue.put("test");
        queue.put("test");

        System.out.println(queue.take());
        System.out.println(queue.take());


    }
}
