package dlt.study.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;

/**
 * @Description:
 * @Package: dlt.study.reactor
 * @Author: denglt
 * @Date: 2018/9/13 上午10:37
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class FluxDemo {

    @Test
    public void just() {
        Flux.just("hello", "world", "!").subscribe(System.out::println);
    }
}
