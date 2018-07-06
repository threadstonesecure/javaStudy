package dlt.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class StudyInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(StudyInterceptor.class);

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
        logger.info(StudyInterceptor.class.getName() + "-" + "preHandle()");
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
           // System.out.println("Bean:" + hm.getBean()); // a instance of Controller
           // System.out.println("BeanType" + hm.getBeanType()); // type of Controller
           logger.info("controller -> " + hm.getBean());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        logger.info(StudyInterceptor.class.getName() + "-" + "postHandle()");
        logger.info(StudyInterceptor.class.getName() + "-modelAndView-" + modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info(StudyInterceptor.class.getName() + "-" + "afterCompletion()");
        logger.info(StudyInterceptor.class.getName() + "-Exception-" + ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request,
                                               HttpServletResponse response, Object handler) throws Exception {
        logger.info(StudyInterceptor.class.getName() + "-" + "afterConcurrentHandlingStarted()");
    }

}
