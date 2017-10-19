package dlt.study.guava;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Test;

public class EventBusDemo {


    @Test
    public void eventBus(){
        EventBus eventBus = new EventBus();  // AsyncEventBus
        eventBus.register(new Logger());
        eventBus.post(new LogEvent());
    }


    private class Logger{

        @Subscribe
        @AllowConcurrentEvents
        public void log(LogEvent event){
            System.out.println("收到LogEvent");
        }
    }

}
