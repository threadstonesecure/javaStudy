package dlt.study.plugins;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.springframework.stereotype.Service;

@Service
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class StatementHandlerPlugin implements Interceptor {
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("进入拦截者 ：" + this);
		System.out.println("拦截了:" + invocation.getTarget().getClass() + ":"
				+ invocation.getMethod());
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();
		MetaObject metaStatementHandler = SystemMetaObject
				.forObject(statementHandler);
		// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
		// 可以分离出最原始的的目标类)
		while (metaStatementHandler.hasGetter("h")) {
			Object object = metaStatementHandler.getValue("h");
			metaStatementHandler = SystemMetaObject.forObject(object);
		}
		// 分离最后一个代理对象的目标类
		while (metaStatementHandler.hasGetter("target")) {
			Object object = metaStatementHandler.getValue("target");
			metaStatementHandler = SystemMetaObject.forObject(object);
		}

		System.out.println("原始对象："
				+ metaStatementHandler.getOriginalObject().getClass());

		RoutingStatementHandler rsh = (RoutingStatementHandler) metaStatementHandler
				.getOriginalObject();
		BoundSql bs = rsh.getBoundSql();
		System.out.println("sql:" + bs.getSql());
		System.out.println("ParameterObject:" + bs.getParameterObject() + ":"
				+ bs.getParameterObject().getClass());
		ParameterHandler ph = rsh.getParameterHandler();

		Configuration configuration = (Configuration) metaStatementHandler
				.getValue("delegate.configuration");

		Object object = invocation.proceed();
		System.out.println("退出 拦截者 ：" + this);
		return object;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub

	}

}
