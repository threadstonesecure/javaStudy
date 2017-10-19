package dlt.study.spring.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

import static dlt.utils.CommonUtils.log;

/**
 * Created by denglt on 2016/12/5.
 */
public class MyAopMethodInterceptor implements MethodInterceptor {

    private Object target;

    public MyAopMethodInterceptor(){

    }

    public MyAopMethodInterceptor(Object target){
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("MyAop begin... by MyAopMethodInterceptor" );
        Object result = target == null ? methodProxy.invokeSuper(obj, args) : methodProxy.invoke(target,args);
        log.info("MyAop end !");
        return result;
    }
}
