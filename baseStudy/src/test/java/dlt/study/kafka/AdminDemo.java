package dlt.study.kafka;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AdminDemo {

    private static Properties props = new Properties();
    private static AdminClient adminClient;

    static {

        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.134:19092");
        adminClient = AdminClient.create(props);
    }

    /**
     * kafka cluster info
     *
     * @throws Exception  get() has throw
     */
    @Test
    public void cluster() throws Exception {
        DescribeClusterResult describeClusterResult = adminClient.describeCluster();
        KafkaFuture<Collection<Node>> nodes = describeClusterResult.nodes();
        nodes.get().forEach(System.out::println);

        Node node = describeClusterResult.controller().get();
        System.out.println("controller:" + node);

        String clusterId = describeClusterResult.clusterId().get();
        System.out.println("clusterId:" + clusterId);
    }

    @Test
    public void listTopics() throws Exception {
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        KafkaFuture<Collection<TopicListing>> kafkaFuture = listTopicsResult.listings();

        adminClient.describeTopics(Collections2.transform(kafkaFuture.get(), TopicListing::name)).all().get().forEach((k, v) -> {
            //System.out.println(k + "->" );
            System.out.println(v.name() + " -> partitions:");
            v.partitions().forEach(p -> System.out.println("   " + p));
        });
    }

    /**
     * info with offset (begin and end)
     */
    @Test
    public void infoTopic() {
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        Map<String, List<PartitionInfo>> stringListMap = consumer.listTopics();

        ArrayList<PartitionInfo> pis = stringListMap.values().stream().collect(Collector.of(ArrayList::new, List::addAll, (l, r) -> null));

        PartitionsResult partitionsResult = pis.stream().collect(Collector.of(PartitionsResult::new, PartitionsResult::add, (l, r) -> null));

/*        consumer.beginningOffsets(Lists.transform(partitionsResult.partitions, input -> new TopicPartition(input.topic, input.partition)))
                .forEach((tp, offset) -> partitionsResult.add(new BeginOffset(tp, offset)));*/
        // same
        consumer.beginningOffsets(partitionsResult.partitions.stream().map(t -> new TopicPartition(t.topic, t.partition)).collect(Collectors.toList()))
                .forEach((tp, offset) -> partitionsResult.add(new BeginOffset(tp, offset)));

        consumer.endOffsets(Lists.transform(partitionsResult.partitions, input -> new TopicPartition(input.topic, input.partition)))
                .forEach((tp, offset) -> partitionsResult.add(new EndOffset(tp, offset)));

        // partitionsResult.partitions.forEach(System.out::println);

        Map<String, List<PartitionInfoWithOffset>> topicPiwoList = partitionsResult.partitions.stream().collect(Collectors.groupingBy(t -> t.topic));
        topicPiwoList.forEach((k, v) -> {
            System.out.println(String.format("topic【%s】", k));
            v.stream().sorted(Comparator.comparingInt(t -> t.partition)).forEach(System.out::println);
        });

    }

    @Test
    public void listInternalTopics() throws Exception {
        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);
        ListTopicsResult listTopicsResult = adminClient.listTopics(listTopicsOptions);
        KafkaFuture<Collection<TopicListing>> kafkaFuture = listTopicsResult.listings();

        adminClient.describeTopics(Collections2.transform(kafkaFuture.get(), TopicListing::name)).all().get().forEach((k, v) -> {
            //System.out.println(k + "->" );
            System.out.println(v.name() + " -> partitions:");
            v.partitions().forEach(p -> System.out.println("   " + p));
        });
    }

    @Test
    public void describeConfigs() throws Exception {
        DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Lists.newArrayList(new ConfigResource(ConfigResource.Type.TOPIC, "__consumer_offsets")));
        describeConfigsResult.all().get().forEach((k, v) -> {
            System.out.println(k);
            v.entries().forEach(System.out::println);
        });
    }

    private class PartitionsResult {
        private List<PartitionInfoWithOffset> partitions;

        private PartitionsResult() {
            partitions = Lists.newArrayList();
        }

        public void add(Object p) {
            if (p instanceof PartitionInfo) {
                PartitionInfo pi = (PartitionInfo) p;
                findAndNew(pi.topic(), pi.partition()).set(pi);
            }

            if (p instanceof BeginOffset) {
                BeginOffset bo = (BeginOffset) p;
                findAndNew(bo.topic, bo.partition).beginOffset = bo.offset;
            }

            if (p instanceof EndOffset) {
                EndOffset eo = (EndOffset) p;
                findAndNew(eo.topic, eo.partition).endOffset = eo.offset;
            }
        }

        private PartitionInfoWithOffset findAndNew(String topic, int partition) {

            //PartitionInfoWithOffset piwo = Iterables.find(partitions, t -> t.topic.equals(topic) && t.partition == partition, null);
            PartitionInfoWithOffset piwo = partitions.stream().filter(t -> t.topic.equals(topic) && t.partition == partition).findFirst().orElse(null);
            if (piwo == null) {
                piwo = new PartitionInfoWithOffset();
                partitions.add(piwo);
            }
            return piwo;
        }

    }

    private class BeginOffset {
        private String topic;
        private int partition;
        private long offset;

        private BeginOffset(TopicPartition tp, Long offset) {
            topic = tp.topic();
            partition = tp.partition();
            this.offset = offset;
        }
    }

    private class EndOffset {
        private String topic;
        private int partition;
        private long offset;

        private EndOffset(TopicPartition tp, Long offset) {
            topic = tp.topic();
            partition = tp.partition();
            this.offset = offset;
        }
    }

    private class PartitionInfoWithOffset {
        private String topic;
        private int partition;
        private long beginOffset;
        private long endOffset;
        private Node leader;
        private List<Node> replicas;
        private List<Node> isr;

        void set(PartitionInfo p) {
            partition = p.partition();
            topic = p.topic();
            leader = p.leader();
            replicas = Lists.newArrayList(p.replicas());
            isr = Lists.newArrayList(p.inSyncReplicas());
        }

        @Override
        public String toString() {
            return "PartitionInfo{" +
                    "topic='" + topic + '\'' +
                    ", partition=" + partition +
                    ", beginOffset=" + beginOffset +
                    ", endOffset=" + endOffset +
                    ", leader=" + leader +
                    ", replicas=" + replicas +
                    ", isr=" + isr +
                    '}';
        }
    }
}
