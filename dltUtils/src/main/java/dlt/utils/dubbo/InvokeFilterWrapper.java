package dlt.utils.dubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


 /**
 * 自动包装扩展点的 Wrapper 类。
  * ExtensionLoader 在加载扩展点时，如果加载到的扩展点有拷贝构造函数，则判定为扩展点 Wrapper 类
 * Wrapper 类同样实现了扩展点接口，但是 Wrapper 不是扩展点的真正实现。
 * 它的用途主要是用于从 ExtensionLoader 返回扩展点时，包装在真正的扩展点实现外。
 * 即从 ExtensionLoader 中返回的实际上是 Wrapper 类的实例，Wrapper 持有了实际的扩展点实现类。
 扩展点的 Wrapper 类可以有多个，也可以根据需要新增。
 通过 Wrapper 类可以把所有扩展点公共逻辑移至 Wrapper 中。
 新加的 Wrapper 在所有的扩展点上添加了逻辑，有些类似 AOP，即 Wrapper 代理了扩展点。
 * Created by denglt on 2017/5/22.
 */
@Activate
public class InvokeFilterWrapper implements Filter {

    private static Log log = LogFactory.getLog(InvokeFilterWrapper.class);

    public InvokeFilterWrapper(Filter filter) {
        log.info("new InvokeFilterWrapper:" + filter);
        this.filter = filter;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            log.info("into :" + filter);
            return filter.invoke(invoker, invocation);
        } finally {
            log.info("out :" + filter);
        }
    }

    private Filter filter;
}
