package dlt.study.spring.aop;

import dlt.study.spring.TaskService;
import org.junit.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.util.ClassUtils;

/**
 * Created by denglt on 2016/11/30.
 */
public class CreateAopDemo {

    @Test
    public void proxyFactory(){
        TaskService bean = new TaskService();
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.setExposeProxy(true);
        proxyFactory.setProxyTargetClass(true);
        //proxyFactory.setFrozen(true);
        Advisor advisor = new MyAopAdvisor();
        proxyFactory.addAdvisor(advisor);
        Object aopBean = proxyFactory.getProxy(ClassUtils.getDefaultClassLoader());
        Object aopBean2 = proxyFactory.getProxy(ClassUtils.getDefaultClassLoader());
        System.out.println(aopBean.getClass());
        System.out.println(aopBean ==  aopBean2);
        if (aopBean instanceof TaskService){
            ((TaskService) aopBean).doTask();
            ((TaskService) aopBean).doTaskNoAsync();
        }

    }

    @Test
    public void proxyFactoryBean(){
        TaskService bean = new TaskService("mytask");
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setSingleton(false);
        Advisor advisor = new MyAopAdvisor();
        proxyFactoryBean.addAdvisor(advisor);
        //proxyFactoryBean.setTargetClass(TaskService.class);
        proxyFactoryBean.setTarget(bean);
       // proxyFactoryBean.setInterceptorNames();
       // proxyFactoryBean.setInterfaces();
        Object aopBean = proxyFactoryBean.getObject();
        System.out.println(aopBean.getClass());
        Object aopBean2 = proxyFactoryBean.getObject();
        System.out.println(aopBean ==  aopBean2);
        if (aopBean instanceof TaskService){
            ((TaskService) aopBean).doTask();
        }
        if (aopBean2 instanceof TaskService){
            ((TaskService) aopBean2).doTask();
        }
    }

    @Test
    public void aspectJProxyFactory(){
        TaskService bean = new TaskService("mytask");

    }
}
