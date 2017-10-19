package dlt.study.guava.collection;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import org.junit.Test;

public class ContiguousSetDemo {


    /**
     * 离散域
     */
    @Test
    public void discreteDomain(){
        DiscreteDomain<Integer> integers = DiscreteDomain.integers();
        integers.distance(12,50);


        Range<Integer> canonical = Range.closed(1, 10).canonical(integers);// 不明白？？？？
        System.out.println(canonical);

    }

    /**
     *相邻集合
     */
    @Test
    public void contiguousSet(){
        ContiguousSet<Integer> integers = ContiguousSet.create(Range.closed(1, 5), DiscreteDomain.integers());
        integers.forEach(System.out::println);


        ContiguousSet.create(Range.greaterThan(0), DiscreteDomain.integers());
    }



}
