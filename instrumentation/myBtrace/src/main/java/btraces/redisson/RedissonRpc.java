package btraces.redisson;

import static com.sun.btrace.BTraceUtils.println;

import com.sun.btrace.AnyType;
import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

import static com.sun.btrace.BTraceUtils.*;

@BTrace
public class RedissonRpc {

    @OnMethod(
            clazz = "/org\\.redisson\\..*/",
            method = "/.*/",
            location = @Location(value = Kind.ENTRY))
    public static void redissonClientEntry(AnyType[] args,
                                           @Self Object target,
                                           @ProbeClassName String className,
                                           @ProbeMethodName String method
                                           ) {


        if ((BTraceUtils.strstr(className, "org.redisson.BaseRemoteService") >= 0 && (BTraceUtils.strstr(method, "poll") == 0))
                || (BTraceUtils.strstr(className, "org.redisson.BaseRemoteService$6") >= 0 && BTraceUtils.strstr(method, "operationComplete") == 0)
                || (BTraceUtils.strstr(className, "org.redisson.RedissonRemoteService") >= 0) && (BTraceUtils.strstr(method, "addAsync") == 0)) {
            printlnWithThread(className + "." + method + "  -> entry" );
        }

    }

    @OnMethod(
            clazz = "/org\\.redisson\\..*/",
            method = "/.*/",
            location = @Location(value = Kind.RETURN))
    public static void redissonClientReturn(AnyType[] args,
                                            @Self Object target,
                                            @ProbeClassName String className,
                                            @ProbeMethodName String method,
                                            @Duration long duration) {


        if ((BTraceUtils.strstr(className, "org.redisson.BaseRemoteService") >= 0 && (BTraceUtils.strstr(method, "poll") == 0))
                || (BTraceUtils.strstr(className, "org.redisson.BaseRemoteService$6") >= 0 && BTraceUtils.strstr(method, "operationComplete") == 0)
                || (BTraceUtils.strstr(className, "org.redisson.RedissonRemoteService") >= 0) && (BTraceUtils.strstr(method, "addAsync") == 0)) {
            printlnWithThread(className + "." + method + "  -> duration:" + duration / 1000000 + " ms");
        }

    }

/*    @OnMethod(
            clazz = "+java.nio.channels.SocketChannel",
            method = "write",
            location = @Location(value = Kind.RETURN))
    public static void channelWrite(ByteBuffer byteBuffer,
                                    @Self Object target,
                                    @ProbeClassName String className,
                                    @ProbeMethodName String method,
                                    @Duration long duration) {
        if (byteBuffer instanceof DirectBuffer) {
            printlnWithThread(className + "." + method + " -> parms:" + str(byteBuffer) + " -> duration:" + duration / 1000000 + " ms");
        }

    }*/

/*
    @OnMethod(
            clazz = "+java.nio.channels.SocketChannel",
            method = "read",
            location = @Location(value = Kind.RETURN))
    public static void channelRead(ByteBuffer byteBuffer,
                                   @Self Object target,
                                   @ProbeClassName String className,
                                   @ProbeMethodName String method,
                                   @Duration long duration) {
        printlnWithThread(className + "." + method + " -> parms:" + str(byteBuffer) + " -> duration:" + duration / 1000000 + " ms");
    }
*/

    @OnEvent("print")
    public static void printEvent() {
        println("===========结束======");
    }

    public static void printlnWithThread(String msg) {
        println("" + BTraceUtils.timeMillis() + " -> " + BTraceUtils.currentThread() + "->" + msg);
    }

}

