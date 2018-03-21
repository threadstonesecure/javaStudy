package dlt.study.kafka.streams;


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
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.state.Stores;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/*
high-level DSL
 */
public class KStreamDemo {
    private static Map<String, Object> props = new HashMap<>();

    static {
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-study-streams2");//流处理应用的标识，对同一个应用需要一致，因为它是作为消费的group_id的
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
                .addProcessor("WordCountProcessor", WordCountProcessor::new, "source")
                .addStateStore(Stores.create("Counts").withStringKeys().withIntegerValues().persistent().build())
                .connectProcessorAndStateStores("WordCountProcessor", "Counts");

    }


    /**
     * 对于Join操作，如果要得到正确的计算结果，需要保证参与Join的KTable或KStream中Key相同的数据被分配到同一个Task
     * 1. 参与Join的KTable或KStream的Key类型相同（实际上，业务含意也应该相同）
     * 2. 参与Join的KTable或KStream对应的Topic的Partition数相同
     * 3. Partitioner策略的最终结果等效（实现不需要完全一样，只要效果一样即可），也即Key相同的情况下，被分配到ID相同的Partition内
     * 如果上述条件不满足，可通过调用如下方法使得它满足上述条件。
     * 1、KStream<K, V> through(Serde<K> keySerde, Serde<V> valSerde, StreamPartitioner<K, V> partitioner, String topic)
     * 2、selectKey
     *
     * @throws Exception
     */
    @Test
    public void join() throws Exception {
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-study-leftjoin7");
        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        KStream<String, String> left = builder.stream("intpu-left");
        KStream<String, String> right = builder.stream("intpu-right");
        KStream<String, String> all = left.filter((k, v) -> v.split("[,.]")[1].equals("a")).selectKey((key, value) -> value.split("[,.]")[1])
                .leftJoin(right.selectKey((key, value) -> value.split("[,.]")[0]), new ValueJoiner<String, String, String>() {
                    @Override
                    public String apply(String value1, String value2) {
                        return value1 + "--" + value2;
                    }
                }, JoinWindows.of(30000)); // 这儿的时间窗口，是以记录产生的时间点来比较
        /**  SELECT * FROM stream1, stream2
         WHERE
         stream1.key = stream2.key
         AND
         stream1.ts - before <= stream2.ts AND stream2.ts <= stream1.ts + after
         */

        all.print();
        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();
        System.in.read();
        streams.close();
        streams.cleanUp();
    }

    public static void main(String[] args) {
        for (String s : "denglt.zyy,zdy".split("[,.]")) {
            System.out.println(s);
        }
    }
}
