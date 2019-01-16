package dlt.study.spring.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;

import static dlt.utils.CommonUtils.log;

/**
 * @Description:
 * @Package: dlt.study.spring.aop
 * @Author: denglt
 * @Date: 2019/1/11 4:11 PM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class MyIntroductionInterceptor implements IntroductionInterceptor, INewAction {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        //log.info("MethodInvocation in MyIntroductionInterceptor:" + methodInvocation.getClass());
        // log.info("Target Object in MyIntroductionInterceptor: "+ methodInvocation.getThis());

        System.out.println(methodInvocation.getMethod().getDeclaringClass());
        if (implementsInterface(methodInvocation.getMethod().getDeclaringClass())){
            return methodInvocation.getMethod().invoke(this, methodInvocation.getArguments());
        }else{
            log.info("我的增强前动作!");
            Object result = methodInvocation.proceed();
            log.info("我的增强后动作!");
            return result;
        }

    }

    @Override
    public void doNewAction() {
        log.info("do new action！我有了新功能");
    }

    @Override
    public boolean implementsInterface(Class<?> aClass) {
        return aClass.isAssignableFrom(INewAction.class);
    }
}
