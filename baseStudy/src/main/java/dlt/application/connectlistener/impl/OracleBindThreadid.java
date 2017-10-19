package dlt.application.connectlistener.impl;

import dlt.utils.proxy.connection.ConnectionListener;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class OracleBindThreadid extends ConnectionListener {

	@PostConstruct
	public void init(){
		
	}
	
	@PreDestroy
	public void destroy(){

	}

	@Override
	public void getConnection(Connection conn) {
		long id = Thread.currentThread().getId();
		String module = "JDBC(" + id + ")";
		setSessionModule(conn, module);

	}

	private void setSessionModule(Connection conn, String module) {
		String sql = "{call dbms_application_info.set_module(?,?)}";
		try {
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setString(1, module);
			cstmt.setString(2, module);
			cstmt.execute();
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Connection conn  = (Connection) invocation.getThis();
		setSessionModule(conn, "JDBC(idle)");
		return invocation.proceed();
	}

	@Override
	public boolean matches(String methodName) {
		return methodName.equals("close");
	}
}
