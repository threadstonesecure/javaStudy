package dlt.study.kafka.spring;

import dlt.study.log4j.Log;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerServer implements MessageListener<String, String> {
    @Override
    public void onMessage(ConsumerRecord<String, String> record) {
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

    }
}
