package dlt.study.netflix.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import dlt.study.log4j.Log;

public class CommandHelloWorld extends HystrixCommand<String> {
    private  String name;
    private  int runtime =0 ;


    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    public CommandHelloWorld(String name, int runtime) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
        this.runtime = runtime;
    }

    @Override
    protected String run() {
        Log.info("CommandHelloWorld is running!");
        try {
            Thread.sleep(runtime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello " + name + "!";
    }


}
