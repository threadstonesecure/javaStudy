package dlt.study.spring;

import org.junit.Test;
import org.springframework.cglib.core.DebuggingClassWriter;

import javax.annotation.Resource;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static dlt.utils.CommonUtils.log;

/**
 * Created by denglt on 2016/11/24.
 */
public class TaskDemo extends JUnit4Spring {

    static {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/logs/cglib/class");
    }

    @Resource
    private TaskService taskService;

    @Test
    public void async() throws Exception{
        log.info("begin...");
        taskService.doTask();
        log.info("end!");
        System.in.read();
    }

    @Test
    public void async2(){
        log.info("begin...");
        Future<String> future = taskService.doTask("hello");
        log.info("end!");
        log.info("get result ...");
        try {
            String result = future.get(60000, TimeUnit.MILLISECONDS);
            log.info("async result:" + result);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
