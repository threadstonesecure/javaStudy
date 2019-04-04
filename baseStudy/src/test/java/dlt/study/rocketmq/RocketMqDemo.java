package dlt.study.rocketmq;

import dlt.study.log4j.Log;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Test;


/**
 * @Description:
 * @Package: dlt.study.rocketmq
 * @Author: denglt
 * @Date: 2019/3/14 4:22 PM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class RocketMqDemo {

    private String topic = "mytopic";
    private String namesrv = "127.0.0.1:9876";

    @Test
    public void producer() throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer("denglt");
        // Specify name server addresses.
        producer.setNamesrvAddr(namesrv);
        // producer.setInstanceName();
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 100; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(topic, "TagA" /* Tag */,
                    ("Hello RocketMQ " +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            // This message will be delivered to consumer 10 seconds later.
            //msg.setDelayTimeLevel(3);
            //Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg, (mqs, msg1, arg) -> {
                return mqs.get(0); // 全放在第一个queue
            }, i);
            System.out.printf("%s%n", sendResult);
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
    /*
    denglt:apache-rocketmq denglt$ bin/mqadmin topicStatus -n localhost:9876 -t mytopic
    Java HotSpot(TM) 64-Bit Server VM warning: ignoring option PermSize=128m; support was removed in 8.0
    Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=128m; support was removed in 8.0
    #Broker Name                      #QID  #Min Offset           #Max Offset             #Last Updated
    broker-a                          0     0                     100                     2019-03-14 17:21:32,106
    broker-a                          1     0                     0
    broker-a                          2     0                     0
    broker-a                          3     0                     0
    broker-a                          4     0                     0
    broker-a                          5     0                     0
    broker-a                          6     0                     0
    broker-a                          7     0                     0
     */

    @Test
    public void consumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("c_denglt_16");
        consumer.setInstanceName("denglt");
        consumer.setNamesrvAddr(namesrv);
        consumer.subscribe(topic, "*");
        //consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeThreadMin(6);
        consumer.setConsumeThreadMax(6);
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.setConsumeMessageBatchMaxSize(4);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> { //该方法一定不能发生错误，否则consumer会重新收到消息
                                                                                            //因为没有确认的消息将会进入到重试topic中(eg:%RETRY%c_denglt_11）
            Log.info("count -> " + msgs.size());
            msgs.forEach(msg -> {
                String body = new String(msg.getBody());
                if (body.equals("Hello RocketMQ 33")) {
                    throw new RuntimeException("我就是要报错！"); // 这个错误会造成ConsumeMessageBatchMaxSize个数的Message进入retry topic.
                }
                Log.info(String.format("Receive New Messages: %s %s %n", body, msg));
            });
            String body = new String(msgs.get(0).getBody());
            if (body.equals("Hello RocketMQ 0")){
                // 就是要 later try to consume
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        //Launch the consumer instance.
        consumer.start();

        System.out.printf("Consumer Started.%n");
        Thread.currentThread().join();
    }

    /**
     * 手动启动两个
     * @throws Exception
     */
    @Test
    public void consumer2() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("c_denglt_m");
       // consumer.setMessageModel(MessageModel.BROADCASTING); //  MessageModel.CLUSTERING
       // consumer.setInstanceName("denglt"); // MessageModel.CLUSTERING模式下如果instanceName、consumerGroup都相同的Consumer会同时收到Message，有点like BROADCASTING模式，但instanceName=DEFAULT例外
        consumer.setNamesrvAddr(namesrv);
        consumer.subscribe(topic, "*");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeThreadMin(2);
        consumer.setConsumeThreadMax(2);
        consumer.setConsumeMessageBatchMaxSize(1);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            msgs.forEach(msg -> {
                String body = new String(msg.getBody());
                Log.info(String.format("Receive New Messages: %s %s %n", body, msg));
            });
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.printf("Consumer Started.%n");
        Thread.currentThread().join();
    }

    @Test
    public void consumerOrder() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("c_denglt_order");
        consumer.setNamesrvAddr(namesrv);
        consumer.subscribe(topic, "*");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeThreadMin(2);
        consumer.setConsumeThreadMax(10);
        consumer.setConsumeMessageBatchMaxSize(3);

        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            //设置自动提交,如果不设置自动提交就算返回SUCCESS,消费者关闭重启 还是会重复消费的
            context.setAutoCommit(true);
            try {
                for (MessageExt msg : msgs) {
                    System.out.println(" 消费者1 ==> 当前线程:" + Thread.currentThread().getName() + " ,quenuID: " + msg.getQueueId() + " ,content: " + new String(msg.getBody()));
                    System.out.println(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //如果出现异常,消费失败，挂起消费队列一会会，稍后继续消费
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
            //消费成功
            return ConsumeOrderlyStatus.SUCCESS;
        });
        consumer.start();
        System.out.printf("Consumer Started.%n");
        Thread.currentThread().join();
    }
}
