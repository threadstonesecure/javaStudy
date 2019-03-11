package com.yuntai.med.support.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.protocol.dubbo.FutureAdapter;
import com.alibaba.dubbo.rpc.support.RpcUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * 跟踪分布式dubbo服务的调用轨迹
 * Created by denglt on 2015/11/11.
 */
@Activate(group = {Constants.CONSUMER, Constants.PROVIDER})
public class TraceFilter implements Filter {

	private static Log log = LogFactory.getLog(TraceFilter.class);
	private static ThreadLocal<String> localTraceid = new ThreadLocal<>();
	private final String TRACE_KEY = "dubbo.traceid";

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation)
			throws RpcException {

		RpcContext rpcContext = RpcContext.getContext();
		String traceid = rpcContext.getAttachment(TRACE_KEY);
		if (traceid == null) {
			traceid = localTraceid.get();
			if (traceid == null) {
				traceid = UUID.randomUUID().toString().replace("-", "");
			}
			rpcContext.setAttachment(TRACE_KEY, traceid);
		}

		String interfaceName = invoker.getInterface().getName();
		String methodName = invocation.getMethodName();
		InetSocketAddress remoteAddress = rpcContext.getRemoteAddress();
		String remoteIpAndPort = remoteAddress.getHostString() + ":"
				+ remoteAddress.getPort();
		boolean isConsumer = rpcContext.isConsumerSide();
		if (isConsumer) {
			log.info(String.format("Traceid[%s]: calling [%s] %s.%s", traceid,
					remoteIpAndPort, interfaceName, methodName));
		} else {
			log.info(String.format("Traceid[%s]: %s.%s  is called by [%s]",
					traceid, interfaceName, methodName, remoteIpAndPort));
		}

		// TODO: 2019/3/6   如果是异步调用，需要特殊处理
		boolean isAsync = false ;//= RpcUtils.isAsync(invoker.getUrl(), invocation);
		// final boolean isOneway = RpcUtils.isOneway(invoker.getUrl(), invocation);
		Future<Object> future = rpcContext.getFuture();
		if (future != null && future instanceof FutureAdapter) {
			isAsync = true;
			((FutureAdapter) future).getFuture().setCallback(new AsyncTraceCallback(traceid,interfaceName,methodName)); // 这儿会冲掉已经设置了的Callback，需要改进
		}
		try {
			Result r = invoker.invoke(invocation);
			return r;
		} finally {
			if (isConsumer) {
				if (!isAsync)
					log.info(String.format("Traceid[%s]: calling %s.%s finish !",
							traceid, interfaceName, methodName));
			} else {
				log.info(String.format("Traceid[%s]: %s.%s is called finish !",
						traceid, interfaceName, methodName));
			}
		}

	}


	public static void setLocalTraceid(String value) {
		localTraceid.set(value);
	}

	private class AsyncTraceCallback implements ResponseCallback {

		private String traceid;
		private String interfaceName;
		private String methodName;

		public AsyncTraceCallback(String traceid, String interfaceName,String methodName) {
			this.traceid = traceid;
			this.interfaceName = interfaceName;
			this.methodName = methodName;
		}

		@Override
		public void done(Object o) {
			log.info(String.format("Traceid[%s]: calling %s.%s finish !",
					traceid, interfaceName, methodName));
		}

		@Override
		public void caught(Throwable throwable) {
			log.info(String.format("Traceid[%s]: calling %s.%s finish !",
					traceid, interfaceName, methodName));
		}
	}
}
