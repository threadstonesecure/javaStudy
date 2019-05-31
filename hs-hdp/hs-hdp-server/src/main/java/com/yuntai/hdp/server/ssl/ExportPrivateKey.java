package com.yuntai.hdp.server.ssl;

import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;

/**
 * @Description:
 * @Package: com.yuntai.hdp.server.ssl
 * @Author: denglt
 * @Date: 2019-05-31 09:44
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class ExportPrivateKey {
    private File keystoreFile;
    private String keyStoreType;
    private char[] keyStorePassword;
    private char[] keyPassword;
    private String alias;
    private File exportedFile;

    public void export() throws Exception {
        KeyStore keystore = KeyStore.getInstance(keyStoreType);
        BASE64Encoder encoder = new BASE64Encoder();
        keystore.load(new FileInputStream(keystoreFile), keyStorePassword);
        Key key = keystore.getKey(alias, keyPassword);
        String encoded = encoder.encode(key.getEncoded());
        FileWriter fw = new FileWriter(exportedFile);
        fw.write("-----BEGIN PRIVATE KEY-----\n");
        fw.write(encoded);
        fw.write("\n");
        fw.write("-----END PRIVATE KEY-----");
        fw.close();
    }

    public static void main(String[] args) throws Exception {
        ExportPrivateKey export = new ExportPrivateKey();
        export.keyStoreType = "JKS";
        export.keyPassword = "yuntai".toCharArray();
        export.keyStorePassword = "yuntai".toCharArray();
        export.alias = "securehdpserver";
        export.keystoreFile = new File("/Users/denglt/onGithub/javaStudy/hs-hdp/hs-hdp-server/src/main/java/com/yuntai/hdp/server/ssl/sHdpServer.jks");
        export.exportedFile = new File("/Users/denglt/onGithub/javaStudy/hs-hdp/hs-hdp-server/src/main/java/com/yuntai/hdp/server/ssl/server.key");
        export.export();
    }

}
