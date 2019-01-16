package dlt.study.spring.aop;

import dlt.study.spring.ITask;
import dlt.study.spring.SubTaskService;
import dlt.study.spring.TaskService;
import dlt.study.spring.cglib.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.*;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by denglt on 2016/11/30.
 */
public class AopUtilsDemo {

    @Before
    public void init(){
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/tmp/logs/cglib/class");
    }

    /**
     * 检查Advisor、Class是否可以AOP(AopUtils.canApply)
     * 注意： AopUtils.canApply 与 DefaultAdvisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice 不一致
     */
    @Test
    public void canApply() throws Exception {
        Advisor advisor = new MyAopAdvisor() ;
        boolean canApply =  AopUtils.canApply(advisor, TaskService.class);
        System.out.println(canApply);
        canApply =  AopUtils.canApply(advisor, SubTaskService.class);
        System.out.println(canApply);
        DefaultAdvisorChainFactory defaultAdvisorChainFactory = new DefaultAdvisorChainFactory();
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.addAdvisor(advisor);
        Method doTask = TaskService.class.getDeclaredMethod("doTask"); // TaskService.class.getDeclaredMethod("doTask");
        List<Object> interceptorsAndDynamicInterceptionAdvice =
                defaultAdvisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(advisedSupport,doTask,TaskService.class);
        System.out.println(interceptorsAndDynamicInterceptionAdvice.size()); //0

       // advisedSupport.getInterceptorsAndDynamicInterceptionAdvice(doTask,TaskService.class)
    }

    @Test
    public void canApplyOnCglib() throws Exception {
        Advisor advisor = new MyAopAdvisor();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TaskService.class);
        TaskService target = new TaskService("denglt");
        dlt.study.spring.cglib.MyAopMethodInterceptor methodInterceptor = new dlt.study.spring.cglib.MyAopMethodInterceptor(target); // 设置目标对象
        enhancer.setCallback(methodInterceptor);
        Object aopProxy = enhancer.create();
        System.out.println(aopProxy.getClass());
        boolean canApply =  AopUtils.canApply(advisor, aopProxy.getClass());
        System.out.println(canApply);// false
        DefaultAdvisorChainFactory defaultAdvisorChainFactory = new DefaultAdvisorChainFactory();
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.addAdvisor(advisor);
        Method doTask = ITask.class.getDeclaredMethod("doTask");
        List<Object> interceptorsAndDynamicInterceptionAdvice =
                defaultAdvisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(advisedSupport,doTask,TaskService.class);
        System.out.println(interceptorsAndDynamicInterceptionAdvice.size()); //0
    }

    /**
     * 检查PointCut、Class是否可以AOP(AopUtils.canApply)
     */
    @Test
    public void canApply2(){
        Pointcut cpc = new AnnotationMatchingPointcut(MyAop.class);
        boolean canApply =  AopUtils.canApply(cpc, TaskService.class);
        System.out.println(canApply);
        Pointcut mpc = new AnnotationMatchingPointcut(null,MyAop.class);
        canApply =  AopUtils.canApply(mpc, TaskService.class);
        System.out.println(canApply);

        Pointcut composablePointcut = new ComposablePointcut(cpc).union(mpc);
        canApply =  AopUtils.canApply(composablePointcut, TaskService.class);
        System.out.println(canApply);
    }


    /**
     * 测试方法匹配
     * @throws Exception
     */
    @Test
    public void methodMatcher() throws Exception {
        MethodMatcher methodMatcher = new AnnotationMethodMatcher(MyAop.class);
        Method method = TaskService.class.getMethod("doTask");
        boolean matche =  methodMatcher.matches(method,TaskService.class);
        Method resolvedMethod = ClassUtils.getMostSpecificMethod(method, TaskService.class);
        System.out.println(matche);
        System.out.println(method == resolvedMethod);
    }

    /**
     * Determine a list of org.aopalliance.intercept.MethodInterceptor objects
     * for the given advisor chain configuration
     */
    @Test
    public void advisorChainFactory() throws Exception {
        TaskService bean = new TaskService();
        DefaultAdvisorChainFactory advisorChainFactory =  new DefaultAdvisorChainFactory();
        AdvisedSupport advised =  new AdvisedSupport();
        Advisor advisor = new MyAopAdvisor();
        advised.addAdvisor(advisor);
        //advised.setTarget(bean);
        Method method = TaskService.class.getMethod("doTask");
        List<Object> advisorChains = advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(advised,method,TaskService.class);
        for(Object chain :advisorChains){
            System.out.println(chain+":"+chain.getClass()); //dlt.study.spring.aop.MyAopMethodInterceptor@1a407d53:class dlt.study.spring.aop.MyAopMethodInterceptor
        }
    }

    /**
     *  使用AopProxyFactory的默认实现DefaultAopProxyFactory创建代理对象
     */
    @Test
    public void creatAopByAopProxyFactory(){
        TaskService bean = new TaskService();
        DefaultAopProxyFactory aopProxyFactory = new DefaultAopProxyFactory();
        AdvisedSupport advised = new AdvisedSupport();
        advised.setExposeProxy(true);
        Advisor advisor = new MyAopAdvisor();
        //Advisor advisor1 = new MyAopAdvisor();
        advised.addAdvisor(advisor);
    //    advised.addAdvisor(advisor1);
        advised.setTarget(bean); // 设置目标对象，其实Cglib是可以不需要target对象的，见dlt.aop。MethodInvocationDemo
       // advised.setInterfaces(ITask.class);  // 将使用JDK动态代理
        AopProxy aopProxy =  aopProxyFactory.createAopProxy(advised);
        Object aopBean = aopProxy.getProxy();
        if (aopBean instanceof ITask){
            ((ITask) aopBean).doTask();

        }
    }

    /**
     * 使用ProxyFactory创建代理对象（ProxyCreatorSupport里面封装DefaultAopProxyFactory）
     */
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
            //((TaskService) aopBean).doTaskNoAsync();
        }

    }

    /**
     * 使用ProxyFactoryBean创建代理对象
     */
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

    /**
     * 使用AspectJProxyFactory创建代理对象
     */
    @Test
    public void aspectJProxyFactory(){
        // TODO: 2016/12/5
    }
}
