package dlt.study.guava.graph;

import com.google.common.graph.ElementOrder;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public class NetWorkDemo {

    static MutableNetwork<City, Flight> graph = NetworkBuilder.directed()
            .allowsParallelEdges(true).allowsSelfLoops(true)
            .nodeOrder(ElementOrder.sorted(new Comparator<City>() {
                @Override
                public int compare(City o1, City o2) {
                    return o1.getCode().compareTo(o2.getCode());
                }
            }))
            .edgeOrder(ElementOrder.sorted(new Comparator<Flight>() {
                @Override
                public int compare(Flight o1, Flight o2) {
                    return o1.getCode().compareTo(o2.getCode());
                }
            }))
            .expectedNodeCount(100000)
            .expectedEdgeCount(1000000)
            .build();


    @BeforeClass
    public static void init() {
        graph.addEdge(City.BEIJIN, City.SHANGHAI, new Flight("NH_BS_01", "NH_BS_01", BigDecimal.valueOf(1400)));
        graph.addEdge(City.BEIJIN, City.SHANGHAI, new Flight("NH_BS_02", "NH_BS_02", BigDecimal.valueOf(1450))); // parallel edge

        graph.addEdge(City.BEIJIN, City.GUANGZHOU, new Flight("NH_BG_01", "NH_BS_01", BigDecimal.valueOf(1460)));
        graph.addEdge(City.GUANGZHOU, City.BEIJIN, new Flight("NH_GZ_01", "NH_GZ_01", BigDecimal.valueOf(1400)));
        graph.addEdge(City.SHENZHEN, City.BEIJIN, new Flight("NH_SB_01", "NH_GZ_01", BigDecimal.valueOf(1400)));

        //错误数据  a Fight only connect once
        //Edge dlt.study.guava.graph.Flight@2d41edea already exists between the following nodes: <City{code='beijin', name='北京'} -> City{code='shanghai', name='上海'}>, so it cannot be reused to connect the following nodes: <City{code='shanghai', name='上海'} -> City{code='shanghai', name='上海'}>.
        //graph.addEdge(City.SHANGHAI, City.SHANGHAI, new Flight("NH_BS_01", "NH_BS_01", BigDecimal.valueOf(1400)));
    }

    @Test
    public void edgeConnecting() {
        Optional<Flight> flight = graph.edgeConnecting(City.BEIJIN, City.SHANGHAI);
        if (flight.isPresent()) {
            System.out.println(flight.get());
        } else {
            System.out.println("没有航班");
        }
    }

    @Test
    public  void edgesConnecting(){
        Set<Flight> flights = graph.edgesConnecting(City.BEIJIN, City.SHANGHAI);
        flights.forEach(System.out::println); //  edgeOrder 没有生效
    }

    @Test
    public void edges(){
        graph.edges().forEach(System.out::println); // edgeOrder
    }


}
