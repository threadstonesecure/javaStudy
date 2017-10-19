package dlt.utlis;

import dlt.utils.ByteUtils;
import org.junit.Test;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Arrays;

/**
 * Created by denglt on 2017/5/2.
 */
public class ByteUtilsDemo {

    @Test
    public void intToByteArray() {
        byte[] byteArray = ByteUtils.intToByteArray(Integer.MAX_VALUE);
        for (byte b : byteArray) {
            System.out.println(b);
        }
    }

    @Test
    public void byteArrayToInt() {
        for (int i = 0; i<= Integer.MAX_VALUE;i++) {
            byte[] byteArray = ByteUtils.intToByteArray(i);
            int j = ByteUtils.byteArrayToInt(byteArray);
            assert i == j;
        }
    }

    @Test
    public void work() {
        double d =  1;
        long l = Double.doubleToLongBits(d);  // Double.longBitsToDouble()
        System.out.println(l);
        d = Double.longBitsToDouble(l);
        System.out.println(d);

        System.out.println("Long.MAX_VALUE:" + Long.MAX_VALUE);
        System.out.println("Double.MAX_VALUE:" + Double.MAX_VALUE);
    }

    @Test
    public void intToByte() {
        int i  = 156;
        byte b = (byte) i;
        System.out.println(b);
        System.out.println((int) b);
        System.out.println(b & 0xFF);
    }

    @Test
    public void byteToInt(){
        byte b = -56;
        int i = b;
        System.out.println((byte) i);
    }
}
