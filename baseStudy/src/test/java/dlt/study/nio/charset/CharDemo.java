package dlt.study.nio.charset;

import dlt.utils.ByteUtils;
import org.jbpm.util.ByteUtil;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Created by denglt on 2017/5/5.
 */
public class CharDemo {

    @Test
    public void intAndChar() throws Exception {
        char c = '邓';
        int i = (int)c; // Unicode code point
        System.out.println(i);
        c = (char) i;
        System.out.println(c);

        String b = String.valueOf(c);
        byte[] bs = b.getBytes("US-ASCII");
        System.out.println(bs.length);
        System.out.println(ByteUtils.byteToInt(bs[0]));
    }

    @Test
    public void intAndChar2() throws Exception {
        char c = '邓';
        int i = c;
        System.out.println(i);
        c = (char) i;
        System.out.println(c);

        String b = String.valueOf(c);
        String csn = Charset.defaultCharset().name();
        System.out.println(csn);
        byte[] bs = b.getBytes();
        System.out.println(bs.length);
        System.out.println(b.codePointAt(0)); //Unicode code point

    }
}
