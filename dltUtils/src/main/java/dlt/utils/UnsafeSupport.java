package dlt.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.Unsafe;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import java.lang.reflect.Field;

/**
 * SUN未开源的sun.misc.Unsafe的类，该类功能很强大。java的 concurrent.locks机制就是使用该类来实现的
 * 不能使用 Unsafe.getUnsafe() 获取 Unsafe，因为getUnsafe进行了ClassLoader校验
 *
 * @author dlt
 */
public class UnsafeSupport {
    private static Log log = LogFactory.getLog(UnsafeSupport.class);
    private static Unsafe unsafe;

    static {
        Field field;
        try {

            // 由反编译Unsafe类获得的信息
            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            // 获取静态属性,Unsafe在启动JVM时随rt.jar装载
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            unsafe = null;
            log.error("Get Unsafe instance occur error", e);
        }
    }

    /**
     * 获取{@link sun.misc.Unsafe }
     */
    public static Unsafe getInstance() {
        return unsafe;
    }

}
