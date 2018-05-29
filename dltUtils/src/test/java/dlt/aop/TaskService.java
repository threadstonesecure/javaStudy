package dlt.aop;

import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

import static dlt.utils.CommonUtils.log;

/**
 * Created by denglt on 2016/11/24.
 */
@Service
public class TaskService {

    private String taskName;

    public TaskService() {
        this.taskName = "default";
    }

    public TaskService(String taskName) {
        this.taskName = taskName;
    }

    public void doTask() {
        log.info("doTask by " + taskName);

        doSubTask(); // cglib 的AOP在在这儿是生效的，jdk动态代理不行
    }

    protected void  doSubTask(){
        log.info("doSubTask by " + taskName);
    }
}
