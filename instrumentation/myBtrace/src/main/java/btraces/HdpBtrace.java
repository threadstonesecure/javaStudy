package btraces;


import com.sun.btrace.annotations.*;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static com.sun.btrace.BTraceUtils.*;
import static com.sun.btrace.BTraceUtils.printFields;

@BTrace
public class HdpBtrace {

    @OnMethod(
            clazz = "+java.nio.channels.SocketChannel",
            method = "write",
            location = @Location(value = Kind.RETURN))
    public static void channelWrite(ByteBuffer byteBuffer,
                                    @Self Object target,
                                    @ProbeClassName String className,
                                    @ProbeMethodName String method,
                                    @Duration long duration) {
        if (byteBuffer instanceof DirectBuffer) {
            println(className + "." + method + " -> parms:" + str(byteBuffer));
            println("duration:" + duration / 1000000 + " ms");  // duration 纳秒
            println("self:" + classOf(target));
            printFields(byteBuffer);
            println("====================================");
        }

    }

    @OnMethod(
            clazz = "+java.nio.channels.SocketChannel",
            method = "read",
            location = @Location(value = Kind.RETURN))
    public static void channelRead(ByteBuffer byteBuffer,
                                   @Self Object target,
                                   @ProbeClassName String className,
                                   @ProbeMethodName String method,
                                   @Duration long duration) {
        println(className + "." + method + " -> parms:" + str(byteBuffer));
        println("duration:" + duration / 1000000 + " ms");
        println("self:" + classOf(target));
        printFields(byteBuffer);
        println("====================================");

    }
}
