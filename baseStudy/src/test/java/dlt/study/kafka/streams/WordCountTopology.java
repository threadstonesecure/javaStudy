package dlt.study.kafka.streams;

import dlt.study.log4j.Log;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WordCountTopology {
    public static void main(String[] args) throws Exception {
        Log.info(System.getProperty("user.dir"));
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-study-streams");//流处理应用的标识，对同一个应用需要一致，因为它是作为消费的group_id的
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.134:19092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        TopologyBuilder builder = new TopologyBuilder();
        builder.addSource("source", "words")
                .addProcessor("WordCountProcessor", WordCountProcessor::new, "source")
                .addStateStore(Stores.create("Counts").withStringKeys().withIntegerValues().persistent().build())
                .connectProcessorAndStateStores("WordCountProcessor", "Counts")
                .addSink("sink", "count", new StringSerializer(), new IntegerSerializer(),"WordCountProcessor"); // 流程
        KafkaStreams streams = new KafkaStreams(builder, props);
        streams.start();
        System.in.read();
        streams.close();
        streams.cleanUp();
    }
}
