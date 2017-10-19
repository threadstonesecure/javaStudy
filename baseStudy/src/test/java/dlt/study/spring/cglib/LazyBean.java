package dlt.study.spring.cglib;

import org.springframework.cglib.proxy.Dispatcher;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.LazyLoader;
import static dlt.utils.CommonUtils.log;
/**
 * 测试两种延时：LazyLoader、Dispatcher
 * LazyLoader只在第一次访问延迟加载属性时触发代理类回调方法，而Dispatcher在每次访问延迟加载属性时都会触发代理类回调方法
 * Created by denglt on 2016/12/7.
 */
public class LazyBean {
    private String name;
    private int age;
    private PropertyBean propertyBean;
    private PropertyBean propertyBeanDispatcher;

    public LazyBean(String name, int age) {
        log.info("lazy bean init");
        this.name = name;
        this.age = age;
        this.propertyBean = createPropertyBean();
        this.propertyBeanDispatcher = createPropertyBeanDispatcher();
    }

    private PropertyBean createPropertyBean() {
        return (PropertyBean) Enhancer.create(PropertyBean.class,new ConcreteClassLazyLoader());
    }

    private PropertyBean createPropertyBeanDispatcher() {
        return (PropertyBean) Enhancer.create(PropertyBean.class,new ConcreteClassDispatcher());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public PropertyBean getPropertyBean() {
        return propertyBean;
    }

    public PropertyBean getPropertyBeanDispatcher() {
        return propertyBeanDispatcher;
    }
}

class PropertyBean {
    private String key;
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PropertyBean{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}

class ConcreteClassLazyLoader implements LazyLoader{
    @Override
    public Object loadObject() throws Exception {
        log.info("before lazyLoader...");
        PropertyBean propertyBean = new PropertyBean();
        propertyBean.setKey("denglt");
        propertyBean.setValue(new Object());
        log.info("after lazyLoader...");
        return propertyBean;
    }
}

class ConcreteClassDispatcher implements Dispatcher{
    @Override
    public Object loadObject() throws Exception {
        log.info("before Dispatcher...");
        PropertyBean propertyBean = new PropertyBean();
        propertyBean.setKey("zyy");
        propertyBean.setValue(new Object());
        log.info("after Dispatcher...");
        return propertyBean;
    }
}