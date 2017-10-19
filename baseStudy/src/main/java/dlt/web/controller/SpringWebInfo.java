package dlt.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import dlt.utils.spring.MutlSpringContextUtils;
import dlt.utils.spring.SpringContextUtils;

@Controller
@RequestMapping(value = "springweb")
public class SpringWebInfo {

	@RequestMapping(value = "infoRequest")
	@ResponseBody
	public String infoRequest(ServletRequest request) {
		System.out.println(request.getContentType());
		StringBuffer sb = new StringBuffer();
		sb.append("ServletRequest:" + request.toString() + "\n");
		sb.append("Attributes:" + "\n");
		Enumeration<String> es = request.getAttributeNames();
		while (es.hasMoreElements()) {
			String e = es.nextElement();
			sb.append(e + "=" + request.getAttribute(e));
			sb.append("\n");
		}
		sb.append("Paramete:" + "\n");
		Enumeration<String> parameters = request.getParameterNames();
		while (parameters.hasMoreElements()) {
			String param = parameters.nextElement();
			String value = request.getParameter("username");
			sb.append(param + "=" + value);
			sb.append("\n");
		}
		return sb.toString();
	}

	@RequestMapping(value = "servletContext")
	@ResponseBody
	public String servletContext(ServletRequest request) {
		
		ServletContext sc = request.getServletContext();
		StringBuffer sb = new StringBuffer();
		sb.append("ServletContex:" + sc.toString() + "\n");
		sb.append("Attributes:" + "\n");
		Enumeration<String> es = sc.getAttributeNames();
		while (es.hasMoreElements()) {
			String e = es.nextElement();
			sb.append(e + "=" + sc.getAttribute(e));
			sb.append("\n");
		}

		String name = (String) sc.getAttribute("name");
		if (name == null) {
			sc.setAttribute("_name", "邓隆通");

		}
		return sb.toString();

	}
	
	@RequestMapping("/requestHeader")
	@ResponseBody
	public ResponseEntity<String> requestHeader(
			HttpEntity<byte[]> requestEntity, String userName)
			throws UnsupportedEncodingException {
		System.out.println("userName=" + userName);
		StringBuffer sb = new StringBuffer();
		HttpHeaders headers = requestEntity.getHeaders();
		Set<Entry<String, List<String>>> headerinfo = headers.entrySet();
		sb.append("Request Headers  \n");
		for (Entry<String, List<String>> e : headerinfo) {
			String name = e.getKey();
			List<String> value = e.getValue();
			sb.append(name + ":" + value.toString() + "\n");
		}

		MediaType mediaType = headers.getContentType();
		sb.append("Request Body  \n");
		byte[] requestBody = requestEntity.getBody();

		sb.append("请求体字节长度 ：" + requestBody.length + "\n");
		if (requestBody.length > 0) {
			String bodyContext = new String(requestBody);
			bodyContext = URLDecoder.decode(bodyContext, mediaType.getCharSet()
					.name());
			sb.append(bodyContext + "\n");
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("MyResponseHeader", "MyValue");
		return new ResponseEntity<String>(sb.toString() +  "中文", responseHeaders,
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "servletContext2")
	@ResponseBody
	public String servletContext2(ServletContext sc) { // 无法获取ServletConetxt
		System.out.println(sc);
		return "ok";
	}

	@RequestMapping(value = "appContext")
	@ResponseBody
	public String infoApplicationContext(ServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append("通过类MutlSpringContextUtils获取:");
		sb.append("\n");
		List<ApplicationContext> acs = MutlSpringContextUtils.getApplicationContext();
		for (ApplicationContext context : acs) {
			sb.append(context);
			sb.append("\n");
		}
		sb.append("通过类RequestContextUtils获取:");
		sb.append("\n");
		WebApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
		sb.append("RequestContextUtils.getWebApplicationContext:" + context);
		sb.append("\n");
		sb.append("通过类WebApplicationContextUtils获取:");
		sb.append("\n");
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
		sb.append("WebApplicationContextUtils.getRequiredWebApplicationContext:"
				+ context);
		sb.append("\n");
		return sb.toString();
	}

	@RequestMapping(value = "beanNames")
	@ResponseBody
	public String infoBeanName(ServletRequest request) {
		StringBuffer sb = new StringBuffer();
		WebApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
		String[] beanNames = context.getBeanDefinitionNames();
		for (String name : beanNames) {
			Object bean = context.getBean(name);
			sb.append(name + "=" + bean + "\n");
		}
		return sb.toString();
	}
	
	@RequestMapping(value = "requestMappingHandlerAdapter")
	@ResponseBody
	public String requestMappingHandlerAdapter(ServletRequest request) {
		StringBuffer sb = new StringBuffer();
		WebApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
		RequestMappingHandlerAdapter adapter=null;
		if (context.containsBean("requestMappingHandlerAdapter")) {
			adapter = (RequestMappingHandlerAdapter) context.getBean("requestMappingHandlerAdapter");
			sb.append("用户定义RequestMappingHandlerAdapter:" + adapter + "\n");
			if (adapter != null) {
				List<HttpMessageConverter<?>> coverters = adapter.getMessageConverters();
				sb.append("MessageConverters:\n");
				for (HttpMessageConverter converter : coverters) {
					sb.append(converter.getClass().getName() + "=" +  converter+ "\n");
				}
			}
		}
		sb.append("\n\n\n");
		adapter = (RequestMappingHandlerAdapter) context.getBean("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#0");
		sb.append("Spring默认RequestMappingHandlerAdapter:" + adapter + "\n");
		if (adapter != null) {
			List<HttpMessageConverter<?>> coverters = adapter
					.getMessageConverters();
			sb.append("MessageConverters:\n");
			for (HttpMessageConverter converter : coverters) {
				sb.append(converter.getClass().getName() + "=" + converter+ "\n");
			}
		}
		return sb.toString();
	}

	@RequestMapping(value="dispatcherServlet")
	@ResponseBody
	public String dispatcherServlet(HttpServletRequest request){

		ServletContext sc = request.getServletContext();
		ServletRegistration sr =  sc.getServletRegistration("extjs-web");
		Collection<String> cs =  sr.getMappings() ;
		System.out.println(cs);
		System.out.println(sr);
		return null;
	}
}
