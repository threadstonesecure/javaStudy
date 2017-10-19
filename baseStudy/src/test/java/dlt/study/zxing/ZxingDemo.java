package dlt.study.zxing;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import dlt.utils.ZXingUtils;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Created by denglt on 16/9/30.
 */
public class ZxingDemo {
    private String filename = "/tmp/zxing.png";

   @Test
    public void encode() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("www", "http://www.baidu.com");
        jsonObject.put("author", "邓隆通");
        String content = jsonObject.toJSONString();
        System.out.println(content);
        ZXingUtils.encode(content, BarcodeFormat.QR_CODE, 200, 200, Paths.get(filename));
    }

    @Test
    public void decode() throws Exception {
        Result result = ZXingUtils.decode(Paths.get(filename));
        System.out.println(result.getText());
        JSONObject content = JSONObject.parseObject(result.getText());
        System.out.println("图片中内容：  ");
        System.out.println("author： " + content.getString("author"));
        System.out.println("www：  " + content.getString("www"));
        System.out.println("图片中格式：  ");
        System.out.println("encode： " + result.getBarcodeFormat());
    }

    @Test
    public void withByte() throws Exception {
        String content = "邓隆通";  //邓隆通 //汪安奎  // 金艳华 // 燕常伟  钟永健
        byte[] bs = ZXingUtils.encode(content,BarcodeFormat.QR_CODE,200,200,"png");
        Result result = ZXingUtils.decode(bs);
        System.out.println(result.getText());
    }


    @Test
    public void encode1D() throws Exception{
        String content = "denglt345435fgsdafdjhasdfsadfsadfasdfsadfasdfs";
        System.out.println(content);
        ZXingUtils.encode(content, BarcodeFormat.CODE_128, 200, 50, Paths.get(filename));
    }

    @Test
    public void decode1D() throws Exception{
        Result result = ZXingUtils.decode(Paths.get(filename));
        System.out.println(result.getText());
    }

    @Test
    public void decodeFromPic() throws Exception{
        Result result = ZXingUtils.decode(Paths.get("/Users/denglt/Dropbox/myprograme/javaStudy/oracleDba/src/main/webapp/oracle/adminscript/money/zfb_366.png"));
        System.out.println("结果:" + result.getText());
        System.out.println(result.getBarcodeFormat());
    }


}
