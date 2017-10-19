package dlt.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SocketChannelDemo {

	private static void blockingConn() throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		// socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress("www.baidu.com", 80));
		// System.out.println(socketChannel.finishConnect());
		if (socketChannel.isConnected()) {
			System.out.println("blocking连接成功！");
		} else {
			System.out.println("blocking fail！");
		}
		socketChannel.close();
	}
	
	private static void noBlockingConn() throws IOException {
		Selector selector = Selector.open();
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress("www.baidu.com", 80));
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		
		selector.select();
		Set<SelectionKey> sets  =  selector.selectedKeys();
		Iterator<SelectionKey> it = sets.iterator();
        while(it.hasNext()){
        	SelectionKey key = it.next();
        	if (key.isConnectable()){
        		SocketChannel channel = (SocketChannel) key.channel();  
        		boolean success = channel.finishConnect();
        		if (success){
        			System.out.println("noBlocking连接成功！");
        		}else
        			System.out.println("noBblocking fail！");
        		
        	}
        }
		socketChannel.close();
	}

	private static void noBlockingConn2() throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress("www.baidu.com", 80));
		while(!socketChannel.finishConnect()){

		}
		if (socketChannel.isConnected()) {
			System.out.println("noBlocking2连接成功！");
		} else {
			System.out.println("noBblocking2 fail！");
		}
		socketChannel.close();
	}

	public static void main(String[] args) throws IOException {
/*
		blockingConn();
		noBlockingConn();
		noBlockingConn2();*/
		
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress("www.baidu.comx", 80));
	}
}
