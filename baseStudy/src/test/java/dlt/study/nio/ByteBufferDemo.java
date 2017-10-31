package dlt.study.nio;

import org.junit.Test;
import sun.misc.SharedSecrets;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

public class ByteBufferDemo {


    /**
     * 堆外内存  -XX:MaxDirectMemorySize ( default 0 and the VM selects the maximum size of direct memory.)
     */
    @Test
    public void allocateDirect() throws Exception{
        long maxDirectMemory = sun.misc.VM.maxDirectMemory();
        System.out.println(maxDirectMemory/1024/1024);
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed()/1024/1024);
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getTotalCapacity()/1024/1024);
        int i  = 1024*8*1024*1024;
        System.out.println(i);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(5*1024*1024);
        System.out.println(byteBuffer.getClass());
        System.out.println(byteBuffer.isDirect());
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed()/1024/1024);
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getTotalCapacity()/1024/1024);

        if (byteBuffer.isDirect()) {
            ((DirectBuffer)byteBuffer).cleaner().clean();
        }

        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed()/1024/1024);
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getTotalCapacity()/1024/1024);

        System.in.read();



    }
}
