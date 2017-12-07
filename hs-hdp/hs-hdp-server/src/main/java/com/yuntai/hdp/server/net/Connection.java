package com.yuntai.hdp.server.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Description 代表一个channel连接
 * @author denglt
 * @CopyRight: 版权归Hundsun 所有
 */
public class Connection {
	Log log = LogFactory.getLog(Connection.class);
	private Channel channel;
	private String remoteIp;
	private int remotePort;
	private String localIp;
	private int localPort;

	private Connection(Channel channel) {
		this.channel = channel;
		SocketAddress socketAddress = channel.remoteAddress();
		if (socketAddress instanceof InetSocketAddress) {
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
			remoteIp = inetSocketAddress.getAddress().getHostAddress();
			remotePort = inetSocketAddress.getPort();
		}

		socketAddress = channel.localAddress();
		if (socketAddress instanceof InetSocketAddress) {
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
			localIp = inetSocketAddress.getAddress().getHostAddress();
			localPort = inetSocketAddress.getPort();
		}
	}

	public static Connection wrap(Channel channel) {
		Connection conn = new Connection(channel);
		return conn;
	}

	public boolean write(Object msg) {
		ChannelFuture f = channel.writeAndFlush(msg).awaitUninterruptibly();
		if (!f.isSuccess()) {
			log.error("发送请求到HtpClient失败：", f.cause());
		}

		return f.isSuccess();
	}

	public void close(){
		channel.close();
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

    public Channel getChannel() {
        return channel;
    }
}
