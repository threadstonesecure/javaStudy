package dlt.web.interceptor;

import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TimeBasedAccessInterceptor extends HandlerInterceptorAdapter {
    private int openingTime;
    private int closingTime;

    public void setOpeningTime(int openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(int closingTime) {
        this.closingTime = closingTime;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (openingTime <= hour && hour < closingTime) {
        	System.out.println(Thread.currentThread()+":现在为工作时间，可以访问");
            return true;
        }
        //response.sendRedirect("http://host.com/outsideOfficeHours.html");
        response.setContentType("text/html;charset=utf-8"); 
		PrintWriter out=response.getWriter();
		out.println("非工作时间，不能登录！");
		out.flush();
		out.close();
        return false;
    }

}
