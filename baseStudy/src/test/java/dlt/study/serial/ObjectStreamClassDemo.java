package dlt.study.serial;

import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

/**
 * Created by denglt on 2017/6/27.
 */
public class ObjectStreamClassDemo {

    @org.junit.Test
    public void  getSerialVersionUID(){
        ObjectStreamClass objectStreamClass = ObjectStreamClass.lookup(C.class);
        System.out.println(objectStreamClass.getSerialVersionUID());
    }

    @org.junit.Test
    public void getDeclaredField() throws Exception {
        Field f = C.class.getDeclaredField("serialVersionUID");
        System.out.println(f);
    }

    @org.junit.Test
    public void getDeclaredField2() throws Exception {
        Field f = C.class.getDeclaredField("name");
        System.out.println(f);
    }

    @org.junit.Test
    public void getField() throws Exception {
        Field f = C.class.getField("name");
        System.out.println(f);
    }
}
