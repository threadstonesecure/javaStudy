package dlt.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import static dlt.utils.CommonUtils.log;

/**
 * Created by denglt on 2016/11/30.
 */
public class MyAopMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("MyAop begin...");
        Object result = invocation.proceed();
        log.info("MyAop end !");
        return result;
    }
}
