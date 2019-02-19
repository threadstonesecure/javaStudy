package dlt.study.kafka;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import kafka.admin.AdminClient;
import kafka.coordinator.group.GroupOverview;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;
import scala.Function1;
import scala.Option;
import scala.collection.Iterator;
import scala.collection.immutable.List;
import scala.collection.immutable.Map;
import scala.collection.JavaConversions;


import java.util.Properties;

public class ScalaAdminDemo {
    private static Properties props = new Properties();
    private static AdminClient adminClient;

    static {
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        adminClient = AdminClient.create(props);
    }


    /**
     * 查询消费组信息和offsets
     */
    @Test
    public void consumerGroups() {
        Map<Node, List<GroupOverview>> nodeListMap = adminClient.listAllConsumerGroups();
        JavaConversions.mapAsJavaMap(nodeListMap).forEach((Node k, List<GroupOverview> v) -> {

            JavaConversions.asJavaIterable(v).forEach((GroupOverview g) -> {
                System.out.println("消费组【" + g + "】:");  // Group
                AdminClient.ConsumerGroupSummary consumerGroupSummary = adminClient.describeConsumerGroup(g.groupId(), 10000);
                Option<List<AdminClient.ConsumerSummary>> consumers = consumerGroupSummary.consumers();
                Iterable<AdminClient.ConsumerSummary> consumerSummaries = JavaConversions.asJavaIterable(consumers.get());

                JavaConversions.mapAsJavaMap(adminClient.listGroupOffsets(g.groupId())).forEach((TopicPartition tp, Object obj) -> { // topic、partition and offset

                    AdminClient.ConsumerSummary consumerSummary = Iterables.find(consumerSummaries, (AdminClient.ConsumerSummary cs) -> {
                        return Lists.newArrayList(JavaConversions.asJavaIterable(cs.assignment())).contains(tp);
                    }, null);

                    System.out.println(String.format("topic[%s]:partition[%s]:offset[%s]:owner[%s]",
                            tp.topic(), tp.partition(), obj, consumerSummary != null ? consumerSummary.consumerId() : "no runner"));
                });

                System.out.println();
            });
        });
    }

    @Test
    public void describeConsumerGroup() {
        AdminClient.ConsumerGroupSummary consumerGroupSummary = adminClient.describeConsumerGroup("my-study-streams", 10000);
        Option<List<AdminClient.ConsumerSummary>> consumers = consumerGroupSummary.consumers();
        JavaConversions.asJavaIterable(consumers.get()).forEach((AdminClient.ConsumerSummary consumerSummary) -> {
            System.out.println(consumerSummary.assignment());
            System.out.println(consumerSummary.consumerId());
            System.out.println(consumerSummary.clientId());
            System.out.println(consumerSummary.host());
            //  JavaConversions.asJavaIterator(consumerSummary.productIterator()).forEachRemaining(System.out::println);
        });
    }
}
