package dlt.study.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class BlockClient {
	private SocketChannel channel;

	public void initClient(String ip, int port) throws IOException {
		// 获得一个Socket通道
		channel = SocketChannel.open();
		channel.connect(new InetSocketAddress(ip, port));	
	}
	
	public void write(ByteBuffer info) throws IOException {
		channel.write(info);
	}
	
	public void read() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		channel.read(buffer);
		byte[] data = buffer.array();
		String msg = new String(data).trim();
		System.out.println("收到消息：" + msg);
	}
	
	public static void main(String[] args) throws IOException {
		BlockClient client = new BlockClient();
		client.initClient("127.0.0.1", 8000);

		byte inBuffer[] = new byte[100];
		ByteBuffer dbuf = ByteBuffer.allocateDirect(1024);
		while (true) {
			System.in.read(inBuffer);
			System.out.println("发送服务 :"+ new String(inBuffer));
			dbuf.clear();
			dbuf.put(inBuffer);
			dbuf.flip();
			client.write(dbuf);
			client.read();
		}
	}
}
