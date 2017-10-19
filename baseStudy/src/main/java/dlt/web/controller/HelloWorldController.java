package dlt.web.controller;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.Date;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import dlt.application.AppUser;
import dlt.domain.model.User;
import dlt.web.view.MessageView;

@Controller
// @RestController is a stereotype annotation that combines @ResponseBody and
// @Controller

@RequestMapping(value = "/helloWorld")
@SessionAttributes(value = "currentUser")
public class HelloWorldController {

	@Autowired
	AppUser appUser;

	/*
	 * A controller can have any number of @ModelAttribute methods. All such
	 * methods are invoked before @RequestMapping methods of the same
	 * controller.
	 */
	@ModelAttribute
	public User findUser(String findUserName) {
		System.out.println("userName = " + findUserName);
		return appUser.findUser(findUserName); // 该User会放入到Model中
	}

	@ModelAttribute
	public void populateModel(String findUserName, Model model) {
		System.out.println("Model:" + model);
		System.out.println(findUserName + "被加入到Model");
		model.addAttribute("helloName", findUserName);
		model.addAttribute("user2", appUser.findUser(findUserName));
		model.addAttribute("wife", appUser.findUser("zyy"));
		System.out.println("Model:" + model);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		/*
		 * System.out.println("WebDataBinder in HelloWorldController class:");
		 * System.out.println("WebDataBinder:" + binder);
		 */
	}

	@RequestMapping(value = "helloWorld")
	@ResponseBody
	public String helloWorld(User user, @ModelAttribute("wife") User wife,
			@ModelAttribute("helloName") String helloName) {
		return "hello " + helloName + "\nhello " + user + "\n" + "hello "
				+ wife;
	}

	@RequestMapping(value = "writerHtml")
	@ResponseBody
	public void writerHtml(Writer writer) throws IOException {
		writer.write("<html> <h2> Hello World </h2> </html>");
	}

	public String writerHtml() {
		return "<html> <h2> Hello World </h2> </html>"; // 与上面的
														// writer.write返回是不同的，这儿的返回为"Content-Type:text/plain;"
	}

	@RequestMapping(value = "requestBody", method = RequestMethod.POST)
	public void handle(Writer writer,User body)
			throws IOException {
		writer.write(body.toString());
	}

	@RequestMapping(value = "welcomeUser")
	@ResponseBody
	public String welcomeUser(@ModelAttribute("currentUser") User currentUser,
			HttpSession session,
			@CookieValue(value = "userName", required = false) String userName) {
		Object user = session.getAttribute("currentUser");
		System.out.println("currentUser:" + user);
		System.out.println("cookie:" + userName);
		return "Welcome " + currentUser.getUserName();
	}

	@RequestMapping(value = "cookie")
	@ResponseBody
	public String cookie(@CookieValue("JSESSIONID") String sessionid,
			@CookieValue(value = "userName", required = false) String userName,
			@RequestHeader("cookie") String cookies) {
		System.out.println("cookies:" + cookies);
		StringBuffer sb = new StringBuffer();
		sb.append("JSESSIONID:" + sessionid + "\n");
		sb.append("userName:" + userName + "\n");
		return sb.toString();
	}

	@RequestMapping(value = "customDataBinder")
	@ResponseBody
	public String customDataBinder(double money, Date createDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("money:" + money + "\n");
		sb.append("createDate:" + createDate + "\n");
		return sb.toString();
	}

	@RequestMapping(value = "viewResolver")
	public ModelAndView handle() {
		System.out.println("test ModelAndView...");
		ModelAndView mv = new ModelAndView();
		mv.setViewName(MessageView.class.getName());
		// mv.setView(new MessageView());
		mv.addObject(MessageView.MESSAGE_TEXT, "Test on ModelAndView is ok!");
		return mv;
	}

	@RequestMapping(value = "viewResolver2")
	public String handle(Model model) {
		System.out.println("test ModelAndView...");
		model.addAttribute(MessageView.MESSAGE_TEXT,
				"Test on ModelAndView is ok!");
		// return MessageView.class.getName();
		return MessageView.VIEW_NAME;
	}

	@RequestMapping(value = "buildUrl")
	@ResponseBody
	public URI buildUrl() {

		UriComponents uriComponents = MvcUriComponentsBuilder.fromMethodName(
				HelloWorldController.class, "buildUrl", new Object[0]).buildAndExpand();

		URI uri = uriComponents.encode().toUri();
		return uri;
	}
	
	@RequestMapping(value="uploadFile")
	@ResponseBody
	public String uploadFile(@RequestParam("file") MultipartFile[] file){
		System.out.println(file.getClass());
		StringBuffer sb = new StringBuffer();
		sb.append("上传文件信息：\n");
		sb.append("文件名                  大小 （字节）\n");
		for (MultipartFile f :file){
			sb.append(f.getOriginalFilename() +"    " + f.getSize()+"\n");
		}
	//	sb.append(file.getOriginalFilename() +"    " + file.getSize()+"\n");
		return sb.toString();
	}
}
