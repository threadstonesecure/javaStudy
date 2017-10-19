package dlt.web.view;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

/**
 * 自定义 建议继承 AbstractView
 * 
 * @author dlt
 *
 */
/**
 * @author dlt
 *
 */
public class MessageView extends MatchingAdapter implements View {

	public static final String MESSAGE_TEXT = MessageView.class.getName()
			+ ".TEXT";
	
	public static final String VIEW_NAME="MESSAGE";
	
	private static final String CONTENT_TYPE = "text/html;charset=utf-8";
	
	


	@Override
	public String getContentType() {
		return CONTENT_TYPE;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String msg = (String) model.get(MESSAGE_TEXT);
		response.setContentType(getContentType());

		PrintWriter out = response.getWriter();
		out.write("信息：" + msg);
		out.flush();
		out.close();
	}

	@Override
	public boolean endsWith(String viewName) {
		return viewName.equals(VIEW_NAME);
	}

	@Override
	public boolean startsWith(String viewName) {
		return viewName.equals(VIEW_NAME);
	}
	
	
	

}
