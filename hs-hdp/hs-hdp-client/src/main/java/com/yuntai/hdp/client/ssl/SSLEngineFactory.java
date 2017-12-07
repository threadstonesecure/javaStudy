package com.yuntai.hdp.client.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Properties;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SSLEngineFactory {
	private static Log log = LogFactory.getLog(SSLEngineFactory.class);
	private static SSLContext sslContext;
	private final static String sslConfigFileName = "ssl.properties";
	static {
		Properties prop = new Properties();
		try {
			prop.load(SSLEngineFactory.class
					.getResourceAsStream(sslConfigFileName));
			log.info(" SSL Config loaded: " + sslConfigFileName);
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			throw new RuntimeException("SSL Config loading error: "
					+ sslConfigFileName, ex);
		}

		String protocol = prop.getProperty("hdp.ssl.protocol", "SSL");
		String storeType = prop.getProperty("hdp.ssl.storeType", "JKS");
		String keystore = prop
				.getProperty("hdp.ssl.keystore", "sHdpClient.jks");
		String keypass = prop.getProperty("hdp.ssl.keypass");
		String storepass = prop.getProperty("hdp.ssl.storepass");
		try {
			KeyStore ks = KeyStore.getInstance(storeType);
			InputStream in = SSLEngineFactory.class
					.getResourceAsStream(keystore);
			ks.load(in, storepass.toCharArray());

			KeyManagerFactory kmf = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm()); // SunX509
			kmf.init(ks, keypass.toCharArray()); // keypass

			TrustManagerFactory tf = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tf.init(ks);

			sslContext = SSLContext.getInstance(protocol);
			sslContext.init(kmf.getKeyManagers(), tf.getTrustManagers(), null);
			log.info("SSLContext is ok!");
		} catch (Exception ex) {
			sslContext = null;
			log.error(ex.getMessage(), ex);
			throw new RuntimeException("SSLContext  error: ", ex);
		}

	}

	public static SSLEngine createEngine() {
		SSLEngine engine = sslContext.createSSLEngine();
		// engine.setNeedClientAuth(true);
		engine.setUseClientMode(true);

		return engine;
	}

}
