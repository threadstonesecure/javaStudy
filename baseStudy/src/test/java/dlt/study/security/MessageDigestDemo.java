package dlt.study.security;

import org.junit.Test;
import sun.security.jca.GetInstance;
import sun.security.jca.ProviderList;
import sun.security.jca.Providers;
import sun.security.provider.MD5;

import java.security.MessageDigest;
import java.security.MessageDigestSpi;

/**
 * Created by denglt on 2017/4/27.
 */
public class MessageDigestDemo {

    @Test
    public void md5() throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");//sun.security.provider.MD5
        messageDigest.update("denglt".getBytes());
        messageDigest.update("zyy".getBytes());
        byte[] md5Byte = messageDigest.digest();
        System.out.println(messageDigest.getClass());
        System.out.println(encodeHex(md5Byte)); // ba71ddcd66473d43c57db651fa9466fb
        System.out.println(encodeHex(md5Byte, DIGITS_LOWER));

        System.out.println(org.apache.commons.codec.digest.DigestUtils.md5Hex("dengltzyy"));
        System.out.println(org.springframework.util.DigestUtils.md5DigestAsHex("dengltzyy".getBytes()));

    }


    @Test
    public void md52() throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("deng".getBytes());
        messageDigest.update("ltzyy".getBytes());
        byte[] md5Byte = messageDigest.digest();
        System.out.println(encodeHex(md5Byte));

    }

    @Test
    public void md5Long() throws Exception{  // sun.security.provider.MD5 不会溢出，最大开辟byte[64]的内存
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte b = 0;
        messageDigest.update(b);
        for (int i=0;i<100000000;i++) {
            messageDigest.update("12345678901234567890".getBytes());
            //messageDigest.update("ltzyy".getBytes());
        }
        messageDigest.update(b);
        byte[] md5Byte = messageDigest.digest();
        System.out.println(encodeHex(md5Byte));
    }

    @Test
    public void getMd5() throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        System.out.println(messageDigest);
        Object[] objs = GetInstance.getInstance("MessageDigest", MessageDigestSpi.class, "MD5").toArray();
        System.out.println(objs[0]);
        System.out.println(objs[1]);
    }


    @Test
    public void testEncodeHex() {
        byte[] md5Byte = new byte[]{-128};
        System.out.println(encodeHex(md5Byte,DIGITS_LOWER));
    }


    @Test
    public void int2Byte(){
        int i =  128*10+1;
        byte b = (byte)i;
        System.out.println(b);

        System.out.println(b & 0xFF);
    }

    private static char[] encodeHex(byte[] bytes) {
        char chars[] = new char[32];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return chars;
    }

    private static final char[] HEX_CHARS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    // good  //字节转16进制
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

}
