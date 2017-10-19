package dlt.study.guava;

import com.google.common.math.IntMath;
import org.junit.Test;

import java.math.RoundingMode;

public class IntMathDemo {

    @Test
    public void divide() {
        int divide = IntMath.divide(10, 3, RoundingMode.CEILING);
        System.out.println(divide);
        System.out.println(10 / 3);
    }

    @Test
    public void log2() {
        int i = IntMath.log2(8, RoundingMode.CEILING);
        System.out.println(i);
    }

    @Test
    public void mod() {
        System.out.println(-5 / 3 + ":" + -5 % 3);
        System.out.println(5 % 3);
        System.out.println(IntMath.divide(-5,3 ,RoundingMode.HALF_UP) +":"+IntMath.mod(-5,3));
    }
}
