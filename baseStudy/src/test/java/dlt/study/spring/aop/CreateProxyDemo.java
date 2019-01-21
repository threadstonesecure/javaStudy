package dlt.study.spring.aop;

import dlt.study.spring.TaskService;
import org.aopalliance.aop.Advice;
import org.junit.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ClassUtils;

/**
 * 核心类：ProxyCreatorSupport （）
 * 该类 AopProxyFactory （实现类DefaultAopProxyFactory）
 * <p>
 * Created by denglt on 2016/11/30.
 */
public class CreateProxyDemo {

    @Test
    public void proxyFactory() {
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
        System.out.println(aopBean == aopBean2);
        if (aopBean instanceof Advised) {
            System.out.println("aopBean instanceof Advised"); // true
        }
        if (aopBean instanceof TaskService) {
            ((TaskService) aopBean).doTask();
            ((TaskService) aopBean).doTaskNoAsync();
        }
    }

    /**
     * 多次代理  like AbstractAdvisingBeanPostProcessor.postProcessAfterInitialization
     */
    @Test
    public void mutiAdvisor() {
        TaskService bean = new TaskService();
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.setExposeProxy(true);
        proxyFactory.setProxyTargetClass(true);
        //proxyFactory.setFrozen(true);
        Advisor advisor = new MyAopAdvisor();
        proxyFactory.addAdvisor(advisor);
        Object aopBean = proxyFactory.getProxy(ClassUtils.getDefaultClassLoader());
        if (aopBean instanceof Advised) { // 生成的proxy对象实现了Advised，可以通过Advised.addAdvisor来增加新的Advisor，这样就解决了多次proxy。
            Advised advised = (Advised) aopBean;
            Advisor asyncAdvisor = new MyAopAdvisor(Async.class); // TaskService的方法上加上或去掉@Asysnc，看看结果有什么不同
            // boolean canApply =  AopUtils.canApply(asyncAdvisor, TaskService.class);
            advised.addAdvisor(asyncAdvisor); // 代理对象，再增加Advisor
        }

        if (aopBean instanceof TaskService) {
            ((TaskService) aopBean).doTask();
        }


    }

    @Test
    public void proxyFactoryBean() {
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
        System.out.println(aopBean == aopBean2);

        if (aopBean instanceof TaskService) {
            ((TaskService) aopBean).doTask();
        }
        if (aopBean2 instanceof TaskService) {
            ((TaskService) aopBean2).doTask();
        }
    }

    @Test
    public void aspectJProxyFactory() {
        TaskService bean = new TaskService("mytask");

    }

    /**
     *  Spring retry 通过 IntroductionAdvisor 来实现的
     */

    @Test
    public void introductionAdvisor() {
        TaskService bean = new TaskService();
        Advice advice = new MyIntroductionInterceptor();
        DefaultIntroductionAdvisor introductionAdvisor = new DefaultIntroductionAdvisor(advice, () -> new Class[]{INewAction.class});
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvisor(introductionAdvisor);
        Advisor advisor = new MyAopAdvisor();
        proxyFactory.addAdvisor(advisor);
        Object proxy = proxyFactory.getProxy();
        if (proxy instanceof TaskService) {
            System.out.println("proxy instanceof TaskService");
            ((TaskService) proxy).doTask();
        }
        if (proxy instanceof INewAction) {
            System.out.println("proxy instanceof INewAction");
            ((INewAction) proxy).doNewAction();
        }
    }


}
