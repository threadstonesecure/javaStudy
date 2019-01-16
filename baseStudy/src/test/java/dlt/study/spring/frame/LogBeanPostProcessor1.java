package dlt.study.spring.frame;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;

import static dlt.utils.CommonUtils.log;
/**
 create bean 过程中BeanPostProcessor执行的顺序：

 bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
 if (bean != null) {
 bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
 return bean；
 }
 doCreateBean过程{
     createBeanInstance;// 创建Bean实例
     applyMergedBeanDefinitionPostProcessors;
     populateBean; //Bean 属性赋值、autowire ，调用1、InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation
     applyBeanPostProcessorsBeforeInitialization();
     invokeInitMethods(); // Bean上定义的初始化方法(InitializingBean、@PostConstruct)
     applyBeanPostProcessorsAfterInitialization();
 }

 * Created by denglt on 2016/11/25.
 */


@Service
public class LogBeanPostProcessor1 implements InstantiationAwareBeanPostProcessor, Ordered ,BeanFactoryAware{

    private BeanFactory beanFactory ;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private int order = 1;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try {
            log.info("postProcessBeforeInitialization:" + beanName + ":" + bean);
        }catch (Exception e){
            // bean.toString() 可能发生错误
        }
        log.info(bean.getClass());
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("postProcessAfterInitialization:" + beanName + ":" + bean);
        return bean;
    }


    /** 创建Bean前调用
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        log.info("postProcessBeforeInstantiation:" + beanClass.getClass() +":"+ beanName );
        return null;
    }

    /** Bean创建完成调用 **/
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        try {
            log.info("postProcessAfterInstantiation:" + bean + ":" + beanName);
        }catch (Exception  e){
            // bean.toString() 可能发生错误
        }
        return true;
    }


    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
