package dlt.utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;

/**
 * json操作工具类
 * @author dlt
 */
public final class JsonUtils {
	private static final ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setDateFormat(ConfigUtils.getDateFormat());
	}

	private JsonUtils() {
	}

	public static ObjectMapper getMapper() {
		return mapper;
	}

	public static String toJson(Object jsonObj) {
		try {
			return mapper.writeValueAsString(jsonObj);
		} catch (IOException e) {
			throw new IllegalArgumentException("parser JSON ", e);
		}
	}

	/**
	 * T 为简单对象
	 * 
	 * @param jsonStr
	 * @param cls
	 * @return
	 */
	public static <T> T toObject(String jsonStr, Class<T> cls) {
		try {
			return mapper.readValue(jsonStr, cls);
		} catch (IOException e) {
			throw new IllegalArgumentException("parser JSON ", e);
		}
	}

	/**
	 * 支持泛型
	 * 
	 * @param jsonStr
	 * @param type
	 * @param contextClass
	 * @return
	 */
	@Deprecated
	/* 使用 toList 或 toMap */
	public static Object toObject(String jsonStr, TypeReference<?> valueTypeRef) {
		return readValue(jsonStr, valueTypeRef);
	}

	@Deprecated
	public static Object toObject(String jsonStr, Type type,
			Class<?> contextClass) {
		JavaType javaType = getJavaType(type, contextClass);
		return readValue(jsonStr, javaType);
	}

	public static Map<String, Object> toMap(String jsonStr) {
		Map<String, Object> map = readValue(jsonStr,
				new TypeReference<Map<String, Object>>() {
				});
		return map;
	}

	public static <T> List<T> toList(String jsonStr, Class<T> beanClass) {
		JavaType javaType = mapper.getTypeFactory().constructCollectionType(
				List.class, beanClass);
		List<T> list = readValue(jsonStr, javaType);
		return list;

	}

	public static <V> Map<String, V> toMap(String jsonStr, Class<V> vClass) {
		MapType mapType = mapper.getTypeFactory().constructMapType(Map.class,
				String.class, vClass);

		Map<String, V> map = readValue(jsonStr, mapType);
		return map;
	}

	public static <K, V> Map<K, V> toMap(String jsonStr, Class<K> kClass,
			Class<V> vClass) {
		MapType mapType = mapper.getTypeFactory().constructMapType(Map.class,
				kClass, vClass);

		Map<K, V> map = readValue(jsonStr, mapType);
		return map;
	}

	private static JavaType getJavaType(Type type, Class<?> contextClass) {
		return mapper.getTypeFactory().constructType(type, contextClass);
	}

	private static <T> T readValue(String jsonStr, JavaType javaType) {
		try {
			return mapper.readValue(jsonStr, javaType);
		} catch (IOException e) {
			throw new IllegalArgumentException("parser JSON ", e);
		}
	}

	private static <T> T readValue(String jsonStr, TypeReference<?> valueTypeRef) {
		try {
			return mapper.readValue(jsonStr, valueTypeRef);
		} catch (IOException e) {
			throw new IllegalArgumentException("parser JSON ", e);
		}
	}
}
