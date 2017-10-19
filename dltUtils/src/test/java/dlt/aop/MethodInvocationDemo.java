package dlt.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by denglt on 2016/12/6.
 */
public class MethodInvocationDemo {

    @Test
    public void reflectiveMethodInvocation() throws Throwable {
        TaskService taskService = new TaskService("denglt");
        Method method = TaskService.class.getMethod("doTask");
        MyAopMethodInterceptor interceptor = new MyAopMethodInterceptor();
        MyAopMethodInterceptor2 interceptor2 = new MyAopMethodInterceptor2();
        List<MethodInterceptor> interceptors = new ArrayList<>();
        interceptors.add(interceptor);
        interceptors.add(interceptor2);
        MethodInvocation methodInvocation = new ReflectiveMethodInvocation(null, taskService, method, new Object[0], interceptors);
        methodInvocation.proceed();
    }

    @Test
    public void cglibMethodInvocation() {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/logs/cglib/class");
        TaskService taskService = (TaskService) Enhancer.create(TaskService.class, new org.springframework.cglib.proxy.MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                MyAopMethodInterceptor interceptor = new MyAopMethodInterceptor();
                MyAopMethodInterceptor2 interceptor2 = new MyAopMethodInterceptor2();
                List<MethodInterceptor> interceptors = new ArrayList<>();
                interceptors.add(interceptor);
                interceptors.add(interceptor2);
                MethodInvocation methodInvocation =  new  CglibMethodInvocation(obj,null,method,args,interceptors,methodProxy);
                return methodInvocation.proceed();
            }
        });
        taskService.doTask();
    }
}
