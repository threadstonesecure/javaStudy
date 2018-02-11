package dlt.study.kafka.streams;


import com.beust.jcommander.internal.Lists;
import dlt.domain.model.User;
import dlt.study.log4j.Log;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class KStreamDemo {
    private static Map<String, Object> props = new HashMap<>();

    static {
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-study-streams");//流处理应用的标识，对同一个应用需要一致，因为它是作为消费的group_id的
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.134:19092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    /**
     * 转换topic 到另一个topic
     *
     * @throws Exception
     */
    @Test
    public void mapAndTo() throws Exception {
        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        builder.stream("denglt").filter((k, v) -> true).map((k, v) -> new KeyValue<>(k, v)).to("my-output-topic");
        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.setStateListener(((newState, oldState) -> Log.info("状态改变：" + oldState + "->" + newState)));
        streams.start();
        System.in.read();
        //Thread.sleep(60000);
        streams.close();
        streams.cleanUp();
    }

    @Test
    public void streamDemo() {
        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        // builder.stream("").flatMap( k,v -> Lists.newArrayList(k,v) ).groupBy().count().toStream().to("");

        //builder.table("").filter().toStream().to("");

        // KStream<String, Order> orders = builder.stream(Serdes.String(), Serdes.serdeFrom(Order.class), "orders");
    }


    @Test
    public void process() {
        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        builder.stream("denglt").process((ProcessorSupplier) () -> new WordCountProcessor(), "denglt");

        builder.stream("denglt").process((ProcessorSupplier) WordCountProcessor::new, "denglt");

        builder.addSource("source", "words")
                .addProcessor("WordCountProcessor", WordCountProcessor::new, "source");

    }


    @Test
    public void leftJoin() {
        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        KStream<String, String> left = builder.stream("intpu-left");
        KStream<String, String> right = builder.stream("intpu-right");
        KStream<String, String> all = left.selectKey((key, value) -> value.split(",")[1])
                .join(right.selectKey((key, value) -> value.split(",")[0]), new ValueJoiner<String, String, String>() {
                    @Override
                    public String apply(String value1, String value2) {
                        return value1 + "--" + value2;
                    }
                }, JoinWindows.of(30000));

        all.print();
    }
}
