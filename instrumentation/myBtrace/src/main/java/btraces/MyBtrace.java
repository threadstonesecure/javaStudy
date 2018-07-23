package btraces;


import static com.sun.btrace.BTraceUtils.*;

import com.sun.btrace.annotations.*;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

@BTrace
public class MyBtrace {

    @OnMethod(
            clazz = "dlt.study.Trans",
            method = "run",
            location = @Location(value = Kind.RETURN)
    )
    public static void printMethodRunTime(int sleepTime,  //需要method 完成的参数列表，也可以使用AnyType[],注：参数列表会作为trace method的判定 ,
                                          @Self Object target,
                                          @ProbeClassName String className,
                                          @ProbeMethodName String method,
                                          @Duration long duration) {
        println(className + "." + method + " -> parms:" + str(sleepTime));
        println("duration:" + duration / 1000 + " ms");
        println("self:" + classOf(target));
        println("self.cname:" + get(field("dlt.study.Trans", "cname"))); // 只能是static
        println("self.name:" + get(field("dlt.study.Trans", "name"), target));
        printFields(target);

    }

    //dlt.study.Trans.run()中每个调用方法的消耗时间
    @OnMethod(clazz = "dlt.study.Trans", method = "run",
            location = @Location(value = Kind.CALL, clazz = "/.*/", method = "/.*/", where = Where.AFTER)) //​如果想获得执行时间，必须把Where定义成AFTER
    public static void timeInRun(@Self Object self,
                                 @ProbeClassName String className,
                                 @ProbeMethodName String proboMethod,
                                 @TargetInstance Object instance, //静态函数中，instance的值为空
                                 @TargetMethodOrField String method,
                                 @Duration long duration) {
        //println(className + "." + proboMethod + " => ");
        println( (instance != null ? classOf(instance): "null") + "." + method + ", duration:" + duration / 1000000 + " ms");
    }

    @OnMethod(
            clazz="+java.lang.Runnable",   // "+"匹配所有子类型
            method="run"
    )
    public static void runnable(@ProbeClassName String pcn, @ProbeMethodName String pmn) {
        // on every Runnable.run() method entry print class.method
        print(pcn);
        print('.');
        println(pmn);
    }

    @OnMethod(
            clazz="java.lang.Thread",
            location=@Location(value=Kind.LINE, line=-1)
    )
    public static void online(@ProbeClassName String pcn, @ProbeMethodName String pmn, int line) {
        print(pcn + "." + pmn +  ":" + line);
    }

    /**
     * 谁调用了gc
     */
    @OnMethod(clazz = "java.lang.System", method = "gc")
    public static void onSystemGC() {
        println("entered System.gc()");
        jstack();// print the stack info.
    }


    @OnMethod(
            clazz="java.net.ServerSocket",
            method="<init>"   // <init> 代表构造函数
    )
    public static void onServerSocket(@Self ServerSocket self,
                                      int p, int backlog, InetAddress bindAddr) {
        port = p;
        inetAddr = bindAddr;
    }
    @TLS private static int port = -1;
    @TLS private static InetAddress inetAddr;
    @TLS private static SocketAddress sockAddr;
}
