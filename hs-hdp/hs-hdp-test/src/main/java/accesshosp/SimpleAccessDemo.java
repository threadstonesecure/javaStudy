package accesshosp;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.AccessHospitalHandler;
import dlt.utils.spring.SpringContextUtils;

/**
 * Created by denglt on 16/10/19.
 */
public class SimpleAccessDemo {
    public static void main(String[] args) throws Exception {
        SpringContextUtils.init(new String[]{"dubbo-client.xml"});
        final AccessHospitalHandler accessHospitalHandler = SpringContextUtils.getBean("accessHospitalHandler");
        Thread.sleep(10000);
        for (int i=0; i<2;i++) {
            RequestPack requestPack = new RequestPack();
            requestPack.setHosId("999999-test");
            requestPack.setCmd("业务代码:" + 10000);
            requestPack.setBody("ddddd");
            ResultPack result = accessHospitalHandler.getHospitalResult(requestPack, 30);
            System.out.println(result);
        }
    }

}

