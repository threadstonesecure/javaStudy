package dlt.study.spring.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class ConsoleLogListener implements ApplicationListener<ApplicationEvent> {

	public void onApplicationEvent(ApplicationEvent event){
        System.out.print(this);
		System.out.println("event:" + event);
	}
}
