package dlt.utils.dubbo.cache;

import com.alibaba.dubbo.config.spring.extension.SpringExtensionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * Created by denglt on 2017/5/8.
 */
public class SpringRedisHelper {



    public static RedisConnectionFactory getRedisConnectionFactory(String beanName) {

        if (beanName != null && beanName.length() > 0) {
            RedisConnectionFactory rcf = getBean(beanName);
            return rcf != null ? rcf : firstConnectionFactory;
        }
        return firstConnectionFactory;
    }

    private static <T> T getBean(String beanName) {

        for (ApplicationContext ac : acs) {
            try {
                return (T) ac.getBean(beanName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    private static Set<ApplicationContext> acs;

    private static RedisConnectionFactory firstConnectionFactory;

    static {
        try {
            Field f = SpringExtensionFactory.class.getDeclaredField("contexts");
            f.setAccessible(true);
            acs = (Set<ApplicationContext>) f.get(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            assert false : "no found spring ApplicationContext";
        }

        for (ApplicationContext ac : acs) {
            Map<String, RedisConnectionFactory> rcfs = ac.getBeansOfType(RedisConnectionFactory.class);
            if (rcfs != null && rcfs.size() > 0) {
                Set<Map.Entry<String, RedisConnectionFactory>> entries = rcfs.entrySet();
                firstConnectionFactory = entries.iterator().next().getValue();
                break;
            }
        }

    }

}
