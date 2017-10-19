package yunxin;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by denglt on 2017/2/10.
 */
public class IMDemo {

    private String AppKey = "78e60bb2f3674216f052c93ff35b9898";
    private String appSecret = "693c874a9a0e";
    @Test
    public void create() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpPost post = new HttpPost("https://api.netease.im/nimserver/user/create.action");
            post.addHeader("AppKey", AppKey);
            post.addHeader("Nonce", "123456");
            String curTime = "" + System.currentTimeMillis() / 1000L;
            String checkSum = CheckSumBuilder.getCheckSum(appSecret, "123456", curTime);
            post.addHeader("CurTime", curTime);
            post.addHeader("CheckSum", checkSum);
            List<BasicNameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("accid", "54589c72ef6c11e68bf700163e020f85"));
            parameters.add(new BasicNameValuePair("name", "大小鸟"));
            //parameters.add(new BasicNameValuePair("token", "*"));
            post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));

            CloseableHttpResponse response = httpClient.execute(post);
            System.out.println(response);
            System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
