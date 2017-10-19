package dlt.web.view;

public class MatchingAdapter implements Matching {

	@Override
	public boolean endsWith(String viewName) {
		return false;
	}

	@Override
	public boolean startsWith(String viewName) {
		return false;
	}

}
