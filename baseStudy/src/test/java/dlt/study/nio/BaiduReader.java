package dlt.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class BaiduReader {

	private Charset charset = Charset.forName("UTF-8");// 创建GBK字符集 UTF-8
	private SocketChannel channel;

	public void readHTMLContent() {
		try {
			InetSocketAddress socketAddress = new InetSocketAddress(
					"www.baidu.com", 80);
			// step1:打开连接
			channel = SocketChannel.open(socketAddress);
			// step2:发送请求，使用GBK编码
			channel.write(charset.encode("GET " + "/ HTTP/1.1" + "\r\n\r\n"));
			// channel.write(charset.encode("GET"));
			// step3:读取数据
			ByteBuffer buffer = ByteBuffer.allocate(1024);// 创建1024字节的缓冲
			while (channel.read(buffer) >0 ) {
				buffer.flip();// flip方法在读缓冲区字节操作之前调用。
				System.out.println(charset.decode(buffer));
				// 使用Charset.decode方法将字节转换为字符串
				buffer.clear();// 清空缓冲
			}
		} catch (IOException e) {
			System.err.println(e.toString());
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new BaiduReader().readHTMLContent();
/*		URL url = new URL("http://www.baidu.com:80");
		System.out.println(url.getHost());
		System.out.println(url.getPort());*/

	}
}