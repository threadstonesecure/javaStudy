package dlt.study.dubbo;

import dlt.utils.spring.SpringContextUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class DubboServerDemo {

    @BeforeClass
    public static void init() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.out.println("init");
    }

    @Test
    public void server() throws Exception {
        String[] paths = new String[]{"dubbotest/*-server.xml"};
        SpringContextUtils.init(paths);
        System.out.println("dubbo server start");
        Thread.currentThread().join();
    }

    @Test
    public void server2() throws Exception {
        String[] paths = new String[]{"dubbotest/*-server2.xml"};
        SpringContextUtils.init(paths);
        System.out.println("dubbo server start");
        Thread.currentThread().join();
    }

    /**
     广播地址绑定失败:Can't assign requested address mac dubbo
     <dubbo:registry address="multicast://224.5.6.7:1234"/>


     This was caused by an IPv6 address being returned from java.net.NetworkInterface.getDefault(). I'm on a Macbook and was using wireless -- p2p0 (used for AirDrop) was returned as the default network interface but my p2p0 only has an IPv6 ether entry [found by running ipconfig].

     Two solutions, both of which worked for me (I prefer the first because it works whether you are using a wired or wireless connection)

     Start the JVM with -Djava.net.preferIPv4Stack=true. This caused java.net.NetworkInterface.getDefault() to return my vboxnet0 network interface -- not sure what you'll get if you're not running a host-only VM.
     Turn off wireless and use a wired connection
     * @throws Exception
     */
    @Test
    public void networkInterface() throws Exception {
        InetAddress mutilcastAddress = InetAddress.getByName("224.5.6.7");
        MulticastSocket mutilcastSocket = new MulticastSocket(1234);
        mutilcastSocket.setLoopbackMode(false);
        mutilcastSocket.joinGroup(mutilcastAddress);
        System.out.println(mutilcastAddress);
    }
}
