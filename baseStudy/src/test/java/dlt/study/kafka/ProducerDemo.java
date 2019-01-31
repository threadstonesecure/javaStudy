package dlt.study.kafka;

import com.google.common.collect.Lists;
import dlt.study.log4j.Log;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.Future;

public class ProducerDemo {
    private static Properties props = new Properties();

    private static String topic = "denglt";

    static {
        topic = "denglt";
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.134:19092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "1"); //[0,1,ALL]
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");  //[ none, gzip, snappy, or lz4]
        props.put(ProducerConfig.RETRIES_CONFIG, "0");// default 0
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432"); // The total bytes of memory the producer can use to buffer records waiting to be sent to the server.
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384"); // 16k //The producer will attempt to batch records together into fewer requests whenever multiple records are being sent to the same partition
        props.put(ProducerConfig.LINGER_MS_CONFIG, "1"); // 发送延时1ms，default 0
       // props.put(ProducerConfig.);
    }

    @Test
    public void Send() throws Exception {

        Producer<String, String> producer = new KafkaProducer<>(props);
        Future<RecordMetadata> metadataFuture = producer.send(new ProducerRecord<>("denglt", "denglt key", "hello world"), new MyCallback());
        RecordMetadata recordMetadata = metadataFuture.get();
        Log.info("结果：" + recordMetadata);


        Integer partition = 0; // 0;
        Long timestamp = System.currentTimeMillis();
        String key = "denglt key";
        String value = "hello world2";
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.putInt(100).flip();

        for (int i = 0; i < 20; i++) {
            Iterable<Header> headers = Lists.newArrayList(new RecordHeader("name", "邓隆通".getBytes(StandardCharsets.UTF_8))/*,
                    new RecordHeader("age", byteBuffer)*/);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, timestamp, key, value/*, headers*/);
            metadataFuture = producer.send(record, new MyCallback());
            recordMetadata = metadataFuture.get();
            Log.info("结果：" + recordMetadata);
        }

        producer.close();

    }

    /**
     * 交易事务
     * transactional.id参数的目的是为了单个producer的事务可以通过多个session来恢复
     */
    @Test
    public void tran() {
        props.put("transactional.id", "my-transactional-id");
        props.put(ProducerConfig.ACKS_CONFIG, "all");  // 必须为all
        props.put(ProducerConfig.RETRIES_CONFIG, "1"); // 必须 > 0
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");
        Producer<String, String> producer = new KafkaProducer<>(props);
        producer.initTransactions(); // 执行一次
        try {
            producer.beginTransaction();
            for (int i = 0; i < 100; i++) {
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, Integer.toString(i), Integer.toString(i));
                producer.send(record, new MyCallback());
            }
            throw new KafkaException();
            //producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            // We can't recover from these exceptions, so our only option is to close the producer and exit.
            e.printStackTrace();
            producer.close();
        } catch (KafkaException e) {
            // For all other exceptions, just abort the transaction and try again.
            e.printStackTrace();
            producer.abortTransaction();
        }
        producer.close();
    }
}


/**
 * 注意：
 * callback一般在生产者的I/O线程中执行，所以要是相当的快的，否则将延迟其他的线程的消息发送。
 * 如果你需要执行阻塞或计算昂贵（消耗）的回调，建议在callback主体中使用自己的Executor来并行处理。
 */
class MyCallback implements Callback {
    @Override
    public void onCompletion(RecordMetadata metadata, Exception e) {
        if (e != null) {
            Log.error("消息发送失败：", e);
        }
        Log.info("The offset of the record we just sent is: " + metadata.offset() + " on " + metadata.partition());
    }
}
