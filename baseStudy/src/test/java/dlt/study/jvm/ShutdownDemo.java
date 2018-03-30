package dlt.study.jvm;

import org.junit.Test;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class ShutdownDemo {

    /**
     * 在进程被kill -15 的时候main函数就已经结束了，仅会运行shutdownHook中run()方法的代码。
     * @throws Exception
     */
    @Test
    public void addShutdownHoo() throws Exception {


        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("run second shutdownHook !");
            }
        }));

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("run first shutdownHook !");
            }
        })); // 后加入的先运行
        Thread.currentThread().join();  // kill -15 时 先结束main thread，再执行Hook中的run()方法
        System.out.println("main end!"); // 不会运行
    }

    /**
     * SingalHandler函数会在进程被kill时收到TERM信号，对main函数的运行不会有任何影响
     * 拦截信号，用自定义SignalHander处理，需要自己主动关闭
     *
     * @throws Exception
     */
    @Test
    public void singal() throws Exception {
        Signal signalKill = new Signal("KILL");
        Signal signalTerm = new Signal("TERM");
        //    Signal.handle(signalKill,new SystemShutdown());
        SignalHandler prevHandler = Signal.handle(signalTerm, new SystemShutdown());
        System.out.println("TREM的原SignalHandler："+ prevHandler.getClass());
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
        synchronized (ShutdownDemo.class) {
            ShutdownDemo.class.notify();
        }
    }
}