package dlt.study.jvm;

import org.junit.Test;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class ShutdownDemo {

    @Test
    public void addShutdownHoo() throws Exception {


        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("run two shutdownHook !");
            }
        }));

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("run first shutdownHook !");
            }
        })); // 后加入的先运行
        Thread.currentThread().join();  // 先结束main thread，在执行Hook中的run()方法
        System.out.println("main end!");
    }

    /**
     * 拦截信号，用自定义SignalHander处理，需要自己主动关闭
     *
     * @throws Exception
     */
    @Test
    public void singal() throws Exception {
        Signal signalKill = new Signal("KILL");
        Signal signalTerm = new Signal("TERM");
        //    Signal.handle(signalKill,new SystemShutdown());
        Signal.handle(signalTerm, new SystemShutdown());
        synchronized (ShutdownDemo.class) {
            ShutdownDemo.class.wait();
        }
        System.out.println("main end!");
    }


}

class SystemShutdown implements SignalHandler {
    @Override
    public void handle(Signal signal) {
        System.out.println(signal.getName() + "->" + signal.getNumber());
        // Runtime.getRuntime().exit(0);
        synchronized (ShutdownDemo.class) {
            ShutdownDemo.class.notify();
        }
    }
}