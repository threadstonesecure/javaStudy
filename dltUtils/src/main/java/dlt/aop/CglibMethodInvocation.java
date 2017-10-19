package dlt.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by denglt on 2016/12/6.
 */
public class CglibMethodInvocation extends ReflectiveMethodInvocation {

    private final MethodProxy methodProxy;

    public CglibMethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
                                 List<MethodInterceptor> interceptors, MethodProxy methodProxy) {
        super(proxy, target, method, arguments, interceptors);
        this.methodProxy = methodProxy;
    }


    @Override
    protected Object invokeJoinpoint() throws Throwable {
        return target == null ? methodProxy.invokeSuper(proxy, arguments) : methodProxy.invoke(target, arguments);
    }
}
