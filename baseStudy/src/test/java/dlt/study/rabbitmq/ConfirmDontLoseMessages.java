package dlt.study.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

import dlt.study.rabbitmq.hello.QueueInfo;

public class ConfirmDontLoseMessages {
    static int msgCount = 10000;
    final static String QUEUE_NAME = "confirm-test";
    static ConnectionFactory connectionFactory;

    public static void main(String[] args)
        throws IOException, InterruptedException
    {
        if (args.length > 0) {
                msgCount = Integer.parseInt(args[0]);
        }

        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(QueueInfo.HOST);
        connectionFactory.setUsername(QueueInfo.USERNAME);
        connectionFactory.setPassword(QueueInfo.PASSWORD);

        // Publish msgCount messages and wait for confirms.
        (new Thread(new Publisher())).start();
         
        //Thread.sleep(5000);
        // Consume msgCount messages.
        (new Thread(new Consumer())).start();
    }

    static class Publisher implements Runnable {
        public void run() {
            try {
                long startTime = System.currentTimeMillis();

                // Setup
                Connection conn = connectionFactory.newConnection();
                Channel ch = conn.createChannel();
                ch.queueDeclare(QUEUE_NAME, true, false, false, null);
                ch.confirmSelect();

                // Publish
                for (long i = 0; i < msgCount; ++i) {
                    ch.basicPublish("", QUEUE_NAME,
                                    MessageProperties.PERSISTENT_BASIC,
                                    "nop".getBytes());
                }

                ch.waitForConfirmsOrDie();

                // Cleanup
                ch.queueDelete(QUEUE_NAME);
                ch.close();
                conn.close();

                long endTime = System.currentTimeMillis();
                System.out.printf("StringIntern took %.3fs\n",
                                  (float)(endTime - startTime)/1000);
            } catch (Throwable e) {
                System.out.println("foobar :(");
                System.out.print(e);
            }
        }
    }

    static class Consumer implements Runnable {
        public void run() {
            try {
                // Setup
                Connection conn = connectionFactory.newConnection();
                Channel ch = conn.createChannel();
                ch.queueDeclare(QUEUE_NAME, true, false, false, null);

                // Consume
                QueueingConsumer qc = new QueueingConsumer(ch);
                ch.basicConsume(QUEUE_NAME, true, qc);
                for (int i = 0; i < msgCount; ++i) {
                    QueueingConsumer.Delivery delivery = qc.nextDelivery();
                    delivery.getBody();
                }

                // Cleanup
                ch.close();
                conn.close();
            } catch (Throwable e) {
                System.out.println("Whoosh!");
                System.out.print(e);
            }
        }
    }
}
