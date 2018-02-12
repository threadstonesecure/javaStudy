package dlt.study.kafka.streams;

import dlt.study.log4j.Log;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 KTable和KStream是Kafka Stream中非常重要的两个概念，它们是Kafka实现各种语义的基础。
 KStream是一个数据流，可以认为所有记录都通过Insert only的方式插入进这个数据流里。
 而KTable代表一个完整的数据集，可以理解为数据库中的表。
 由于每条记录都是Key-Value对，这里可以将Key理解为数据库中的Primary Key，而Value可以理解为一行记录。
 可以认为KTable中的数据都是通过Update only的方式进入的。
 也就意味着，如果KTable对应的Topic中新进入的数据的Key已经存在，那么从KTable只会取出同一Key对应的最后一条数据，相当于新的数据更新了旧的数据。
 */
public class KTableDemo {
    private static Map<String, Object> props = new HashMap<>();

    static {
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-study-tables");//流处理应用的标识，对同一个应用需要一致，因为它是作为消费的group_id的
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.134:19092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    /**
     * 观察 KTable 和 KStream的 区别
     * KStream ：数据的记录始终被解释为Insert，只有追加，因为没有办法替换当前已经存在的相同key的行数据
     * KTable: 一个changlog更新日志流,一个数据记录的值被认为是相同Key的最后一个值的更新结果
     *
     * @throws Exception
     */
    @Test
    public void table() throws Exception {
        //produce();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-study-tables_2");
        String topic = "study";
        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        builder.table(topic).toStream().print(); //.foreach((k, v) -> Log.info(k + "->" + v)); // 只有denglt->3 一条记录
        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.setStateListener(((newState, oldState) -> Log.info("状态改变：" + oldState + "->" + newState)));
        streams.start();

        System.in.read();
        //Thread.sleep(60000);
        streams.close();
        streams.cleanUp();
    }

    @Test
    public void produce() throws Exception {
        String topic = "study";
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        props.put(ProducerConfig.RETRIES_CONFIG, "0");
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        props.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        Producer<String, String> producer = new KafkaProducer<>(props);

        String key = "denglt";  // key 不能为null
        String value = "1";
        producer.send(new ProducerRecord<>(topic, key, value));

        key = "denglt";
        value = "3";
        producer.send(new ProducerRecord<>(topic, key, value));
        Thread.sleep(10000);

    }

    /**
     * Join records of this KTable with another KTable's records using non-windowed inner equi join
     * 无窗口，每次使用最新的数据
     * 任意一边有更新，结果KTable都会更新
     * @throws Exception
     */
    @Test
    public void join() throws Exception {
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-study-tables");
        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        KTable<String, String> left = builder.table("intpu-left");
        KTable<String, String> right = builder.table("intpu-right");
        KTable<String, String> all = left.join(right, new ValueJoiner<String, String, String>() {
            @Override
            public String apply(String value1, String value2) {
                return value1 + "--" + value2;
            }
        });
        all.print();
        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();
        System.in.read();
        streams.close();
        streams.cleanUp();
    }

    @Test
    public void produceJoinData() throws Exception{
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        props.put(ProducerConfig.RETRIES_CONFIG, "0");
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        props.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        Producer<String, String> producer = new KafkaProducer<>(props);

        String key = "zyy";
        int value = 1;
        producer.send(new ProducerRecord<>("intpu-left", "zyy", "6"));
        Thread.sleep(10000);
        producer.send(new ProducerRecord<>("intpu-right", "denglt", "3"));

        Thread.sleep(10000);
    }

}
