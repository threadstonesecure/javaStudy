package dlt.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SelectOpen {

	 public static void main(String[] args) throws IOException, InterruptedException {
	      
	            Selector selector = Selector.open();
	       for (int i = 0; i < 3; i++) {
	            SocketChannel channel = SocketChannel.open();
	            channel.configureBlocking(false);
	            channel.connect(new InetSocketAddress("127.0.0.1", 8000));
	            channel.register(selector, SelectionKey.OP_READ);
	      }
	        Thread.sleep(300000);
	       
	    }
}
