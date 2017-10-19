/**
 * 
 */
package dlt.utils.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.jdbc.Work;
import org.springframework.orm.hibernate4.HibernateCallback;


public interface HibernateService {

	/**
	 * @param obj
	 */
	void save(Serializable obj);

	void update(Serializable obj);
	
	void saveOrUpdate(Serializable obj);

	Integer delete(Class<?> class1, Serializable obj);

	void delete(Object obj);

	<T> T load(Class<T> class1, Serializable id);

	/**
	 * @param class1
	 * @return
	 */
	Long total(Class<?> class1);

	<T> T uniqueQuery(String hql, String[] paramNames, Object[] values);

	Integer execute(String hql, String[] paramNames, Object[] values);

	Integer executeNamed(String namedHql, String[] paramNames, Object[] values);

	/**
	 * @param class1
	 * @param start
	 * @param limit
	 * @return
	 */
	<T> List<T> loadPage(Class<T> class1, int start, int limit);

	/**
	 * @param namedHql
	 * @param paramNames
	 * @param values
	 * @return
	 */
	<T> List<T> query(String namedHql, String[] paramNames, Object[] values);

	<T> List<T> queryHQL(String hql, String[] paramNames, Object[] values);

	<T> List<T> queryHQL(String hql, Object... values);

	/**
	 * @param string
	 * @param paramNames
	 * @param values
	 * @return
	 */
	<T> T uniqueNamedQuery(String namedHql, String[] paramNames, Object[] values);

	<T> List<T> namedQuery(String namedHql, String[] paramNames, Object[] values);

	<T> List<T> namedQuery(String namedHql, String[] paramNames, Object[] values, int start, int limit);

	<T> List<T> queryHQL(String hql, String[] paramNames, Object[] values, int start, int limit);

	/**
	 * @param sql
	 * @param paramNames
	 * @param values
	 * @return
	 */
	<T> List<T> querySql(String sql, String[] paramNames, String[] values);

	/**
	 * @param sql
	 * @param paramNames
	 * @param values
	 * @param class1
	 * @param scalar 
	 * @return
	 */
	<T> List<T> querySql(String sql, String[] paramNames, String[] values, Class<?> class1, String[] scalar);

	/**
	 * @param sql
	 * @param paramNames
	 * @param values
	 * @param start
	 * @param limit
	 * @return
	 */
	<T> List<T> querySql(String sql, String[] paramNames, String[] values, int start, int limit);

	<T> List<T> querySql(String sql, String[] paramNames, String[] values, Class<?> class1, String[] scalar, int start, int limit);
	
	/**
	 * 
	 * @param sql
	 * @param paramNames
	 * @param values
	 * @return
	 */
	<T> T uniqueSqlQuery(final String sql, final String[] paramNames, final String[] values);

	<T> T executeWithNativeSession(HibernateCallback<T> action);

	/**
	 * jdbc直接调用
	 * @param work
	 * @throws HibernateException
	 */
	public void doWork(Work work) throws HibernateException;
}
