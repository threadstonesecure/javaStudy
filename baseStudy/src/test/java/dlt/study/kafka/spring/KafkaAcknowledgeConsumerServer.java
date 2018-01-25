package dlt.study.kafka.spring;

import dlt.study.log4j.Log;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


/**
 * 手工确认
 */
@Component
public class KafkaAcknowledgeConsumerServer implements AcknowledgingMessageListener<String, String> {
    @Override
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        Log.info("=============kafkaConsumer开始消费=============");
        String topic = record.topic();
        String key = record.key();
        String value = record.value();
        long offset = record.offset();
        int partition = record.partition();
        Log.info("-------------topic:" + topic);
        Log.info("-------------value:" + value);
        Log.info("-------------key:" + key);
        Log.info("-------------offset:" + offset);
        Log.info("-------------partition:" + partition);
        Log.info("~~~~~~~~~~~~~kafkaConsumer消费结束~~~~~~~~~~~~~");
        acknowledgment.acknowledge();// 不能自己实现，目前实现 ConsumerAcknowledgment 和 ConsumerBatchAcknowledgment
    }
}
