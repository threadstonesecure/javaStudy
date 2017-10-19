package dlt.study.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NioServer {
	// 通道管理器
	private Selector selector;
	private HandlerWithThreadPool handlerWithThreadPool = new HandlerWithThreadPool();

	private List<SocketChannel> channels = new ArrayList<SocketChannel>();

	/**
	 * 获得一个ServerSocket通道，并对该通道做一些初始化的工作
	 * 
	 * @param port
	 *            绑定的端口号
	 * @throws IOException
	 */
	public void initServer(int port) throws IOException {
		// 获得一个ServerSocket通道
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		// 设置通道为非阻塞
		serverChannel.configureBlocking(false);
		// 将该通道对应的ServerSocket绑定到port端口
		serverChannel.socket().bind(new InetSocketAddress(port));
		// 获得一个通道管理器
		this.selector = Selector.open();
		// 将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
		// 当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
		SelectionKey acceptKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		//acceptKey.attach(obj);
	}

	/**
	 * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void listen() throws IOException {
		System.out.println("服务端启动成功！");
		// 轮询访问selector
		while (true) {
			// 当注册的事件到达时，方法返回；否则,该方法会一直阻塞  或者 selector.wakeup()主动唤醒
			int i = selector.select(); // select()阻塞，等待有事件发生唤醒
			if (i == 0) {
				continue;
			}
			System.out.println("有"+i+"个 事件等待处理 !");
			// 获得selector中选中的项的迭代器，选中的项为注册的事件
			Iterator ite = this.selector.selectedKeys().iterator();
			while (ite.hasNext()) {
				SelectionKey key = (SelectionKey) ite.next();
				// 删除已选的key,以防重复处理
				ite.remove();
				if (!key.isValid()) {
					continue;
				}
				// 客户端请求连接事件
				if (key.isAcceptable()) {
					ServerSocketChannel server = (ServerSocketChannel) key
							.channel();
					// 获得和客户端连接的通道
					SocketChannel channel = server.accept();
                    channel.socket().setReuseAddress(true);
					// 设置成非阻塞
					channel.configureBlocking(false);
					channels.add(channel);
					SelectionKey readKey = channel.register(this.selector, SelectionKey.OP_READ);
					//readKey.attach(ob);
					// 获得了可读的事件
				} else if (key.isReadable()) {

					System.out.println("read :" + key);
					// read(key);
					handlerWithThreadPool.execute(key);  //如果启用多线程处理SelectionKey，因为处理的滞后，
					                                     //再次selector.select()会触发相同的SelectionKey事件
					                                     //这里简单用Thread.sleep(100)处理
					try {
						Thread.sleep(100); 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}

		}
	}

	/**
	 * 处理读取客户端发来的信息 的事件
	 * 
	 * @param key
	 * @throws IOException
	 */
	public void read(SelectionKey key) throws IOException {
		// 服务器可读取消息:得到事件发生的Socket通道
		SocketChannel channel = (SocketChannel) key.channel();
		// 创建读取的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(100);
		int count = channel.read(buffer);
		if (count < 0 ){
			// 客户端已经断开连接.
			key.cancel();
			channel.close();
			channels.remove(channel);
			return;
		}
		byte[] data = buffer.array();
		String msg = new String(data).trim();
		System.out.println("服务端收到信息：" + msg);
		msg = "hello from Server";
		ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
		channel.write(outBuffer);// 将消息回送给客户端
	}



	public void notifyAll(String info) throws IOException {
		System.out.println("向所有客户发送消息：" + info);
		for (SocketChannel channel : channels) {
			if (channel.isConnected()) {
				ByteBuffer outBuffer = ByteBuffer.wrap(info.getBytes());
				channel.write(outBuffer);
			}

		}
	}

	/**
	 * 启动服务端测试
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final NioServer server = new NioServer();
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(60000);
						server.notifyAll("大家好！");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		server.initServer(8000);
		server.listen();

	}
}
