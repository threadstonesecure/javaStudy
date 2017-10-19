package dlt.study.spring.aop;

import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.stereotype.Service;

/**
 * Created by denglt on 2016/11/30.
 */

//@Service
public class MyAopBeanPostProcessor extends AbstractAdvisingBeanPostProcessor {

    public MyAopBeanPostProcessor(){
        this.advisor = new MyAopAdvisor();
    }
}
