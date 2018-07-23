package btraces;

import com.sun.btrace.AnyType;
import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;

import com.yuntai.hdp.access.RequestPack;

import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class HdpServer2HdpServer {

    @TLS
    private static String hdpSeqno;

    @OnMethod(
            clazz = "com.yuntai.hdp.server.HdpServer2HdpServer",
            method = "getHospitalResult",
            location = @Location(value = Kind.ENTRY))
    public static void getHospitalResultEntry(RequestPack request, int timeout) {
        hdpSeqno = str(get(field("com.yuntai.hdp.access.RequestPack", "hdpSeqno"), request));

    }

    @OnMethod(
            clazz = "com.yuntai.hdp.server.HdpServer2HdpServer",
            method = "getHospitalResult",
            location = @Location(value = Kind.RETURN))
    public static void getHospitalResult(RequestPack request, int timeout,
                                         @Self Object target,
                                         @ProbeClassName String className,
                                         @ProbeMethodName String method,
                                         @Duration long duration) {
        String msg = className + "." + method + "duration:" + duration / 1000000 + " ms";

        printlnWithThread(msg);
        println("====================================");
    }



    public static void printlnWithThread(String msg){
        println(hdpSeqno + ":" + BTraceUtils.currentThread() + "->" + msg);
    }
}
