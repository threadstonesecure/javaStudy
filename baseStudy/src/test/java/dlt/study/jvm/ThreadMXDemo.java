package dlt.study.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ThreadMXDemo {

    public static void main(String[] args) {
        System.out.println("hello world!");

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        for (ThreadInfo threadInfo : threadMXBean.dumpAllThreads(true, true)) {
            System.out.println(threadInfo);
        }

        // threadMXBean.findDeadlockedThreads()
       //  threadMXBean.findMonitorDeadlockedThreads()
    }
}
