package dlt.utils.proxy.connection;

import org.aopalliance.intercept.MethodInterceptor;

import java.sql.Connection;

public abstract class ConnectionListener  implements MethodInterceptor {

	public void getConnection(Connection conn){
		
	}

	public abstract boolean  matches(String methodName);
}
