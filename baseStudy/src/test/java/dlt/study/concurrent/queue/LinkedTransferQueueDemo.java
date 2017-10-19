package dlt.study.concurrent.queue;

import java.util.concurrent.LinkedTransferQueue;

/**
 * Created by denglt on 2016/6/7.
 */
public class LinkedTransferQueueDemo  {
    public static void main(String[] args) throws Exception {
        LinkedTransferQueue<String> queue = new LinkedTransferQueue();
        queue.put("test");
        queue.put("test");

    }
}
