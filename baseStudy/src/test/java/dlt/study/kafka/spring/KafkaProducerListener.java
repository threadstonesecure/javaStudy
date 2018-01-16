package dlt.study.kafka.spring;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static dlt.study.log4j.Log.*;

@Component
public class KafkaProducerListener implements ProducerListener {
    /**
     * 发送消息成功后调用
     */
    @Override
    public void onSuccess(String topic, Integer partition, Object key,
                          Object value, RecordMetadata recordMetadata) {
        info("==========kafka发送数据成功（日志开始）==========");
        info("----------topic:"+topic);
        info("----------partition:"+partition);
        info("----------key:"+key);
        info("----------value:"+value);
        info("----------RecordMetadata:"+recordMetadata);
        info("~~~~~~~~~~kafka发送数据成功（日志结束）~~~~~~~~~~");
    }

    /**
     * 发送消息错误后调用
     */
    @Override
    public void onError(String topic, Integer partition, Object key,
                        Object value, Exception exception) {
        info("==========kafka发送数据错误（日志开始）==========");
        info("----------topic:"+topic);
        info("----------partition:"+partition);
        info("----------key:"+key);
        info("----------value:"+value);
        info("----------Exception:"+exception);
        info("~~~~~~~~~~kafka发送数据错误（日志结束）~~~~~~~~~~");
        exception.printStackTrace();
    }

    /**
     * 方法返回值代表是否启动kafkaProducer监听器
     */
    @Override
    public boolean isInterestedInSuccess() {
        info("///kafkaProducer监听器启动///");
        return true;
    }

}
