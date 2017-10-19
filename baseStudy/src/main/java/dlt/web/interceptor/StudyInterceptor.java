package dlt.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import dlt.infrastructure.ThreadOut;

public class StudyInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler // When using the
															// RequestMappingHandlerMapping
															// the actual
															// handler is an
															// instance of
															// HandlerMethod
															// which identifies
															// the specific
															// controller method
															// that will be
															// invoked.
	) throws Exception {
		ThreadOut.println(StudyInterceptor.class.getName() + "-" + "preHandle");
		ThreadOut.println(StudyInterceptor.class.getName() + "-handler:");
		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod) handler;
			System.out.println("Bean:" + hm.getBean()); // a instance of
														// Controller
			System.out.println("BeanType" + hm.getBeanType()); // type of
																// Controller
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		ThreadOut
				.println(StudyInterceptor.class.getName() + "-" + "postHandle");
		ThreadOut.println(StudyInterceptor.class.getName() + "-modelAndView-"
				+ modelAndView);

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		ThreadOut.println(StudyInterceptor.class.getName() + "-"
				+ "afterCompletion");
		ThreadOut
				.println(StudyInterceptor.class.getName() + "-Exception-" + ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		ThreadOut.println(StudyInterceptor.class.getName() + "-"
				+ "afterConcurrentHandlingStarted");
	}

}
