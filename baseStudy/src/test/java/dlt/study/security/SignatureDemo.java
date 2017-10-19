package dlt.study.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class SignatureDemo {

    public static void main(String[] args) throws Exception {

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
}
