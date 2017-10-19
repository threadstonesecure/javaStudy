package dlt.study.guava.serivce;


import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import org.junit.Test;

public class ServiceDemo {

    @Test
    public void myIdleService() throws Exception {
        Service service = new MyIdleService("ServiceDlt");
        service.addListener(new LogListener("listener 1"), MoreExecutors.directExecutor());
        service.addListener(new LogListener("listener 2"), MoreExecutors.directExecutor());
        service.startAsync();

        service.awaitTerminated();
    }


    @Test
    public void startAsync() {
        Service service = new MyExecutionThreadService();
        service.addListener(new LogListener("listener1"), MoreExecutors.directExecutor());
        service.startAsync();
        service.awaitTerminated();
    }

    @Test
    public void startAndStop() throws Exception {
        Service service = new MyExecutionThreadService();
        service.addListener(new LogListener("listener1"), MoreExecutors.directExecutor());
        service.startAsync();
        Thread.sleep(1000);
        service.stopAsync();
        service.awaitTerminated();
    }

    @Test
    public void schedule() throws Exception {
        Service service = new MyScheduledService();
        service.addListener(new LogListener("listener 1"), MoreExecutors.directExecutor());
        service.startAsync();
        Thread.sleep(60000);
        service.stopAsync();
        service.awaitTerminated();
    }
}
