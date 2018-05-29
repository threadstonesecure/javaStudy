package dlt.study.guava.collection;

import com.google.common.base.Optional;
import org.junit.Test;

public class OptionalDemo {

    @Test
    public void newOptional(){
        Optional<Integer> possible = Optional.of(5);
        System.out.println(possible.isPresent()); // returns true
        System.out.println(possible.get());

    }
}
