package dlt.web.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@SessionAttributes(value = "currentUser") 不能有该语句，否则currentUser会被放到新产生的session中
public class LogoutAction {
	Log log = LogFactory.getLog(this.getClass());
	@RequestMapping(value="/logout")
	public String handle(HttpSession session) {
		log.info("before invalidate , session id is " + session.getId());
		session.invalidate();
		
		log.info("after invalidate , session id is " + session.getId());
		try {
			Object o = session.getAttribute("currentUser");
			log.info("currentUser :" + o);
		} catch (Exception e) {
			log.error(e);
		}
		return "redirect:/index.jsp";
		//return "logout";  //这个会被InternalResourceViewResolver中viewClass指定的view处理。会有错误，因为session已经失效了。
	}
}
