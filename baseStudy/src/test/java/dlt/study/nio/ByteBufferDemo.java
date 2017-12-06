package dlt.study.nio;

import org.junit.Test;
import sun.misc.SharedSecrets;
import sun.nio.ch.DirectBuffer;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.nio.ByteBuffer;
import java.text.NumberFormat;

public class ByteBufferDemo {


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
    public void memoryInfo(){
        Runtime runtime = Runtime.getRuntime();
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        sb.append("Free memory: ");
        sb.append(format.format(freeMemory / 1024 /1024));
        sb.append("\n");
        sb.append("Allocated memory: ");
        sb.append(format.format(allocatedMemory / 1024 /1024));
        sb.append("\n");
        sb.append("Max memory: ");
        sb.append(format.format(maxMemory / 1024/1024));
        sb.append("\n");
        sb.append("Total free memory: ");
        sb.append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024 /1024));
        sb.append("\n");
        sb.append("totalMemory:");

        System.out.println(sb.toString());
    }

/*
    @Test
    public void t(){
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        int availableProcessors = operatingSystemMXBean.getAvailableProcessors();
        long prevUpTime = runtimeMXBean.getUptime();
        long prevProcessCpuTime = operatingSystemMXBean.getProcessCpuTime();
        double cpuUsage;
        try
        {
            Thread.sleep(500);
        }
        catch (Exception ignored) { }

        operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long upTime = runtimeMXBean.getUptime();
        long processCpuTime = operatingSystemMXBean.getProcessCpuTime();
        long elapsedCpu = processCpuTime - prevProcessCpuTime;
        long elapsedTime = upTime - prevUpTime;

        cpuUsage = Math.min(99F, elapsedCpu / (elapsedTime * 10000F * availableProcessors));
        System.out.println("Java CPU: " + cpuUsage);
    }*/
}
