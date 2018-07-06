package dlt.web.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class ConsoleLogListener implements ApplicationListener<ApplicationEvent> {

    private Logger logger = LoggerFactory.getLogger(ConsoleLogListener.class);

    public void onApplicationEvent(ApplicationEvent event) {
        logger.info("ApplicationEvent -> " + event);
    }
}
