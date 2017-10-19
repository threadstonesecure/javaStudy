package dlt.utils.dubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 自动Wrap扩展点的Wrapper类
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
