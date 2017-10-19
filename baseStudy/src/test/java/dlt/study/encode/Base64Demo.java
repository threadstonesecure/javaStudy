package dlt.study.encode;

import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Created by denglt on 2016/12/27.
 */
public class Base64Demo {

    @Test
    public void encode(){
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "邓隆通dengltsd";
        String encodeStr = encoder.encode(str.getBytes());
        System.out.println(encodeStr);
    }

    @Test
    public void  decode() throws IOException {
        String base64Str = "6YKT6ZqG6YCaZGVuZ2x0c2Q="; // "6qfKQ7ns1gAS9g==";
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] deocde  = decoder.decodeBuffer(base64Str);
        System.out.print(new String(deocde));
    }
}
