package dlt.study.nio.file;

import com.google.common.collect.Maps;
import org.junit.Test;
import sun.misc.SharedSecrets;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

/**
 * 在传统的文件IO操作中，我们都是调用操作系统提供的底层标准IO系统调用函数  read()、write() ，
 * 此时调用此函数的进程（在JAVA中即java进程）由当前的用户态切换到内核态，然后OS的内核代码负责将相应的文件数据读取到内核的IO缓冲区，
 * 然后再把数据从内核IO缓冲区拷贝到进程的私有地址空间中去，这样便完成了一次IO操作
 * <p>
 * 内存映射文件和标准IO操作最大的不同之处就在于它虽然最终也是要从磁盘读取数据，但是它并不需要将数据读取到OS内核缓冲区，
 * 而是直接将进程的用户私有地址空间中的一部分区域与文件对象建立起映射关系，就好像直接从内存中读、写文件一样，速度当然快了。
 * 内存映射文件能让你创建和修改那些因为太大而无法放入内存的文件。
 * <p>
 * 有了内存映射文件，你就可以认为文件已经全部读进了内存，然后把它当成一个非常大的数组来访问。
 * 这种解决办法能大大简化修改文件的代码。
 * fileChannel.map(FileChannel.MapMode mode, long position, long size)将此通道的文件区域直接映射到内存中。
 * 注意，你必须指明，它是从文件的哪个位置开始映射的，映射的范围又有多大；也就是说，它还可以映射一个大文件的某个小片断。
 * <p>
 * 不能超过2G
 */

/**
 * java中提供了3种内存映射模式，即：只读(readonly)、读写(read_write)、专用(private) ，
 * 只读模式来说，如果程序试图进行写操作，则会抛出ReadOnlyBufferException异常；
 * 第二种的读写模式表明了通过内存映射文件的方式写或修改文件内容的话是会立刻反映到磁盘文件中去的，
 *  别的进程如果共享了同一个映射文件，那么也会立即看到变化！
 *  最后一种专用模式采用的是OS的“写时拷贝”原则，即在没有发生写操作的情况下，多个进程之间都是共享文件的同一块物理内存（进程各自的虚拟地址指向同一片物理地址），
 *  一旦某个进程进行写操作，那么将会把受影响的文件数据单独拷贝一份到进程的私有缓冲区中，不会反映到物理文件中去。
 */
public class MemoryMappedFileDemo {

    private int length = Integer.MAX_VALUE; // 128 Mb
    private String fileName = "/tmp/howtodoinjava.dat";
    private final int BUFFER_SIZE = 1024;

    @Test
    public void max() {
        System.out.println(Integer.MAX_VALUE / 1024 / 1024);
    }

    @Test
    public void write() throws Exception {
        MappedByteBuffer out = new RandomAccessFile(fileName, "rw")
                .getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < length; i++) {
            out.put((byte) 'a');
        }
        long end = System.currentTimeMillis();
        System.out.println("time is:" + (end - begin));
        System.out.println("Finished writing");
    }

    @Test
    public void write2() throws Exception {
        MappedByteBuffer out = new RandomAccessFile(fileName, "rw")
                .getChannel().map(FileChannel.MapMode.READ_WRITE, length + 20, length);
        for (int i = 0; i < length; i++) {
            out.put((byte) 'm');
        }

        System.out.println("Finished writing");
    }

    @Test
    public void read() throws Exception {

        //Get file channel in readonly mode
        FileChannel fileChannel = new RandomAccessFile(fileName, "r").getChannel();

        //Get direct byte buffer access using channel.map() operation
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

        // the buffer now reads the file as if it were loaded in memory.
        // System.out.println(buffer.isLoaded());  //prints false
        System.out.println(buffer.capacity());  //Get the size based on content size of file

        memory();
        //You can read the file from this buffer the way you like.
        byte[] b = new byte[BUFFER_SIZE];
        long begin = System.currentTimeMillis();
        int len = buffer.capacity();
        for (int offset = 0; offset < len; offset += 1024) {
            if (len - offset > BUFFER_SIZE) {
                buffer.get(b);
            } else {
                buffer.get(new byte[len - offset]);
                break; // 文件2g时，offset + 1024 溢出
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("time is:" + (end - begin)); //time is:1447
        memory();
    }

    @Test
    public void normal() throws Exception {
        FileChannel fileChannel = new RandomAccessFile(fileName, "r").getChannel();
        ByteBuffer buff = ByteBuffer.allocate(BUFFER_SIZE);
        memory();
        long begin = System.currentTimeMillis();
        while (fileChannel.read(buff) != -1) {
            buff.flip();
            buff.clear();
        }
        long end = System.currentTimeMillis();
        System.out.println("time is:" + (end - begin));  // time is:3981
        memory();
    }

    @Test
    public void memory() {
        HashMap<String, String> memoryInfo = Maps.newLinkedHashMap();
        memoryInfo.put("direct.max", "" + sun.misc.VM.maxDirectMemory() / 1024 / 1024);
        memoryInfo.put("direct.used", "" + SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed() / 1024 / 1024);
        memoryInfo.put("direct.capacity", "" + SharedSecrets.getJavaNioAccess().getDirectBufferPool().getTotalCapacity() / 1024 / 1024);

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long allocatedMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = allocatedMemory - freeMemory;
        long totalFreeMemory = freeMemory + maxMemory - allocatedMemory;
        memoryInfo.put("maxMemory", maxMemory + "M");
        memoryInfo.put("allocatedMemory", allocatedMemory + "M");
        memoryInfo.put("freeMemory", freeMemory + "M");
        memoryInfo.put("usedMemory", usedMemory + "M");
        memoryInfo.put("totalFreeMemory", totalFreeMemory + "M");
        System.out.println(memoryInfo);
    }
}
