package dlt.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by denglt on 2016/12/5.
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    protected Object proxy;
    protected Object target;
    protected Method method;
    protected Object[] arguments;
    protected List<MethodInterceptor> interceptors;
    private int currentInterceptorIndex = -1;

    public ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, List<MethodInterceptor> interceptors) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.interceptors = interceptors;

    }

    public void add(MethodInterceptor methodInterceptor){
        interceptors.add(methodInterceptor);
    }
    @Override
    public Object proceed() throws Throwable {
        if (this.currentInterceptorIndex == this.interceptors.size() - 1) {
            return invokeJoinpoint();
        }
        MethodInterceptor interceptor = this.interceptors.get(++this.currentInterceptorIndex);
        return interceptor.invoke(this);
    }

    protected Object invokeJoinpoint() throws Throwable{
        return method.invoke(target, arguments);
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }
}
