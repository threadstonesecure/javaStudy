package com.yuntai.hdp.server.net;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description Connection管理类
 * @author denglt
 * @CopyRight: 版权归Hundsun 所有
 */
public class ConnectionManager {

	private static Map<String, Connection> connections = new ConcurrentHashMap<String, Connection>();

	private static String getConnKey(Connection conn) {
		return getConnKey(conn.getRemoteIp(), conn.getRemotePort());
	}

	private static String getConnKey(String ip, int port) {
		return ip + ":" + port;
	}

	public static void addConnection(Connection conn) {
		connections.put(getConnKey(conn), conn);
	}

	public static Connection removeConnection(Connection conn) {
		Connection old = connections.remove(getConnKey(conn));
		return old;
	}

	public static Connection getConnection(String ip, int port) {
		Connection conn = connections.get(getConnKey(ip, port));
		return conn;
	}

	public static Collection<Connection> getAllConnection() {
		return connections.values();

	}
}
