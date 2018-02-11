package dlt.study.kafka;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dlt.study.log4j.Log;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.Test;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.*;

public class ConsumerDemo {

    private static Properties props = new Properties();
    private static String topic = "denglt";

    static {
        String zooKeeper = "192.168.1.134:12181";
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.134:19092");
        // props.put("zookeeper.connect",zooKeeper);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "GroupA");//必须要
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);// 自动提交偏移量（offset）//禁用自动提交，并只有在线程完成处理后才为记录手动提交偏移量
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG //  最大poll的间隔，可以为消费者提供更多的时间去处理返回的消息，缺点是此值越大将会延迟组重新平衡
        //ConsumerConfig.MAX_POLL_RECORDS_CONFIG // 此设置限制每次调用poll返回的消息数
        //ConsumerConfig.AUTO_OFFSET_RESET_CONFIG // 决定consumer如何reset offset
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }


    /**
     * 订阅模式：由kafka自动分配partition
     */
    @Test
    public void subscribe() {
        topic = "count";
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "G_COUNT_2");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Lists.newArrayList(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                Log.info("onPartitionsRevoked -> " + Joiner.on(",").join(partitions));
                position();
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                Log.info("onPartitionsAssigned -> " + Joiner.on(",").join(partitions));
                position();
            }

            private void position() {
                //System.out.println(consumer.assignment().size());
                consumer.assignment().forEach((tp) -> {
                    long position = consumer.position(tp);
                    Log.info(String.format("group=%s, topic = %s, partition = %d, offset = %d", props.getProperty(ConsumerConfig.GROUP_ID_CONFIG),
                            tp.topic(), tp.partition(), position));
                });
            }
        });

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
           // consumer.pause();consumer.resume();
            for (ConsumerRecord<String, String> record : records)
                Log.info(String.format("topic = %s, partition = %d, offset = %d, key = %s, value = %s", record.topic(), record.partition(), record.offset(), record.key(), record.value()));

        }

        // Manual Offset Control
