package dlt.web.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import dlt.application.AppUser;
import dlt.domain.model.User;
import dlt.web.view.MessageView;

@Controller
// A @RequestMapping on the class level is not required. Without it, all paths
// are simply absolute, and not relative.
@RequestMapping(value = "user")
@SessionAttributes(value = "currentUser")
public class UserAction {

	Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private AppUser appUser;

	@RequestMapping(value = "listuser")
	@ResponseBody
	public List<User> getUsers(String username) {
		System.out.println("username=" + username);
		return appUser.listUsers();
	}

	@RequestMapping(value = "listuserjson")
	@ResponseBody
	public String getUserJson() {
		return appUser.toJson();
	}

	@RequestMapping(value = "logon")
	public String logon(
			String userName,
			String password,
			Model model,
			HttpServletResponse response,
			HttpServletRequest request,
			@CookieValue(value = "userName", required = false) String cUserName,
			@CookieValue(value = "JSESSIONID", required = false) String sessionid) {
		log.info("@CookieValue:JSESSIONID=" + sessionid);
		HttpSession session = request.getSession();
		log.info("Request:JSESSIONID" + session.getId());
		User user = appUser.findUser(userName);

		if (user != null) {
			model.addAttribute("currentUser", user); // 将会放入session中
			Cookie userCook = new Cookie("userName", user.getUserName());
			userCook.setPath(request.getContextPath());// 这个很关键（Cookie是跟路径相关的），否则HelloWorldController中获取不到该Cookie
			userCook.setMaxAge(1000000);
			response.addCookie(userCook);
			System.out.println("findUser:" + user);
			if (userName.equals(cUserName)) {
				System.out.println("欢迎再次归来！");
			}
		} else {
			System.out.println("no find user:" + userName);
			String context = "no find user:" + userName;
			model.addAttribute(MessageView.MESSAGE_TEXT, context);
			return MessageView.VIEW_NAME;
		}
		// return "redirect:/index.jsp";
		return "redirect:../helloWorld/welcomeUser"; // 这儿会有个ModelAndView（View为redirect:../helloWorld/welcomeUser;Model为model）
		// return "../helloWorld/welcomeUser";
	}

	@RequestMapping(value = "logoff")
	public String lonoff(HttpSession session) {
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
	}

	@RequestMapping(value = "toIndex")
	// 跳转
	public String gotoIndex() {
		return "redirect:/index.html";
	}

	@RequestMapping(value = "paramObject")
	public void paramObject(Writer writer, User user) throws IOException {
		System.out.println(user.getUserName() + ":" + user.getAge());
		writer.write(user.toString());
	}

	@RequestMapping(value = "ajaxString", method = RequestMethod.POST)
	@ResponseBody
	public User ajaxString(User user) throws IOException {
		System.out.println(user);
		return user;
	}

	public String ajaxString2(User user, Writer writer) throws IOException {
		System.out.println(user);
		writer.write("ok");
		return null;
	}

	@RequestMapping(value = "ajaxJson", method = RequestMethod.POST, consumes = "application/json",produces="application/json")
	@ResponseBody
	public List<User> ajaxJson(@RequestBody List<User> users) {

		for (User user : users) {
			System.out.println(user);
		}
		return users;
	}

	@RequestMapping(value = "checkusers")
	@ResponseBody
	public List<User> checkusers(String[] usernames, boolean isMarried) {
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < usernames.length; i++) {
			User user = new User();
			user.setUserName(usernames[i]);
			user.setMarried(isMarried);
			users.add(user);
		}
		return users;
	}

	@RequestMapping(value = "asyncRequest")
	@ResponseBody
	public WebAsyncTask<User> asyncRequest(String userName) {
		MyCallable callable = new MyCallable(userName);
		WebAsyncTask<User> act = new WebAsyncTask<User>(2000, callable);
		act.onCompletion(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread() + ":异步请求处理完成！");
			}

		});
		act.onTimeout(new Callable<User>() {
			@Override
			public User call() throws Exception {
				System.out.println(Thread.currentThread() + ":异步请求处理超时!");
				User user = new User();
				user.setUserId(-1);
				user.setUserName("异步请求处理超时");
				return user;
			}

		}); // 到达 DeferredResult的效果
		return act;
	}

	public Callable<User> asyncRequest2(String userName) {
		System.out.println(Thread.currentThread());
		MyCallable callable = new MyCallable(userName);
		return callable;
	}

	private class MyCallable implements Callable<User> {

		private String userName;

		public MyCallable(String userName) {
			this.userName = userName;
		}

		@Override
		public User call() throws Exception {
			System.out.println(Thread.currentThread() + ":MyCallable.call");
			// Thread.sleep(10000);
			User user = appUser.findUser(userName);
			return user;
		}

	}
}
