/**
 * 
 */
package dlt.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import dlt.utils.spring.PropertyConfigurer;

public class ConfigUtils {

	private static volatile DateFormat dateFormat;

	public static DateFormat getDateFormat() {
		if (dateFormat == null) {
			synchronized (ConfigUtils.class) {
				if (dateFormat == null) {
					String fmt = getDatePattern();
					dateFormat = new SimpleDateFormat(fmt);
					dateFormat.setLenient(false);
				}
			}

		}
		return dateFormat;
	}

	public static String getDatePattern() {
		return getProperty("sys.date.format", "yyyy-MM-dd HH:mm:ss");
	}

	public static String getProperty(String key) {
		return PropertyConfigurer.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return PropertyConfigurer.getProperty(key, defaultValue);
	}
}
