package dlt.study.jvm;

import org.junit.Test;
import sun.misc.SharedSecrets;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class MemoryMxDemo {
    /**
     * 堆外内存  -XX:MaxDirectMemorySize=10M ( default 0 and the VM selects the maximum size of direct memory.)
     */
    @Test
    public void allocateDirect() throws Exception {
        long maxDirectMemory = sun.misc.VM.maxDirectMemory();
        System.out.println(maxDirectMemory / 1024 / 1024);
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed() / 1024 / 1024);
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getTotalCapacity() / 1024 / 1024);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10 * 1024 * 1024);
        System.out.println(byteBuffer.getClass());
        System.out.println(byteBuffer.isDirect());
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed() / 1024 / 1024);
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getTotalCapacity() / 1024 / 1024);

        System.out.println("freeMemory" + Runtime.getRuntime().freeMemory() / 1024 / 1024);
        System.out.println("maxMemory" + Runtime.getRuntime().maxMemory() / 1024 / 1024);
        System.out.println("totalMemory" + Runtime.getRuntime().totalMemory() / 1024 / 1024);
/*        byteBuffer = null;
        System.gc();*/
        ByteBuffer.allocateDirect(1 * 1024 * 1024); // java.lang.OutOfMemoryError: Direct buffer memory
        System.out.println("start");
        Thread.sleep(1000);
/*        if (byteBuffer.isDirect()) {
            ((DirectBuffer) byteBuffer).cleaner().clean();
        }*/

        System.out.println("end");

        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed() / 1024 / 1024);
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getTotalCapacity() / 1024 / 1024);

/*        System.out.println("env");
        System.getenv().forEach((k, v) -> System.out.println(k + "->" + v));
        System.out.println("Properties");
        System.getProperties().forEach((k, v) -> System.out.println(k + "->" + v));*/

        System.out.println("freeMemory=" + Runtime.getRuntime().freeMemory() / 1024 / 1024);
        System.out.println("maxMemory=" + Runtime.getRuntime().maxMemory() / 1024 / 1024);
        System.out.println("allocatedMemory=" + Runtime.getRuntime().totalMemory() / 1024 / 1024);
        System.in.read();


    }

    @Test
    public void memoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        sb.append("Free memory: ");
        sb.append(format.format(freeMemory / 1024 / 1024));
        sb.append("\n");
        sb.append("Allocated memory: ");
        sb.append(format.format(allocatedMemory / 1024 / 1024));
        sb.append("\n");
        sb.append("Max memory: ");
        sb.append(format.format(maxMemory / 1024 / 1024));
        sb.append("\n");
        sb.append("Total free memory: ");
        sb.append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024 / 1024));
        sb.append("\n");

        System.out.println(sb.toString());
    }

    /**
     * 查看 Heap 和 非Heap
     */

    @Test
    public void memoryMXBean() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        // memoryMXBean.gc();
        System.out.println("HeapMemory:" + memoryMXBean.getHeapMemoryUsage());
        System.out.println("NonHeapMemory:" + memoryMXBean.getNonHeapMemoryUsage());
/*
        HeapMemory:init = 268435456(262144K) used = 24174448(23607K) committed = 257425408(251392K) max = 3817865216(3728384K)
        NonHeapMemory:init = 2555904(2496K) used = 8208720(8016K) committed = 8847360(8640K) max = -1(-1K)
*/
    }


    /**
     * 查看 Heap 和 非Heap 各内存结构
     */
    @Test
    public void memoryPoolMXBean() {
        System.out.println("============MemoryPoolMXBean=========");
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

        memoryPoolMXBeans.forEach(memoryPoolMXBean -> {
            System.out.println("==============");
            // System.out.println(memoryPoolMXBean);
            System.out.println("name:" + memoryPoolMXBean.getName());
            System.out.println("type:" + memoryPoolMXBean.getType());
            System.out.println("usage:" + memoryPoolMXBean.getUsage());
            //System.out.println(memoryPoolMXBean.getPeakUsage());
        });
    }

    @Test
    public void memoryManagerMXBean() {
        System.out.println("=====MemoryManagerMXBean======");
        List<MemoryManagerMXBean> memoryManagerMXBeans = ManagementFactory.getMemoryManagerMXBeans(); // 内存管理者
        memoryManagerMXBeans.forEach(mm -> {
            System.out.println("==========");
            System.out.println(mm);
            System.out.println(mm.getName());
            System.out.println(Arrays.toString(mm.getMemoryPoolNames()));
            System.out.println(mm.getObjectName());
        });
    }

}
