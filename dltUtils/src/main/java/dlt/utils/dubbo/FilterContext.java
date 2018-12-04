package dlt.utils.dubbo;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;

public class FilterContext {

    private static final ThreadLocal<FilterContext> LOCAL = ThreadLocal.withInitial(() -> new FilterContext());
    private Invoker<?> invoker;
    private Invocation invocation;

    private FilterContext(){

    }

    private Result _invoke(){
        return invoker.invoke(invocation);
    }

    public static FilterContext getContext() {
        return LOCAL.get();
    }

    public static void removeContext() {
        LOCAL.remove();
    }

    public static Result invoke(){
        return LOCAL.get()._invoke();
    }

    public Invoker<?> getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker<?> invoker) {
        this.invoker = invoker;
    }

    public Invocation getInvocation() {
        return invocation;
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }
}
