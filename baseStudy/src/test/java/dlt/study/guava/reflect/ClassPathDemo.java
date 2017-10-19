package dlt.study.guava.reflect;

import com.google.common.reflect.ClassPath;
import org.junit.Test;

public class ClassPathDemo {

    @Test
    public void scanClass() throws Exception {
        ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
        classPath.getTopLevelClasses("dlt.study.netty").forEach(System.out::println);
    }
}
