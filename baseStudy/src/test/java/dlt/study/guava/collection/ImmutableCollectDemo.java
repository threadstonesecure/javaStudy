package dlt.study.guava.collection;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;


public class ImmutableCollectDemo {


    @Test
    public void immutableSet() {
        ImmutableSet<String> COLOR_NAMES = ImmutableSet.of(
                "red",
                "orange",
                "yellow",
                "green",
                "blue",
                "purple");

        ImmutableSet<String> copy = ImmutableSet.copyOf(COLOR_NAMES);


        ImmutableSet<String> GOOGLE_COLORS =
                ImmutableSet.<String>builder()
                        .addAll(COLOR_NAMES)
                        .add("black")
                        .build();
    }
}
