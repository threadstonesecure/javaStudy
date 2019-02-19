package dlt.study.spring.cron;

import org.junit.Test;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @Package: dlt.study.spring.cron
 * @Author: denglt
 * @Date: 2019/2/19 4:44 PM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class CronExpression {
    
    @Test
    public void cron(){
        CronTrigger cronTrigger = new CronTrigger("0 15 10 15 * ?");
        SimpleTriggerContext simpleTriggerContext = new SimpleTriggerContext();
        System.out.println(formateDate(cronTrigger.nextExecutionTime(simpleTriggerContext)));
       // System.out.println(cronTrigger);
    }

    @Test
    public void cron2(){
        CronTrigger cronTrigger = new CronTrigger("1 0 0 * * ?");
        SimpleTriggerContext simpleTriggerContext = new SimpleTriggerContext();
        for(int i=0; i< 10;i++){
            Date d = cronTrigger.nextExecutionTime(simpleTriggerContext);
            System.out.println(formateDate(d));
            simpleTriggerContext = new SimpleTriggerContext(d,d,d);
        }
    }

    public static String formateDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
