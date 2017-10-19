package dlt.study.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by denglt on 2016/12/14.
 */
public class InheritedDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("MyAop的标注");
        for (Annotation annotation :MyAop.class.getAnnotations()){
            System.out.println(annotation);
        }

        System.out.println("NoInheritedAop的标注");
        for (Annotation annotation :NoInheritedAop.class.getAnnotations()){
            System.out.println(annotation);
        }
        System.out.println("IAop的标注：");
        for (Annotation annotation :IAop.class.getAnnotations()){
            System.out.println(annotation);
        }


        System.out.println("AopImpl的标注：");
        for (Annotation annotation :AopImpl.class.getAnnotations()){
            System.out.println(annotation);
        }

        System.out.println("SubAopImpl的标注：");
        for (Annotation annotation :SubAopImpl.class.getAnnotations()){
            System.out.println(annotation);
        }

        System.out.println("=====================");
        System.out.println("==doAop的标注==");
        Method m = IAop.class.getMethod("doAop");
        System.out.println("IAop.doAop的标注：");
        System.out.println(m);
        for (Annotation annotation :m.getAnnotations()){
            System.out.println(annotation);
        }

        m = AopImpl.class.getMethod("doAop");
        System.out.println("AopImpl.doAop的标注：");
        System.out.println(m);
        for (Annotation annotation :m.getAnnotations()){
            System.out.println(annotation);
        }

        m = SubAopImpl.class.getMethod("doAop");
        System.out.println("SubAopImpl.doAop的标注：");
        System.out.println(m);
        for (Annotation annotation :m.getAnnotations()){
            System.out.println(annotation);
        }

        m = SubAopImpl.class.getMethod("doAop2");
        System.out.println("SubAopImpl.doAop2的标注：");
        System.out.println(m);
        for (Annotation annotation :m.getAnnotations()){
            System.out.println(annotation);
        }
    }
}
