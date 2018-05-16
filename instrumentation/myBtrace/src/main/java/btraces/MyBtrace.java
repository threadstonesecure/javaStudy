package btraces;


import static com.sun.btrace.BTraceUtils.*;

import com.sun.btrace.annotations.*;

@BTrace
public class MyBtrace {

    @OnMethod(
            clazz = "dlt.study.Trans",
            method = "run",
            location = @Location(value = Kind.RETURN)
    )
    public static void printMethodRunTime(int sleepTime /* 需要method 完成的参数列表，也可以使用AnyType[],注：参数列表会作为trace method的判定 */,
                                          @Self Object target,
                                          @ProbeClassName String className,
                                          @ProbeMethodName String method,
                                          @Duration long duration) {
       // println(className + "." + method + " -> parms:" + str(sleepTime));
        println("duration:" + duration / 1000000 + " ms");
        println("self:" + classOf(target));
        println("self.cname:" + get(field("dlt.study.Trans", "cname"))); // 只能是static
        println("self.name:" + get(field("dlt.study.Trans", "name"), target));
        printFields(target);

    }


}
