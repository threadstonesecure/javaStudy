package dlt.study.guava.reflect;

import com.beust.jcommander.internal.Lists;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

public class InvokableDemo {

    @Test
    public void invoke() throws Exception {

        Method getMethod = List.class.getMethod("get", int.class);
        Invokable<List<String>, Object> invokable = new TypeToken<List<String>>() {}.method(getMethod);
        System.out.println(invokable.getReturnType().getRawType());
        Object result = invokable.invoke(Lists.newArrayList("denglt", "zyy", "dasdfasf"), 1);
        System.out.println(result);
    }
}
