package dlt.study.spring.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

import static dlt.utils.CommonUtils.log;

/**
 * Created by denglt on 2016/12/6.
 */
public class MyAopMethodInterceptor2 implements MethodInterceptor{
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("MyAop begin... by MyAopMethodInterceptor2" );
        Object result = methodProxy.invokeSuper(obj, args);
        log.info("MyAop end !");
        return result;
    }
}
