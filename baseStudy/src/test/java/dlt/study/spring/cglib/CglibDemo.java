package dlt.study.spring.cglib;

import dlt.utils.ClassHierarchyUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * Created by denglt on 2016/12/5.
 */
public class CglibDemo {

    @Before
    public void init(){
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/logs/cglib/class");
    }

    /**
     * 加强类功能，直接根据Superclass创建出加强后的instance
     */
    @Test
    public void enhancer(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TaskService.class);
        enhancer.setCallbackFilter(new CallbackFilter() {
            @Override
            public int accept(Method method) {
                if (method.getName().equals("toString") ||
                        method.getName().equals("hashCode"))
                    return 2;
                if (method.getName().equals("doTaskNoAsync") )
                    return 1;
                return 0;  // 指定Callback的index
            }
        });
        TaskService target = new TaskService("denglt");
        MyAopMethodInterceptor methodInterceptor = new MyAopMethodInterceptor(target); // 设置目标对象
        MyAopMethodInterceptor2 methodInterceptor2 = new MyAopMethodInterceptor2(); // 没有目标对象
        //enhancer.setCallback(methodInterceptor);
        enhancer.setCallbacks(new Callback[]{methodInterceptor,methodInterceptor2,NoOp.INSTANCE});
        Object aopProxy = enhancer.create();
        System.out.println(aopProxy.getClass());  // class dlt.study.spring.cglib.TaskService$$EnhancerByCGLIB$$cbfa526
        TaskService taskService = (TaskService) aopProxy;
        taskService.doTask();
        taskService.doTaskNoAsync();

        System.out.println(taskService instanceof Factory); //CGLib 实现拦截的方式就是生成被拦截类的子类，这个子类实现了 net.sf.cglib.proxy.Factory 接口,这个接口中有一个非常重要的方法 getCallbacks() ，通过这个方法我们可以得到所有的拦截器 。
    }

    /**
     * 测试延时加载
     */
    @Test
    public void layzLoad(){
        LazyBean loader=new LazyBean("denglt",40);
        PropertyBean lazyLoaderBean = loader.getPropertyBean();
        PropertyBean dispatcherBean = loader.getPropertyBeanDispatcher();
        System.out.println("第一次：");
        System.out.println(lazyLoaderBean);
        System.out.println(dispatcherBean);
        System.out.println("第二次：");
        System.out.println(lazyLoaderBean);
        System.out.println(dispatcherBean);
    }


    /**
     * 测试InterfaceMaker（会动态生成一个接口，该接口包含指定类定义的所有方法）
     */
    @Test
    public void interfaceMaker() {
        InterfaceMaker interfaceMaker = new InterfaceMaker();
        //抽取某个类的方法生成接口方法
        interfaceMaker.add(MethodInterceptor.class);
        interfaceMaker.add(org.aopalliance.intercept.MethodInterceptor.class);
        Class<?> targetInterface = interfaceMaker.create();
        System.out.println(targetInterface);
        ClassHierarchyUtil.printClass(targetInterface);
        Object object = Enhancer.create(Object.class, new Class[]{targetInterface}, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return null;
            }
        });
    }
}
