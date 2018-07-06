package dlt.web.view;

import java.util.List;
import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * AbstractCachingViewResolver具有缓存View功能的ViewResolver;一般基础该类来实现自己的ViewResolver
 * 
 * @author dlt
 *
 */
public class BusinessViewResolver implements ViewResolver, Ordered {

	private List<View> views;

	private int order = 1;

	public void setViews(List<View> views) {
		this.views = views;
	}

	public void addView(View v) {
		views.add(v);
	}

	@Override
	public View resolveViewName(String viewName, Locale locale)
			throws Exception {
		return loadView(viewName, locale);
	}

	protected View loadView(String viewName, Locale locale) throws Exception {
		System.out.println("BusinessViewResolver loadview.");
		for (View v : views) {
			if (viewName.equalsIgnoreCase(v.getClass().getName())) {
				return v;
			}
			if (v instanceof Matching) {
				Matching matching = (Matching) v;
				if (matching.startsWith(viewName)) {
					return v;
				}
				if (matching.endsWith(viewName)) {
					return v;
				}
			}
		}
		return null;
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
