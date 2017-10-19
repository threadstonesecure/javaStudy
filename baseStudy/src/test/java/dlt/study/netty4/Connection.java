package dlt.study.netty4;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Connection {

	private Channel channel;
	public Connection(Channel channel) {
		this.channel = channel;
		SocketAddress socketAddress = channel.remoteAddress();
		if (socketAddress instanceof InetSocketAddress) {
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
			System.out.println(inetSocketAddress.getHostName() + ":"
					+ inetSocketAddress.getPort());
			System.out.println("InetAddress:" + inetSocketAddress.getAddress());
		}
	}

	public void write(Object msg) {
		channel.write(msg);
	}
}
