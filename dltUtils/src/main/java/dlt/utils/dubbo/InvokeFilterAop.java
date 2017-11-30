package dlt.utils.dubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 仅仅是个结合MethodInvocation的例子，还需要优化MethodInterceptor的注入和MethodInterceptor的过滤，
 * Created by denglt on 2017/5/22.
 */

@Activate
public class InvokeFilterAop implements Filter {
    private static Log log = LogFactory.getLog(InvokeFilterAop.class);

    private List<MethodInterceptor> interceptors;


    public void add(MethodInterceptor methodInterceptor) {
        synchronized (interceptors) {
            interceptors.add(methodInterceptor);
        }
    }

    private static InvokeFilterAop myself;

    static {
        ExtensionLoader<Filter> extensionLoader = ExtensionLoader.getExtensionLoader(Filter.class);
        String extensionName = extensionLoader.getExtensionName(InvokeFilterAop.class);
        if (extensionName != null) {
            myself = (InvokeFilterAop) extensionLoader.getExtension(extensionName);
        }
    }

    public static void addInterceptor(MethodInterceptor methodInterceptor) {
        if (myself != null) {
            myself.add(methodInterceptor);
        }
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            FilterContext context = FilterContext.getContext();
            context.setInvoker(invoker);
            context.setInvocation(invocation);
            return (Result) (new AopInvoker(invoker, invocation)).proceed();
        } catch (Throwable e) {
            throw new RpcException(e);
        } finally {
            FilterContext.removeContext();
        }
    }

    public interface FilterMethod {
        Invoker<?> getInvoker();
        Invocation getInvocation();
    }

    private class AopInvoker implements MethodInvocation, FilterMethod

    {
        private Invoker<?> invoker;
        private Invocation invocation;

        public AopInvoker(Invoker<?> invoker, Invocation invocation) {
            this.invoker = invoker;
            this.invocation = invocation;
        }

        private int currentInterceptorIndex = -1;

        @Override
        public Result proceed() throws Throwable {
            if (this.currentInterceptorIndex == interceptors.size() - 1) {
                return invokeJoinpoint();
            }
            MethodInterceptor interceptor = interceptors.get(++this.currentInterceptorIndex);
            return (Result) interceptor.invoke(this);
        }

        protected Result invokeJoinpoint() throws Throwable {
            return invoker.invoke(invocation);
        }

        @Override
        public Invoker<?> getInvoker() {
            return invoker;
        }

        @Override
        public Invocation getInvocation() {
            return invocation;
        }

        @Override
        public Method getMethod() {
            return null;
        }

        @Override
        public Object[] getArguments() {
            return invocation.getArguments();
        }

        @Override
        public Object getThis() {
            return this;
        }

        @Override
        public AccessibleObject getStaticPart() {
            return null;
        }
    }
}
