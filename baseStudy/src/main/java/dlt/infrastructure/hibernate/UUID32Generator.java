/**
 * 
 */
package dlt.infrastructure.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.UUIDGenerator;


public class UUID32Generator extends UUIDGenerator {

	/* (non-Javadoc)
	 * @see org.hibernate.id.UUIDGenerator#generate(org.hibernate.engine.spi.SessionImplementor, java.lang.Object)
	 */
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		Serializable result = super.generate(session, object);
		if (result instanceof String) {
			result = StringUtils.remove((String) result, '-');
		}
		return result;
	}
}
