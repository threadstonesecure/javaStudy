package dlt.study.security;

import dlt.utils.ByteUtils;
import org.junit.Test;
import sun.security.jca.ProviderList;
import sun.security.jca.Providers;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by denglt on 2017/4/28.
 */
public class CipherDemo {

    @Test
    public void doRSA() throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptBytes = cipher.doFinal("sdfasdfsad".getBytes());
        System.out.println(ByteUtils.encodeHex(encryptBytes));
        byte[] encryptBytes2 = cipher.doFinal("sdfasdfsad".getBytes());
        System.out.println(ByteUtils.encodeHex(encryptBytes2));

        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        System.out.println(new String(decryptBytes));
        byte[] decryptBytes2 = cipher.doFinal(encryptBytes2);
        System.out.println(new String(decryptBytes2));
    }

    @Test
    public void doRSA2() throws Exception {
        StringBuffer sb = new StringBuffer();
        int keySize = 512;
        int length = keySize / 8 - 11;
        for (int i = 0; i < length; i++) {
            if (i == 0) sb.append("a");
            else if (i == length - 1) sb.append("a");
            else sb.append("b");
        }
        byte[] scrByte = sb.toString().getBytes();
        Cipher cipher = Cipher.getInstance("RSA");
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        System.out.println(privateKey.getEncoded().length);
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        System.out.println(publicKey.getEncoded().length);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        for (int i = 0; i < scrByte.length; i++) {
            byte[] bs = cipher.update(scrByte, i, 1);
            if (bs != null && bs.length > 0) // 一定为null数组，RSA只能拿到全量数据后进行加密
                System.out.println(i + ":" + ByteUtils.encodeHex(bs));
        }
        byte[] encryptBytes = cipher.doFinal();
        System.out.println(encryptBytes.length);
        System.out.println(ByteUtils.encodeHex(encryptBytes));

        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        System.out.println( new String(decryptBytes));

    }

    @Test
    public void des() throws Exception{
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        SecretKey key = new SecretKeySpec("dengltzy".getBytes(), "DES"); //密钥长度必须是8位
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] enc = cipher.doFinal("helloword".getBytes());

        cipher.init(Cipher.DECRYPT_MODE,key);
        System.out.println(new String(cipher.doFinal(enc)));
    }

    @Test
    public void HmacSHA512() throws Exception{
        Cipher cipher = Cipher.getInstance("HmacSHA512");
        SecretKey key = new SecretKeySpec("dengltzy".getBytes(), "HmacSHA512");
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] enc = cipher.doFinal("helloword".getBytes());

        cipher.init(Cipher.DECRYPT_MODE,key);
        System.out.println(new String(cipher.doFinal(enc)));
    }

    @Test
    public void d(){
        ProviderList var2 = Providers.getProviderList();
        System.out.println(var2);
    }
}
