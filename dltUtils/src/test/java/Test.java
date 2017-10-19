import org.springframework.util.DigestUtils;
import sun.reflect.Reflection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denglt on 2016/5/3.
 */
public class Test {
    public static void main(String[] args) {
        Integer  i  = null;
        Integer  i2 = 10000000;
        if (i == 10000000){
            System.out.println("==");
        }else{
            System.out.println("!=");
        }
    }
}
