package dlt.study.guava.graph;

import com.google.common.graph.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

public class GraphDemo {

    static MutableGraph<City> graph = GraphBuilder.undirected()  //GraphBuilder.undirected().build();
            .allowsSelfLoops(true)
            .expectedNodeCount(10000)
            //.nodeOrder()
            .build();

    @BeforeClass
    public static void init() {
        graph.addNode(City.BEIJIN);
        graph.addNode(City.SHANGHAI);
        graph.addNode(City.GUANGZHOU);

        graph.putEdge(City.BEIJIN, City.SHANGHAI);
        graph.putEdge(City.BEIJIN, City.GUANGZHOU);
        graph.putEdge(City.SHENZHEN, City.BEIJIN);
    }

    @Test
    public void relation() {
        System.out.println("========adjacentNodes==临近节点============");// undirected时adjacentNodes、predecessors、successors无区别
        Set<City> cities = graph.adjacentNodes(City.BEIJIN);
        cities.forEach(System.out::println);

        System.out.println("=======predecessors====from Node======");
        cities = graph.predecessors(City.BEIJIN);
        cities.forEach(System.out::println);

        System.out.println("=======successors====to Node======"); //
        cities = graph.successors(City.BEIJIN);
        cities.forEach(System.out::println);

        System.out.println(graph.degree(City.BEIJIN)); // undirected时degree、inDegree、outDegree无区别
        System.out.println(graph.inDegree(City.BEIJIN));
        System.out.println(graph.outDegree(City.BEIJIN));
    }

    @Test
    public void immutable() {
        ImmutableGraph<City> cityImmutableGraph = ImmutableGraph.copyOf(graph);
        ElementOrder<City> cityElementOrder = cityImmutableGraph.nodeOrder();
        System.out.println(cityElementOrder);
        System.out.println(cityElementOrder == graph.nodeOrder());
        cityImmutableGraph.nodes().forEach(n -> n.setCode(n.getCode()+"&"));
        graph.nodes().forEach(System.out::println);
    }

    @Test
    public void edges(){
        Set<EndpointPair<City>> edges = graph.edges();
        edges.forEach( pair -> pair.nodeU());
    }
}
