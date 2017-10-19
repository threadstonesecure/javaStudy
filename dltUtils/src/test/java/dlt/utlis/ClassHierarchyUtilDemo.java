package dlt.utlis;

import dlt.utils.ClassHierarchyUtil;
import org.junit.Test;
import org.springframework.cglib.proxy.InterfaceMaker;

/**
 * Created by denglt on 2016/12/7.
 */
public class ClassHierarchyUtilDemo {

    @Test
    public void jiajia(){
        int i=0;
        int j=++i;
        System.out.println(i);
        System.out.println(j);
    }

    @Test
    public void printClass() {
        ClassHierarchyUtil.printClass(String.class);
    }

    @Test
    public void getAllInterface() {
        Class[] interfaces = ClassHierarchyUtil.getAllInterface(String.class);
        for (Class temp : interfaces) {
            System.out.println(temp);
        }
    }

    @Test
    public void interfaceMaker() {
        InterfaceMaker interfaceMaker = new InterfaceMaker();
        //抽取某个类的方法生成接口方法
        interfaceMaker.add(String.class);
        Class<?> targetInterface = interfaceMaker.create();
        System.out.println(targetInterface);
        ClassHierarchyUtil.printClass(targetInterface);
    }
}
