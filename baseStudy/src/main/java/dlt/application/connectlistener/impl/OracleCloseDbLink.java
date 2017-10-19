package dlt.application.connectlistener.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Service;

import dlt.utils.proxy.connection.ConnectionListener;

@Service
public class OracleCloseDbLink extends ConnectionListener {
	private void closeDbLink(Connection conn) {
		String sql = "select db_link from v$dblink where in_transaction='NO'";
		try {
			Statement queryStatement = conn.createStatement();
			ResultSet resultSet = queryStatement.executeQuery(sql);
			String dblink = null;
			while (resultSet.next()) {
				dblink = resultSet.getString("db_link");

				if (!(dblink == null || dblink.isEmpty())) {
					sql = "alter session close database link " + dblink;
					Statement executeStatement = conn.createStatement();
					executeStatement.execute(sql);
					executeStatement.close();
				}
			}
			resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Connection conn  = (Connection) invocation.getThis();
		closeDbLink(conn);
		return invocation.proceed();
	}

	@Override
	public boolean matches(String methodName) {
		return methodName.equals("close");
	}
}
