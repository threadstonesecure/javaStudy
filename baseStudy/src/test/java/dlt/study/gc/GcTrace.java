package dlt.study.gc;

/**
 * jvm区域总体分两类: heap区和非heap区。
 *        (1)heap区又分：Eden Space（伊甸园）、Survivor Space(幸存者区)、Tenured Gen（老年代-养老区）。
 *        (2)非heap区又分：Code Cache(代码缓存区)、Perm Gen（永久代）、Jvm Stack(java虚拟机栈)、Local Method Statck(本地方法栈)。
 *
 *   (1)Eden Space (heap)  内存最初从这个线程池分配给大部分对象。
     (2)Survivor Space (heap) 用于保存在eden space内存池中经过垃圾回收后没有被回收的对象。
     (3)Tenured Generation (heap) 用于保持已经在survivor space内存池中存在了一段时间的对象。
     (4)Permanent Generation (non-heap)保存虚拟机自己的静态(reflective)数据，例如类（class）和方法（method）对象。Java虚拟机共享这些类数据。这个区域被分割为只读的和只写的。
     (5)Code Cache (non-heap) HotSpot Java虚拟机包括一个用于编译和保存本地代码（native code）的内存，叫做“代码缓存区”（code cache）。
 *
 * 永久代可以通过-XX:PermSize=512M -XX:MaxPermSize=512M设置  -XX:PermSize：设置永久代(perm gen)初始值。默认值为物理内存的1/64。-XX:MaxPermSize：设置持久代最大值。物理内存的1/4。
 * heap堆内存设置参数：-Xmx20m -Xms20m
 * 堆新生代内存分配设置：-Xmn10m 新生代分配了10M的内存
 * -Xss：每个线程的堆栈大小。JDK5.0以后每个线程堆栈大小为1M,以前每个线程堆栈大小为256K。应根据应用的线程所需内存大小进行适当调整。
 *       在相同物理内存下,减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。
 *       一般小的应用， 如果栈不是很深， 应该是128k够用的，大的应用建议使用256k。这个选项对性能影响比较大，需要严格的测试
 *
 * GC相关参数
 *    1）执行GC时候的日志：-XX:+PrintGCDetails  -XX:+PrintGCTimeStamps
 *    2）设置进入老生代的大小限制：-XX:PretenureSizeThreshold=3M（该设置只对Serial和ParNew收集器生效）
 *    3）-verbose:gc开关可显示GC的操作内容。打开它，可以显示最忙和最空闲收集行为发生的时间、收集前后的内存大小、收集需要的时间
 *    4）指定输出文件 -Xloggc:gc.log
 *    5）-XX:+PrintTenuringDistribution开关了解获得使用期的对象权
 *    6) 新生代：
 *          (1) -XX:MaxTenuringThreshold 在新生代中对象存活次数(经过Minor GC的次数)后仍然存活，就会晋升到旧生代
 *          (2) -XX:SurvivorRatio=4 新生代空间中的Eden区和救助空间Survivor区的大小比值 (调小这个参数将增大survivor区，让对象尽量在survitor区呆长一点，减少进入年老代的对象。)
 *    7)-XX:+PrintGCApplicationStoppedTime(GC消耗了多少时间)
 *    8)-XX:+PrintGCApplicationConcurrentTime(GC之间运行了多少时间)
 *
 *    收集器选择:
 *        1) CMS（Concurrent Mark Sweep）收集器 -XX:+UseConcMarkSweepGC
 　　        CMS是一种以最短停顿时间为目标的收集器，使用CMS并不能达到GC效率最高（总体GC时间最小），但它能尽可能降低GC时服务的停顿时间，这一点对于实时或者高交互性应用（譬如证券交易）来说至关重要
                其他  -XX:+CMSClassUnloadingEnabled -XX:+CMSPermGenSweepingEnabled (CMS收集持久代UnpaidFeeList)
          2) ParNew收集器   -XX:+UseParNewGC  (指定在 New Generation 使用 parallel collector, 是 UseParallelGC 的 gc 的升级版本 , 有更好的性能或者优点 , 可以和 CMS gc 一起使用)
 　　         ParNew收集器就是Serial的多线程版本，除了使用多条收集线程外，其余行为包括算法、STW、对象分配规则、回收策略等都与Serial收集器一摸一样
          3) Parallel Scavenge收集器   jdk1.7.0_79 默认
 　　          Parallel Scavenge收集器（下称PS收集器）也是一个多线程收集器
          4) Serial:  –XX:+UseSerialGC (串行GC)

 * Created by denglt on 2016/5/12.
 */
public class GcTrace {
    static int m = 1024*1024;
    public static void main(String[] args)  throws  Exception {  // -verbose.gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:PretenureSizeThreshold=3M    -XX:+UseConcMarkSweepGC   -XX:+UseParNewGC
        // 可以使用 jstat -gc  看内存使用情况
        Thread.sleep(30000);
        byte[] a1 = new byte[2 * m];
        System.out.println("a1 ok");
        byte[] a2 = new byte[4 * m];
        System.out.println("a2 ok");
        Thread.sleep(10000);
        a1 = null;
        a2 = null;
        System.gc();
        System.in.read();
    }
}
