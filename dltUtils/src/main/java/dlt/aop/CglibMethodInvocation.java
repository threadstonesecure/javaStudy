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

    /**
     *  这儿处理了 当target = null时，调用了proxy对象的方法，所以可以对方法中调用的方法也可以进行AOP。
     *  但Spring 中不是这样的，spring 最终调用的是target的方法，进入target后就无法进行aop了（这时需要ApplicationContext.getBean获取proxy）。
     *  可以看org.springframework.aop.framework.CglibMethodInvocation.invokeJoinpoint的方法
     * @return
     * @throws Throwable
     */
    @Override
    protected Object invokeJoinpoint() throws Throwable {
        return target == null ? methodProxy.invokeSuper(proxy, arguments) : methodProxy.invoke(target, arguments);
    }


  /*
    Spring的实现
    @Override
    protected Object invokeJoinpoint() throws Throwable {
        if (this.publicMethod) {
            return this.methodProxy.invoke(this.target, this.arguments);
        }
        else {
            return super.invokeJoinpoint();
        }
    }*/
}
