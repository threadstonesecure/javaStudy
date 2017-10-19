package dlt.study.resource;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class ReadDemo {

	public static void main(String[] args) throws Exception {
		URL classPath = ReadDemo.class.getResource("");

		System.out.println(classPath.getPath());
		System.out.println(classPath.getFile());
		try {
			FileInputStream in = new FileInputStream(classPath.getFile());
			in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ReadDemo.class.getResourceAsStream("keystore");
		System.out.println("ok");

		Properties prop = new Properties();
		prop.load(ReadDemo.class.getResourceAsStream("ssl.config"));
		String keystore = prop.getProperty("hdp.ssl.keystore");
		System.out.println("hdp.ssl.keystore:" + keystore);
	}

}
