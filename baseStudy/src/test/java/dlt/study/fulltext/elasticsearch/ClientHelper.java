package dlt.study.fulltext.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @Description:
 * @Package: dlt.study.fulltext.elasticsearch
 * @Author: denglt
 * @Date: 2018/10/24 11:34 AM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class ClientHelper {

    private static String host1 = "47.106.93.69";
    private static int port = 9300;

    private static volatile TransportClient client;

    public static TransportClient getClient() throws Exception {
        if (client == null) {
            synchronized (ClientHelper.class) {
                if (client == null)
                    client = new PreBuiltTransportClient(Settings.EMPTY)
                            .addTransportAddress(new TransportAddress(InetAddress.getByName(host1), port));
            }
        }
        return client;
    }
}
