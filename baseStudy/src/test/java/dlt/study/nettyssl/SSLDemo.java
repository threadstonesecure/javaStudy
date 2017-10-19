package dlt.study.nettyssl;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public class SSLDemo {

	public static void main(String[] args) throws Exception {
		System.out.println();//
		KeyManagerFactory kmf = null;
		KeyStore ks = KeyStore.getInstance("JKS");
		FileInputStream in = new FileInputStream(
				"D:\\mywork\\keystore\\sHdpServer.jks");
		ks.load(in, "yuntai".toCharArray()); // storepass

		kmf = KeyManagerFactory.getInstance(KeyManagerFactory
				.getDefaultAlgorithm()); // SunX509
		kmf.init(ks, "yuntai".toCharArray()); // keypass
		KeyManager[] kms = kmf.getKeyManagers();
		System.out.println(kms);
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(kms, null, null);
		SSLEngine engine = sslContext.createSSLEngine();
		engine.setUseClientMode(false);
		new SslHandler(engine);
	}
}
