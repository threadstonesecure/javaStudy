package dlt.study.kafka;


import dlt.study.log4j.Log;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class StreamsDemo {
    private static Map<String, Object> props = new HashMap<>();

    static {
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-study-streams");//流处理应用的标识，对同一个应用需要一致，因为它是作为消费的group_id的
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.134:19092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    @Test
    public void to() throws Exception {
        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        builder.stream("denglt").map((k, v) -> new KeyValue<>(k, v)).to("my-output-topic");

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.setStateListener(((newState, oldState) -> Log.info("状态改变：" + oldState + "->" + newState)));
        streams.start();
        System.in.read();
    }
}
