package dlt.study.guava.graph;

import com.google.common.graph.ElementOrder;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Comparator;
import java.util.Optional;

public class ValueGraphDemo {
    static MutableValueGraph<City, Distance> graph = ValueGraphBuilder.directed()
            .allowsSelfLoops(true)
            .expectedNodeCount(10000)
            .nodeOrder(ElementOrder.sorted(new Comparator<City>() {
                @Override
                public int compare(City o1, City o2) {
                    return o1.getCode().compareTo(o2.getCode());
                }
              }))
            .build();

    @BeforeClass
    public static void init() {
/*        graph.addNode(City.BEIJIN);
        graph.addNode(City.SHANGHAI);
        graph.addNode(City.GUANGZHOU);*/

        graph.putEdgeValue(City.BEIJIN, City.SHANGHAI, Distance.of(1000));
        graph.putEdgeValue(City.BEIJIN, City.GUANGZHOU, Distance.of(1100));
        graph.putEdgeValue(City.GUANGZHOU, City.BEIJIN, Distance.of(1000));
    }

    @Test
    public void edgeValue() {
        Optional<Distance> distance = graph.edgeValue(City.BEIJIN, City.GUANGZHOU);
        if (distance.isPresent()) {
            System.out.println(distance.get());
        } else {
            System.out.println("没有数据");
        }

        distance = graph.edgeValue(City.GUANGZHOU, City.BEIJIN);
        if (distance.isPresent()) {
            System.out.println(distance.get());
        } else {
            System.out.println("没有数据");
        }

    }

    @Test
    public void nodeOrder() {
        ElementOrder<City> cityElementOrder = graph.nodeOrder();
        System.out.println(cityElementOrder);

        graph.nodes().forEach(System.out::println);

        System.out.println("=========");
        graph.successors(City.BEIJIN).forEach(System.out::println);
    }

}
