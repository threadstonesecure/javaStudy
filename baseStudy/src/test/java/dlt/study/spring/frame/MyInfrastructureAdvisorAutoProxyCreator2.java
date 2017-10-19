package dlt.study.spring.frame;

import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
import org.springframework.stereotype.Service;

/**
 * Created by denglt on 2016/12/13.
 */

//@Service
public class MyInfrastructureAdvisorAutoProxyCreator2 extends InfrastructureAdvisorAutoProxyCreator {

    public MyInfrastructureAdvisorAutoProxyCreator2() {
        this.setProxyTargetClass(true);
    }

    @Override
    protected boolean isEligibleAdvisorBean(String beanName) {
        return true;
    }
}
