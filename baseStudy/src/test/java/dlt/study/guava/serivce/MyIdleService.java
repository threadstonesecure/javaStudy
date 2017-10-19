package dlt.study.guava.serivce;

import com.google.common.util.concurrent.AbstractIdleService;
import dlt.study.log4j.Log;

import java.util.concurrent.Executor;

public class MyIdleService extends AbstractIdleService {

    private String serviceName = "MyIdleService" ;

    public MyIdleService(){}

    public MyIdleService(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    protected void startUp() throws Exception {
        Thread.sleep(3000);
        Log.info(serviceName +" startUP()");
    }

    @Override
    protected void shutDown() throws Exception {
        Log.info(serviceName + " shutDown()");
    }

    @Override
    protected Executor executor() {
        return super.executor();
    }
}
