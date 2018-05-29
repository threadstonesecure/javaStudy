package dlt.study.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Service;

import static dlt.utils.CommonUtils.log;

/**
 * Created by denglt on 2016/11/30.
 */
//@Service
public class MyAopMethodInterceptor2 implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("MyAop2 begin...");
        log.info("MethodInvocation2:" + invocation.getClass());
        log.info("Target Object 2: "+ invocation.getThis());
        Object result = invocation.proceed();
        log.info("MyAop2 end !");
        return result;
    }
}
