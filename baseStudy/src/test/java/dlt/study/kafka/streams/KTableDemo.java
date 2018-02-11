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
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
        produce();
        String topic = "study";
        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        builder.table(topic).toStream().foreach((k, v) -> Log.info(k + "->" + v)); // 只有denglt->3 一条记录
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

        String key = "denglt";
        String value = "1";
        producer.send(new ProducerRecord<>(topic, key, value));

        key = "denglt";
        value = "3";
        producer.send(new ProducerRecord<>(topic, key, value));
        Thread.sleep(10000);

    }

    @Test
    public leftJoin(){
        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        KTable<String, String> left = builder.table("intpu-left");
        KTable<String, String> right = builder.table("intpu-right");
        left.leftJoin(right)
    }

}
