package dlt.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dlt.application.AppUser;
import dlt.utils.spring.MutlSpringContextUtils;
import dlt.utils.spring.SpringContextUtils;

public class UserServlet extends HttpServlet {

	
	private String rootuser;
	

	public String getRootuser() {
		return rootuser;
	}

	public void setRootuser(String rootuser) {
		this.rootuser = rootuser;
	}

	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		rootuser = this.getInitParameter("rootuser");
		this.getServletConfig();
		//this.getServletContext().getInitParameter();

	}

/*	同上
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		rootuser = config.getInitParameter("rootuser");
	}*/

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("rootuser=" + rootuser);
		// TODO Auto-generated method stub
		AppUser appUser =  SpringContextUtils.getBean("appUserImpl"); // new  AppUserImpl( new UserServiceImpl());
		AppUser appUser2 = MutlSpringContextUtils.getBean("appUserImpl");
		if (appUser == appUser2){
			System.out.println("true");
		}
		String userJson = appUser.toJson();
		//resp.setCharacterEncoding("utf-8");
		//resp.setContentType("text/html");
		resp.setContentType("text/html;charset=utf-8"); 
		PrintWriter out=resp.getWriter();
		out.println(userJson);
		out.flush();
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(req, resp);
	}

	
}
