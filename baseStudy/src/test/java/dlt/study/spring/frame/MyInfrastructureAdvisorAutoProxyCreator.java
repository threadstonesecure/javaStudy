package dlt.study.spring.frame;

import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
import org.springframework.stereotype.Service;

/**
 * Created by denglt on 2016/12/13.
 */

//@Service
public class MyInfrastructureAdvisorAutoProxyCreator extends InfrastructureAdvisorAutoProxyCreator {

    public MyInfrastructureAdvisorAutoProxyCreator() {
        this.setProxyTargetClass(true);
    }

    @Override  // 注意supper中的实现
    protected boolean isEligibleAdvisorBean(String beanName) {
        return true;
    }
}
