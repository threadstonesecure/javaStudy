package dlt.study.app;

import java.io.Serializable;

/**
 * Created by denglt on 2016/4/12.
 */
public class ATest implements Serializable {

    public static void main(String[] args) {
        System.out.println(Child.class.isAssignableFrom(Base.class));
        System.out.println(Base.class.isAssignableFrom(Child.class));


    }

}

class Base{


}

class Child extends Base {

}