/*        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Lists.newArrayList(topic));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (TopicPartition topicPartition : records.partitions()) {
                ConsumerRecord<String, String> last = null;
                for (ConsumerRecord<String, String> record : records.records(topicPartition)) {
                    last = record;
                    Log.info(String.format("topic = %s, partition = %d, offset = %d, key = %s, value = %s", record.topic(), record.partition(), record.offset(), record.key(), record.value()));
                }
                long lastOffset = last.offset();
                consumer.commitSync(Collections.singletonMap(topicPartition, new OffsetAndMetadata(lastOffset + 1)));
            }

        }*/
    }

    /**
     * 指定消费的partition
     */
    @Test
    public void assgin() { //Manual Partition Assignment
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);  // 如果有消费者subscribe了topic，则commit会发生 CommitFailedException
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.assign(Lists.newArrayList(new TopicPartition(topic, 0)));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records)
                Log.info(String.format("topic = %s, partition = %d, offset = %d, key = %s, value = %s", record.topic(), record.partition(), record.offset(), record.key(), record.value()));
        }
    }

    @Test
    public void seek() {
        //props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        TopicPartition topicPartition = new TopicPartition(topic, 0);
        consumer.assign(Lists.newArrayList(topicPartition));
        consumer.seek(topicPartition, 100); // seek后 不会 auto commit
        consumer.commitSync(); // 会设置 consumer's offset = 100 //如果有消费者subscribe了topic，则commit会发生 CommitFailedException
        long position = consumer.position(topicPartition);
        Log.info(String.format("group=%s, topic = %s, partition = %d, offset = %d", props.getProperty(ConsumerConfig.GROUP_ID_CONFIG),
                topicPartition.topic(), topicPartition.partition(), position));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records)
                Log.info(String.format("topic = %s, partition = %d, offset = %d, key = %s, value = %s", record.topic(), record.partition(), record.offset(), record.key(), record.value()));
        }
    }


    /**
     * 获取消费者下个消息的offset
     */
    @Test
    public void position() {
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "GroupA");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); //[earliest|latest|none|anything else] 如果没有初始偏移量，或者当前的偏移量不再存在，该怎么办
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        consumer.assign(Lists.newArrayList(Iterables.transform(partitionInfos, (p) -> new TopicPartition(p.topic(), p.partition()))));

        partitionInfos.forEach((partitionInfo -> {
            Log.info(partitionInfo);
            TopicPartition topicPartition = new TopicPartition(partitionInfo.topic(), partitionInfo.partition());
            //consumer.assign(Lists.newArrayList(topicPartition));
            long position = consumer.position(topicPartition);
            Log.info(String.format("group=%s, topic = %s, partition = %d, offset = %d", props.getProperty(ConsumerConfig.GROUP_ID_CONFIG),
                    topicPartition.topic(), topicPartition.partition(), position));
        }));
    }

    /**
     * 查询指定时间点后没有消费的消息 offset
     */
    @Test
    public void offsetsForTimes() {
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        Map<TopicPartition, Long> timestampsToSearch = Maps.newHashMap();
        partitionInfos.forEach(t -> timestampsToSearch.put(new TopicPartition(t.topic(), t.partition()), System.currentTimeMillis() - 6000000));
        // timestampsToSearch.forEach((k, v) -> System.out.println(k + ":" + v));
        System.out.println(consumer.offsetsForTimes(timestampsToSearch).size());
        consumer.offsetsForTimes(timestampsToSearch).forEach((topicPartition, v) -> {
            Log.info(String.format("group=%s, topic = %s, partition = %d, offset = %d", props.getProperty(ConsumerConfig.GROUP_ID_CONFIG),
                    topicPartition.topic(), topicPartition.partition(), v.offset()));
        });
    }

    /**
     * 与消费者无关
     * Get the first offset for the given partitions.
     */
    @Test
    public void beginningOffsets() {
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        consumer.beginningOffsets(Lists.newArrayList(Iterables.transform(partitionInfos, t -> new TopicPartition(t.topic(), t.partition()))))
                .forEach((tp, p) -> Log.info(String.format("topic = %s, partition = %d, offset = %d", tp.topic(), tp.partition(), p)));
    }

    /**
     * 与消费者无关
     * Get the last offset for the given partitions.
     * The last offset of a partition is the offset of the upcoming message, i.e. the offset of the last available message + 1.
     */
    @Test
    public void endOffsets() {
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "dengltxx");
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        consumer.endOffsets(Lists.newArrayList(Iterables.transform(partitionInfos, t -> new TopicPartition(t.topic(), t.partition()))))
                .forEach((tp, p) -> Log.info(String.format("topic = %s, partition = %d, offset = %d", tp.topic(), tp.partition(), p)));
    }

    @Test
    public void consumerWithOutGroupID() { //org.apache.kafka.common.errors.InvalidGroupIdException: The configured groupId is invalid
        props.remove(ConsumerConfig.GROUP_ID_CONFIG);
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Lists.newArrayList(topic));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records)
                Log.info(String.format("topic = %s, partition = %d, offset = %d, key = %s, value = %s", record.topic(), record.partition(), record.offset(), record.key(), record.value()));
        }
    }


    @Test
    public void infoTopic(){
        topic = "denglt";
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        Log.info("begin offsets:");
        consumer.beginningOffsets(Lists.transform(partitionInfos, t -> new TopicPartition(t.topic(), t.partition())))
                .forEach((tp, p) -> Log.info(String.format("topic = %s, partition = %d, offset = %d", tp.topic(), tp.partition(), p)));

        Log.info("end offsets:");
        consumer.endOffsets(Lists.transform(partitionInfos, t -> new TopicPartition(t.topic(), t.partition())))
                .forEach((tp, p) -> Log.info(String.format("topic = %s, partition = %d, offset = %d", tp.topic(), tp.partition(), p)));
    }

}
