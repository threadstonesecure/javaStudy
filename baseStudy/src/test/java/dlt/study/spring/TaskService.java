package dlt.study.spring;

import dlt.study.spring.aop.MyAop;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

import static dlt.utils.CommonUtils.log;

/**
 * Created by denglt on 2016/11/24.
 */
@MyAop
@Service
public class TaskService implements ITask{

    private String taskName;
    public TaskService(){
        this.taskName = "default";
    }

    public TaskService(String taskName){
        this.taskName = taskName;
    }


    //@Async
    public void doTask(){
        try {
            Object proxy = AopContext.currentProxy();  //ProxyConfig.setExposeProxy(true);

           // System.out.println("Proxy Object:" + proxy + ":" + proxy.getClass());
        }catch(Exception ex){

        }
        log.info("Target Object:" + this);
        try {
            Thread.sleep(10000);
        }catch (Exception ex){

        }
        log.info("doTask by " + taskName);
        doSubTask();
    }

    public void doSubTask(){
        log.info("doSubTask by " + taskName);
    }

    //@MyAop
    //@Async
    public Future<String> doTask(String content){
        try {
            Thread.sleep(30000);
        }catch (Exception ex){

        }

        return new AsyncResult<>(content + " from TaskService by " + taskName);
    }

    public  void doTaskNoAsync(){
        log.info("doTaskNoAsync by " + taskName);
    }
}
