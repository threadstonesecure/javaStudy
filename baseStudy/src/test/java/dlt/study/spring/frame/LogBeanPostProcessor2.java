package dlt.study.spring.frame;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import static dlt.utils.CommonUtils.log;

/**
 * Created by denglt on 2016/11/25.
 */
//@Service
public class LogBeanPostProcessor2 implements BeanPostProcessor, Ordered {

    private int order = Ordered.LOWEST_PRECEDENCE;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("postProcessBeforeInitialization:" + beanName + ":" + bean);
        log.info(bean.getClass());
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("postProcessAfterInitialization:" + beanName + ":" + bean);
        return bean;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
