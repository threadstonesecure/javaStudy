package dlt.study.javasecurity;

import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @Description:  -Djava.security.manager  -Djava.security.policy=//Users/denglt/onGithub/javaStudy/baseStudy/src/test/java/dlt/study/javasecurity/my.policy
 * @Package: dlt.study.javasecurity
 * @Author: denglt
 * @Date: 2018/10/15 11:32 AM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class SecurityManagerDemo {

    public Character aCharacter;

    @Test
    public void securityManager() {
        String ip =  "10.0.0.97";
        Integer port = 80;
        SecurityManager securityManager = System.getSecurityManager();
        System.out.println(securityManager);
        if (securityManager != null) {
           // securityManager.checkAccept(ip,port);
            securityManager.checkListen(port);
        }
        System.out.println("Accept:10.0.0.97:80");
    }

    @Test
    public void reflection() throws Exception {
        Field field = this.getClass().getField("aCharacter");
        field.setAccessible(true);
        Field type = Field.class.getDeclaredField("type");
        type.setAccessible(true);
        type.set(field, String.class);
        field.set(this, 'A');
        System.out.println(this.aCharacter);
    }
}
