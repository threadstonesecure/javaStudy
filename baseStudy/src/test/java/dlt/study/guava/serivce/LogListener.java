package dlt.study.guava.serivce;

import com.google.common.util.concurrent.Service;
import dlt.study.log4j.Log;

public class LogListener extends Service.Listener {

    private String name;

    LogListener(String name){
        this.name = name;
    }

    @Override
    public void starting() {
        Log.info("starting in " + name);
        super.starting();
    }

    @Override
    public void running() {
        Log.info("running in " + name);
        super.running();
    }

    @Override
    public void stopping(Service.State from) {
        Log.info("stopping in  " + name);
        super.stopping(from);
    }

    @Override
    public void terminated(Service.State from) {
        Log.info("terminated from " + from + " in  " + name);
        super.terminated(from);
    }

    @Override
    public void failed(Service.State from, Throwable failure) {
        Log.error("failed from "+ from +" in " + name, failure);
        super.failed(from, failure);
    }



}
