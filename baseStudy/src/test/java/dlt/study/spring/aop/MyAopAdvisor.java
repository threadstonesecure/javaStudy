package dlt.study.spring.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

/**
 * Created by denglt on 2016/11/30.
 */
@Service
public class MyAopAdvisor extends AbstractPointcutAdvisor {

    private Advice advice;

    private Pointcut pointcut;

    public MyAopAdvisor(){
        this.advice = buildAdvice();
        this.pointcut = buildPointcut(MyAop.class);
    }

    public MyAopAdvisor(Class<? extends Annotation> annotationType){
        this.advice = buildAdvice();
        this .pointcut = buildPointcut(annotationType);

    }
    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    private Advice buildAdvice(){
        return new MyAopMethodInterceptor();
    }

    private Pointcut buildPointcut(Class<? extends Annotation> annotationType){
        Pointcut cpc = new AnnotationMatchingPointcut(annotationType); // 类上
        Pointcut mpc = new AnnotationMatchingPointcut(null,annotationType); // 方法上
        return new ComposablePointcut(cpc).union(mpc); // or

    }
}
