package dlt.study.guava.reflect;

import com.google.common.reflect.Reflection;
import dlt.domain.model.User;
import dlt.utils.proxy.connection.ProxyConnection;
import org.junit.Test;

import java.sql.Connection;

public class NewProxyDemo {


    @Test
    public void t() {
        //Connection connection = null;
        ProxyConnection proxyConnection = new ProxyConnection(null, null);  // 仅仅是样列
        Connection connection = Reflection.newProxy(Connection.class, proxyConnection);
    }

    /**
     * AbstractInvocationHandler
     * <p>
     * Sometimes you may want your dynamic proxy to support equals(), hashCode() and toString() in the intuitive way, that is:
     * <p>
     * A proxy instance is equal to another proxy instance if they are for the same interface types and have equal invocation handlers.
     * A proxy's toString() delegates to the invocation handler's toString() for easier customization.
     * AbstractInvocationHandler implements this logic.
     */


    @Test
    public void initialize() {
        Reflection.initialize(User.class);
    }
}
