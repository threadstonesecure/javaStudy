package dlt.study.security;

import org.junit.Test;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class SignatureDemo {

    /*
      RSA
     */
    @Test()
    public void rsaSigner() throws Exception {

        //KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        //初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024);
        //生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        Signature sign = Signature.getInstance("SHA1withRSA");

        sign.initSign(privateKey);
        sign.update("denglt".getBytes());
        sign.update("zyy".getBytes());
        byte[] signData = sign.sign();

        Signature verifySign = Signature.getInstance("SHA1withRSA");
        verifySign.initVerify(publicKey);
        //用于验签的数据
        verifySign.update("dengltzyy".getBytes());
        boolean flag = verifySign.verify(signData);
        System.out.println(flag);

    }

    /**
     * 6HmacSHA512|HmacSHA384|HmacSHA25666666
     */
    @Test
    public void macSigner() throws Exception {
        Mac hmacSHA512 = Mac.getInstance("HmacSHA512");
        SecretKey key = new SecretKeySpec("dengltzyy".getBytes(), "HmacSHA512");
        hmacSHA512.init(key);
        byte[] signData = hmacSHA512.doFinal("denglt".getBytes());
        // Mac 没有verify方法，重新对数据签名，再进行比较
    }
}
