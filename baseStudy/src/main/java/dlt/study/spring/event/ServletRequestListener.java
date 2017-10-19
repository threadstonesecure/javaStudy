package dlt.study.spring.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Service
public class ServletRequestListener implements
		ApplicationListener<ServletRequestHandledEvent> {
	@Override
	public void onApplicationEvent(ServletRequestHandledEvent event) {
		// TODO Auto-generated method stub
        System.out.print(this);
		System.out.println("ServletRequest信息 ："+ event);
	}

}
