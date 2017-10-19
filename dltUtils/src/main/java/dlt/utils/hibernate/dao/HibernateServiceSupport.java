/**
 * 
 */
package dlt.utils.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;


@Service
public class HibernateServiceSupport extends HibernateTemplate implements HibernateService {

	@Autowired
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		
		super.setSessionFactory(sessionFactory);
	}
	
	
	@Override
	public void save(Serializable obj) {
		super.save(obj);
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#updateEntity(java.io.Serializable)
	 */
	@Override
	public void update(Serializable obj) {
		super.update(obj);
		
	}

	@Override
	public void saveOrUpdate(Serializable obj) {
		super.saveOrUpdate(obj);

	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#delete(java.lang.Class, java.io.Serializable)
	 */
	public Integer delete(final Class<?> class1, final Serializable id) {
		return executeWithNativeSession(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery("delete " + class1.getName() + " where id=:id");
				query.setParameter("id", id);
				return query.executeUpdate();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#load(java.io.Serializable)
	 */
	public <T> T load(Class<T> class1, Serializable id) {
		T obj = super.load(class1, id);
		if (!Hibernate.isInitialized(obj))
			Hibernate.initialize(obj);
		return obj;
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#total(java.lang.Class)
	 */
	public Long total(Class<?> class1) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(class1);
		criteria.setProjection(Projections.rowCount());

		return executeWithNativeSession(new HibernateCallback<Long>() {
			public Long doInHibernate(Session session) throws HibernateException {
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				return (Long) executableCriteria.uniqueResult();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#loadPage(java.lang.Class, int, int)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> loadPage(Class<T> class1, int start, int limit) {
		DetachedCriteria criteria = DetachedCriteria.forClass(class1);
		return (List<T>) findByCriteria(criteria, start, limit);
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#query(java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> query(String namedHql, String[] paramNames, Object[] values) {
		return (List<T>) findByNamedQueryAndNamedParam(namedHql, paramNames, values);

	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#queryHQL(java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryHQL(String hql, String[] paramNames, Object[] values) {
		return (List<T>) findByNamedParam(hql, paramNames, values);
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#queryHQL(java.lang.String, java.lang.String[], java.lang.Object[], int, int)
	 */
	public <T> List<T> queryHQL(final String hql, final String[] paramNames, final Object[] values, final int start, final int limit) {
		return executeWithNativeSession(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException {
				Query queryObject = getQuery(hql, paramNames, values, session);
				setPageInfo(start, limit, queryObject);
				return queryObject.list();
			}

		});
	}

	/**
	 * @param start
	 * @param limit
	 * @param queryObject
	 */
	private void setPageInfo(final int start, final int limit, Query queryObject) {
		if (start >= 0) {
			queryObject.setFirstResult(start);
		}
		if (limit > 0) {
			queryObject.setMaxResults(limit);
		}
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#queryHQL(java.lang.String, java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryHQL(String hql, Object... values) {
		return (List<T>) find(hql, values);
	}

	/**
	 * @param paramNames
	 * @param values
	 * @param query
	 */
	private void prepareQuery(final String[] paramNames, final Object[] values, Query query) {
		prepareQuery(query);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				applyNamedParameterToQuery(query, paramNames[i], values[i]);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#namedQuery(java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	@Override
	public <T> T uniqueNamedQuery(final String namedHql, final String[] paramNames, final Object[] values) {
		return executeWithNativeSession(new HibernateCallback<T>() {
			@SuppressWarnings("unchecked")
			public T doInHibernate(Session session) throws HibernateException {
				Query query = getNamedQuery(namedHql, paramNames, values, session);
				return (T) query.uniqueResult();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#namedQuery(java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	@Override
	public <T> List<T> namedQuery(final String namedHql, final String[] paramNames, final Object[] values) {
		return executeWithNativeSession(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			public List<T> doInHibernate(Session session) throws HibernateException {
				Query query = getNamedQuery(namedHql, paramNames, values, session);
				return (List<T>) query.list();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#namedQuery(java.lang.String, java.lang.String[], java.lang.Object[], int, int)
	 */
	@Override
	public <T> List<T> namedQuery(final String namedHql, final String[] paramNames, final Object[] values, final int start, final int limit) {
		return executeWithNativeSession(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException {
				Query queryObject = getNamedQuery(namedHql, paramNames, values, session);
				setPageInfo(start, limit, queryObject);
				return queryObject.list();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#total(java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	@Override
	public <T> T uniqueQuery(final String hql, final String[] paramNames, final Object[] values) {
		return executeWithNativeSession(new HibernateCallback<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T doInHibernate(Session session) throws HibernateException {
				Query queryObject = getQuery(hql, paramNames, values, session);
				return (T) queryObject.uniqueResult();
			}
		});

	}

	/**
	 * @param hql
	 * @param paramNames
	 * @param values
	 * @param session
	 * @return
	 */
	private Query getQuery(final String hql, final String[] paramNames, final Object[] values, Session session) {
		Query queryObject = session.createQuery(hql);
		prepareQuery(paramNames, values, queryObject);
		return queryObject;
	}

	/**
	 * @param namedHql
	 * @param paramNames
	 * @param values
	 * @param session
	 * @return
	 */
	private Query getNamedQuery(final String namedHql, final String[] paramNames, final Object[] values, Session session) {
		Query queryObject = session.getNamedQuery(namedHql);
		prepareQuery(paramNames, values, queryObject);
		return queryObject;
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#execute(java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	@Override
	public Integer execute(final String hql, final String[] paramNames, final Object[] values) {
		return executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = getQuery(hql, paramNames, values, session);
				return query.executeUpdate();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#executeNamed(java.lang.String, java.lang.String[], java.lang.Object[])
	 */
	@Override
	public Integer executeNamed(final String namedHql, final String[] paramNames, final Object[] values) {
		return executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = getNamedQuery(namedHql, paramNames, values, session);
				return query.executeUpdate();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#querySql(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public <T> List<T> querySql(final String sql, final String[] paramNames, final String[] values) {
		return querySql(sql, paramNames, values, null, null);
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#querySql(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public <T> List<T> querySql(final String sql, final String[] paramNames, final String[] values, int start, int limit) {
		return querySql(sql, paramNames, values, null, null, start, limit);
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#querySql(java.lang.String, java.lang.String[], java.lang.String[], java.lang.Class)
	 */
	@Override
	public <T> List<T> querySql(final String sql, final String[] paramNames, final String[] values, final Class<?> class1, final String[] scalar) {
		return executeWithNativeSession(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				prepareQuery(paramNames, values, queryObject);
				setScalar(class1, scalar, queryObject);
				return queryObject.list();
			}

		});
	}

	@Override
	public <T> List<T> querySql(final String sql, final String[] paramNames, final String[] values, final Class<?> class1, final String[] scalar,
			final int start, final int limit) {
		return executeWithNativeSession(new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				prepareQuery(paramNames, values, queryObject);
				setScalar(class1, scalar, queryObject);
				setPageInfo(start, limit, queryObject);
				return queryObject.list();
			}


		});
	}

	/**
	 * @param class1
	 * @param scalar
	 * @param queryObject
	 */
	private void setScalar(final Class<?> class1, final String[] scalar, SQLQuery queryObject) {
		if (class1 != null) {
			for (String sc : scalar) {
				queryObject.addScalar(sc);
			}
			queryObject.setResultTransformer(new AliasToBeanResultTransformer(class1));
		}
	}
	
	/**
	 * 
	 * @param sql
	 * @return
	 */
	public <T> T uniqueSqlQuery(final String sql,final String[] paramNames, final String[] values) {
		return executeWithNativeSession(new HibernateCallback<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T doInHibernate(Session session) throws HibernateException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				prepareQuery(paramNames, values, queryObject);
				return (T)queryObject.uniqueResult();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.gpdi.emoss.domain.service.HibernateService#doWork(org.hibernate.jdbc.Work)
	 */
	@Override
	public void doWork(final Work work) throws HibernateException {
		executeWithNativeSession(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				session.doWork(work);
				return null;
			}
		});

	}


	

}
