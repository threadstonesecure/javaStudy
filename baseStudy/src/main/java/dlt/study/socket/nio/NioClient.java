package dlt.study.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioClient implements Runnable {
	private Selector selector;
	private SocketChannel channel;

	public void initClient(String ip, int port) throws IOException {
		// 获得一个Socket通道
		channel = SocketChannel.open();
		// 设置通道为非阻塞
		channel.configureBlocking(false);
		// 获得一个通道管理器
		this.selector = Selector.open();

		// 客户端连接服务器,其实方法执行并没有实现连接，需要在listen（）方法中调
		// 用channel.finishConnect();才能完成连接
		channel.connect(new InetSocketAddress(ip, port));
		// 将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件。
		channel.register(selector, SelectionKey.OP_CONNECT);

	}

	public void listen() throws IOException {
		// 轮询访问selector
		int iWrite = 0;
		while (true) {
			int i = selector.select();
			if (i == 0) {
				continue;
			}
			// System.out.println("有" + i + "事件需要处理");
			// 获得selector中选中的项的迭代器
			Iterator ite = this.selector.selectedKeys().iterator();
			while (ite.hasNext()) {
				SelectionKey key = (SelectionKey) ite.next();
				// 删除已选的key,以防重复处理
				ite.remove();
				if (!key.isValid()) {
					continue;
				}
				SocketChannel channel = (SocketChannel) key.channel();

				if (key.isConnectable()) { // 连接事件发生
					if (channel.finishConnect()) {
						SocketAddress rsa = channel.getRemoteAddress();
						String serverIp = null;
						int serverPort = 0;
						if (rsa instanceof InetSocketAddress) {
							InetSocketAddress address = (InetSocketAddress) rsa;
							serverIp = address.getAddress().getHostAddress();
							serverPort = address.getPort();
						}
						SocketAddress lsa = channel.getLocalAddress();
						System.out.println("连接服务器[" + serverIp + ":"
								+ serverPort + "]成功！");
					}
					channel.write(ByteBuffer.wrap(new String(
							"my name is denglt.").getBytes()));
					channel.register(this.selector, SelectionKey.OP_READ); //注册读事件到多路复用器Selector

				} else if (key.isReadable()) { // 读事件发生（即有数据被接收）
					read(key);
				} /*else if (key.isWritable()) {
					channel.write(ByteBuffer
							.wrap(new String("Hello by denglt.").getBytes()));
					// key.interestOps(key.interestOps() &
					// ~SelectionKey.OP_WRITE);
					channel.register(this.selector, SelectionKey.OP_READ);
				}*/

			}

		}
	}

	public void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		// 创建读取的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(100);
		channel.read(buffer);
		byte[] data = buffer.array();
		String msg = new String(data).trim();
		System.out.println("info from Server：" + msg);
	}

	@Override
	public void run() {
		try {
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(ByteBuffer info) throws IOException {
		channel.write(info);
	}

	public static void main(String[] args) throws IOException {
		NioClient client = new NioClient();
		client.initClient("127.0.0.1", 8000);
		new Thread(client).start();

		byte inBuffer[] = new byte[100];
		ByteBuffer dbuf = ByteBuffer.allocateDirect(1024);
		while (true) {
			System.in.read(inBuffer);
			System.out.println("发送服务 ");
			dbuf.clear();
			dbuf.put(inBuffer);
			dbuf.flip();
			client.write(dbuf);
			dbuf.clear();

		}
	}

}
