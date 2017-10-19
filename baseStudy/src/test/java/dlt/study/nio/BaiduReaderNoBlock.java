package dlt.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class BaiduReaderNoBlock {

	private Charset charset = Charset.forName("UTF-8");// 创建GBK字符集 UTF-8

	private Selector selector;


	public BaiduReaderNoBlock() throws IOException {
		selector = Selector.open();
		InetSocketAddress socketAddress = new InetSocketAddress(
				"www.baidu.com", 80);
		// step1:打开连接
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		channel.connect(socketAddress);
		channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
	}

	public void readHTMLContent() throws IOException {
		boolean finish= false;
		while (finish == false) {
			selector.select();
			Iterator<SelectionKey> its = selector.selectedKeys().iterator();
			
			while (its.hasNext()) {
				SelectionKey key = (SelectionKey) its.next();
				its.remove();
				if (key.isValid() && key.isConnectable()) {
					SocketChannel channel = (SocketChannel) key.channel();

					if (channel.finishConnect()) {
						System.out.println("连接上服务器");
						channel.register(selector, SelectionKey.OP_READ);
						channel.write(charset.encode("GET " + "/ HTTP/1.1"
								+ "\r\n\r\n"));
						

				} else {
						System.out.println("连接服务器失败 ");
						key.cancel(); //取消 selector对channel的管理
						finish = true;
					}
				}

				if (key.isValid() && key.isReadable()) {
					SocketChannel channel = (SocketChannel) key.channel();
					ByteBuffer buffer = ByteBuffer.allocate(1024);// 创建1024字节的缓冲
					
					while (channel.read(buffer) >0 ) {
						buffer.flip();// flip方法在读缓冲区字节操作之前调用。
						System.out.println(charset.decode(buffer));// 使用Charset.decode方法将字节转换为字符串
						buffer.clear();// 清空缓冲
					}
					finish = true;
				}

			}

		}
		System.out.println("finish");
	}

	public static void main(String[] args) throws Exception {
		BaiduReaderNoBlock reader = new BaiduReaderNoBlock();
		reader.readHTMLContent();

	}
}