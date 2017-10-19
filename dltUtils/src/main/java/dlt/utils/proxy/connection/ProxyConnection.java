package dlt.utils.proxy.connection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import dlt.aop.ReflectiveMethodInvocation;
import dlt.utils.ClassHierarchyUtil;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author dlt
 */
public class ProxyConnection implements InvocationHandler {
    private Connection conn = null;
    private List<ConnectionListener> connListeners = null;

    public ProxyConnection(Connection conn, List<ConnectionListener> connListeners) {
        this.conn = conn;
        this.connListeners = connListeners;
    }


    public static Connection wrap(Connection conn,
                                  List<ConnectionListener> connListeners) {
        // 返回数据库连接conn的接管类，以便截住close方法
        Connection proxyConn = null;

        Class<?>[] intfs = ClassHierarchyUtil.getAllInterface(conn.getClass());

        Object proxy = Proxy.newProxyInstance(conn.getClass().getClassLoader(),
                intfs, new ProxyConnection(conn, connListeners));  //  proxyClass = Proxy.getProxyClass() ;proxyClass.getConstructor()
        proxyConn = (Connection) proxy;

        if (connListeners != null) {
            for (ConnectionListener l : connListeners) {
                l.getConnection(conn);
            }
        }

        return proxyConn;
    }

    public Object invoke(Object proxy, Method m, Object[] args)
            throws Throwable {
        Object retVal ;
        List<MethodInterceptor> interceptors = getInterceptors(m.getName());
        if (interceptors.size() > 0) {
            ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(proxy,conn,m,args,interceptors);
            retVal = methodInvocation.proceed();
        } else
            retVal = m.invoke(conn, args);
        return retVal;
    }


    private List<MethodInterceptor> getInterceptors(String methodName) {
        if (this.connListeners == null) return null;
        List<MethodInterceptor> interceptors = new ArrayList<>();
        for (ConnectionListener connectionListener : this.connListeners) {
            if (connectionListener.matches(methodName)) {
                interceptors.add(connectionListener);
            }
        }
        return interceptors;
    }
}
