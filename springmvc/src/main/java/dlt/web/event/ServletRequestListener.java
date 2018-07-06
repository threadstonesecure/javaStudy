package dlt.web.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Service
public class ServletRequestListener implements
        ApplicationListener<ServletRequestHandledEvent> {
    private Logger logger = LoggerFactory.getLogger(ServletRequestListener.class);

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        logger.info("ServletRequestHandledEvent -> " + event);
    }

}
