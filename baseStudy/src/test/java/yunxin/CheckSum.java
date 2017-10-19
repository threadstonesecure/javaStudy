package yunxin;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.security.MessageDigest;
import java.util.Date;

/**
 * Created by denglt on 2017/2/10.
 */
public class CheckSum {

    public static void main(String[] args) {
        String curTime = "" + System.currentTimeMillis() / 1000L;
        System.out.println("curTime:" + curTime);
        curTime = String.valueOf((new Date()).getTime() / 1000L);
        System.out.println("curTime:" + curTime);
        String checkSum = CheckSumBuilder.getCheckSum("693c874a9a0e", "123456", curTime);
        System.out.println("checkSum:" + checkSum);
    }
}


