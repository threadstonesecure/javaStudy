package dlt.study.spring.cglib;

import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

import static dlt.utils.CommonUtils.log;

/**
 * Created by denglt on 2016/11/24.
 */
//@Service("cglib.taskService")
public class TaskService {

    private String taskName;
    public TaskService(){
        this.taskName = "default";
    }

    public TaskService(String taskName){
        this.taskName = taskName;
    }

    public void inner(){
        log.info("inner by " + taskName);
    }
    public void doTask(){

        inner();
        log.info("doTask by " + taskName);
    }


    public  void doTaskNoAsync(){
        log.info("doTaskNoAsync by " + taskName);
    }
}
