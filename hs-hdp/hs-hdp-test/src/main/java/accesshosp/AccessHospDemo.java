package accesshosp;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.AccessHospitalHandler;
import dlt.utils.spring.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by denglt on 2015/12/17.
 */
public class AccessHospDemo {
    private static Logger logger = LoggerFactory.getLogger(AccessHospDemo.class);

    public static void main(String[] args) {

        SpringContextUtils.init(new String[]{"base.xml", "dubbo2gateway.xml", "redis.xml"});
        final AccessHospitalHandler accessHospitalHandler = SpringContextUtils
                .getBean("accessHospitalHandler");
        ExecutorService tpe = Executors.newFixedThreadPool(300);
        for (int i = 0; i < 200; i++) {
            tpe.execute(new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    RequestPack requestPack = new RequestPack();
                    String[] hostIds = {"100009", "100009", "100009"};
                    Random r = new Random();
                    int i = r.nextInt(3);
                    //requestPack.setHosId(hostIds[i]);
                    requestPack.setHosId("999999-test");
                    requestPack.setCmd("业务代码:" + 10000);//System.currentTimeMillis());
                    requestPack.setBody("ddddd");
                    ResultPack result = accessHospitalHandler.getHospitalResult(
                            requestPack, 30);
                    long endTime = System.currentTimeMillis();
                    if (!result.getCmd().equals(requestPack.getCmd())) {
                        System.out.println("fuck !!!!!!!!!");
                    }
                    logger.info("time[" + (endTime - startTime) + "] -> " + result);
                }

            });
        }

    }

}
