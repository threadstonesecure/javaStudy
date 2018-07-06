package dlt.web.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

public class OnlineInterceptor extends HandlerInterceptorAdapter {

	Log log = LogFactory.getLog(this.getClass());
	
	private String sessionAttributeName;
	
	
	public void setSessionAttributeName(String sessionAttributeName) {
		this.sessionAttributeName = sessionAttributeName;
	}


	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		log.info("进入 OnlineInterceptor:preHandle");
        HttpSession session = request.getSession();
        Object o = null ;
        if (session !=  null){
		 //o = WebUtils.getSessionAttribute(request, sessionAttributeName) ;
		   o= session.getAttribute(sessionAttributeName);
		   log.info("Http Session's id is " + session.getId());
        }else{
        	log.info("Http Session is null !");
        }
        log.info(sessionAttributeName +":" + o);
		if (o == null) {
	        response.setContentType("text/html;charset=utf-8"); 
			PrintWriter out=response.getWriter();
			out.println("请先登录！(使用中断实现。class：OnlineInterceptor)");
			out.flush();
			out.close();		
			log.info("退出  OnlineInterceptor:preHandle");
			return false;
		}
		log.info("退出  OnlineInterceptor:preHandle");
		return true;
	}

	
}
