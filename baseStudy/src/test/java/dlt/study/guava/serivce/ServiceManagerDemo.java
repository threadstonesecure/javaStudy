package dlt.study.guava.serivce;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import dlt.study.log4j.Log;
import org.junit.Test;

import java.util.List;

public class ServiceManagerDemo {

    @Test
    public void serviecs() throws Exception {
        List<Service> services = ImmutableList.of(new MyIdleService(), new MyExecutionThreadService(), new MyScheduledService());
        ServiceManager serviceManager = new ServiceManager(services);
        serviceManager.addListener(new MyListener());
        serviceManager.startAsync();

        serviceManager.servicesByState().asMap().forEach((k, v) -> System.out.println(v + " -> " + v));
        Thread.sleep(60000);

        services.forEach((service) -> service.stopAsync());

        serviceManager.awaitStopped();

    }

    private static class MyListener extends ServiceManager.Listener {
        @Override
        public void healthy() {
            Log.info("healthy");
        }

        @Override
        public void stopped() {
            Log.info("stopped");
        }

        @Override
        public void failure(Service service) {
            Log.error("failure " + service);
        }
    }
}
