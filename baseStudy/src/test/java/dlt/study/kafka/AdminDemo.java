package dlt.study.kafka;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import dlt.study.log4j.Log;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

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
     * @throws Exception
     */
    @Test
    public void cluster() throws Exception {
        DescribeClusterResult describeClusterResult = adminClient.describeCluster();
        KafkaFuture<Collection<Node>> nodes = describeClusterResult.nodes();
        nodes.get().forEach(node -> {
            System.out.println(node);
        });

        Node node = describeClusterResult.controller().get();
        System.out.println("controller:" + node);

        String clusterId = describeClusterResult.clusterId().get();
        System.out.println("clusterId:" + clusterId);
    }

    @Test
    public void listTopics() throws Exception {
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        KafkaFuture<Collection<TopicListing>> kafkaFuture = listTopicsResult.listings();

        adminClient.describeTopics(Collections2.transform(kafkaFuture.get(), v -> v.name())).all().get().forEach((k, v) -> {
            //System.out.println(k + "->" );
            System.out.println(v.name() + " -> partitions:");
            v.partitions().forEach(p -> System.out.println("   " + p));
        });
    }

    @Test
    public void listInternalTopics() throws Exception {
        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);
        ListTopicsResult listTopicsResult = adminClient.listTopics(listTopicsOptions);
        KafkaFuture<Collection<TopicListing>> kafkaFuture = listTopicsResult.listings();

        adminClient.describeTopics(Collections2.transform(kafkaFuture.get(), v -> v.name())).all().get().forEach((k, v) -> {
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

}
