package accesshosp;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.service.DowndataHandler;
import dlt.utils.spring.SpringContextUtils;

/**
 * Created by denglt on 2015/12/1.
 */
public class DownDataDemo {

    public static void main(String[] args) {

        SpringContextUtils.init(new String[]{"dubbo-client.xml"});
        DowndataHandler downdataHandler = SpringContextUtils.getBean("downdataHandler");
        RequestPack requestPack = new RequestPack();
        requestPack.setHosId("100009");
        int i=0;
        while(i<=5) {
            i++;
            requestPack.setCmd("test===>"+i);
            if (downdataHandler.downData(requestPack)) {
                System.out.println("发送成功！===>" + i);
            } else {
                System.out.println("发送失败！===>" + i);
            }
        }

    }
}
