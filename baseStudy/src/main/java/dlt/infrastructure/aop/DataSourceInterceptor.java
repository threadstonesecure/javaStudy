package dlt.infrastructure.aop;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dlt.utils.proxy.connection.ConnectionListener;
import dlt.utils.proxy.connection.ProxyConnection;


@Service
public class DataSourceInterceptor implements MethodInterceptor {

	@Autowired(required = false)
	private List<ConnectionListener> connListeners = new ArrayList<ConnectionListener>();

	public List<ConnectionListener> getConnListeners() {
		return connListeners;
	}

	public void setConnListeners(List<ConnectionListener> connListeners) {
		this.connListeners = connListeners;
	}

	public void addConnListeners(ConnectionListener connListener) {
		if (!connListeners.contains(connListener)) {
			this.connListeners.add(connListener);
		}
	}

	@Override
	public Object invoke(MethodInvocation method) throws Throwable {
		Object retVal = null;
		retVal = method.proceed();
		Object target = method.getThis();// 目标对象
		if ((retVal instanceof Connection)  && (target instanceof DataSource)){
			return ProxyConnection.wrap((Connection) retVal, connListeners);

		}
		return retVal;

	}

}
