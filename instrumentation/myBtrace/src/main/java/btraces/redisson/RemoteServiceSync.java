package btraces.redisson;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.identityStr;
import static com.sun.btrace.BTraceUtils.println;


@BTrace
public class RemoteServiceSync {

    @TLS
    private static long starttime;

    @OnMethod(
            clazz = "/com\\.yuntai\\.redisson\\.BaseRemoteService.*/",
            method = "/.*/",
            location = @Location(value = Kind.SYNC_ENTRY, where = Where.BEFORE)
    )
    public static void BeforeOnSyncEntry(@Self Object obj,
                                         @ProbeClassName String className,
                                         @ProbeMethodName String method) {
        starttime = BTraceUtils.timeMillis();
        printlnWithThread(className + "." + method + "  -> " + "before synchronized entry: " + identityStr(obj));
    }

    @OnMethod(
            clazz = "/com\\.yuntai\\.redisson\\.BaseRemoteService.*/",
            method = "/.*/",
            location = @Location(value = Kind.SYNC_ENTRY, where = Where.AFTER)
    )
    public static void afterOnSyncEntry(@Self Object obj,
                                        @ProbeClassName String className,
                                        @ProbeMethodName String method) {
        printlnWithThread(className + "." + method + "  -> " + "after synchronized entry: " + identityStr(obj) + " -> Duration: " + (BTraceUtils.timeMillis() - starttime) + "ms");
        starttime = BTraceUtils.timeMillis();
    }


    @OnMethod(
            clazz = "/com\\.yuntai\\.redisson\\.BaseRemoteService.*/",
            method = "/.*/",
            location = @Location(Kind.SYNC_EXIT)
    )
    public static void onSyncExit(@Self Object obj,
                                  @ProbeClassName String className,
                                  @ProbeMethodName String method) {
        printlnWithThread(className + "." + method + "  -> " + "before synchronized exit: " + identityStr(obj) + " -> Duration: " + (BTraceUtils.timeMillis() - starttime) + "ms");
    }


    public static void printlnWithThread(String msg) {
        println("" + BTraceUtils.timeMillis() + " -> " + BTraceUtils.currentThread() + "->" + msg);
    }

}
