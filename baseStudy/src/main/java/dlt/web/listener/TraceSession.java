package dlt.web.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TraceSession implements HttpSessionListener ,HttpSessionAttributeListener {

	Log log = LogFactory.getLog(TraceSession.class);
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session  =  se.getSession();
	
		String id = session.getId();
		
		log.info("Session is  Created :" + id);

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session  =  se.getSession();
		String id = session.getId();
		log.info("Session is Destroyed :" + id);
	}
	
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		
	}

}
