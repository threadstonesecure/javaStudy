package dlt.study.zookeeper;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

public class ZookeeperDemo {

	private static ZooKeeper zk;
	static {
		String address = "121.40.225.50:2181"; // "115.29.163.148:2181";
		try {
			zk = new ZooKeeper(address, 60000, new Watcher() {
				// 监控所有被触发的事件
				public void process(WatchedEvent event) {
					System.out.println("已经触发了" + event + "事件！");
				}
			});
		} catch (Exception ex) {

		}
	}

	@Test
	public void simpleTest() throws Exception {
		// 创建一个与服务器的连接

		// zk.delete("/testRootPath", -1);
		// 创建一个目录节点
		zk.create("/testRootPath", "testRootPathData".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

		// 创建一个子目录节点
		zk.create("/testRootPath/testChildPathOne",
				"testChildDataOne".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);

		zk.create("/testRootPath/testChildPathTwo",
				"testChildDataOne".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		//
		System.out
				.println(new String(zk.getData("/testRootPath", false, null)));

		// 取出子目录节点列表
		System.out.println(zk.getChildren("/testRootPath", true));

		// 修改子目录节点数据
		zk.setData("/testRootPath/testChildPathOne",
				"modifyChildDataOne".getBytes(), -1);

		System.out.println("目录节点状态：[" + zk.exists("/testRootPath", true) + "]");

		// 删除子目录节点
		zk.delete("/testRootPath/testChildPathTwo", -1);
		zk.delete("/testRootPath/testChildPathOne", -1);
		// 删除父目录节点
		zk.delete("/testRootPath", -1);
		// 关闭连接
		zk.close();
		System.in.read();
	}

	private static String getSpace(int count) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < count; i++) {
			sb.append(" ");
		}
		if (count > 0) {
			sb.append("|--");
		}
		return sb.toString();
	}

	public void printZookeeper(String root, int level) throws Exception {
		if ((root == null || root.length() == 0) && level == 0) {
			root = "/";
		}
		if (level == 0 && !root.equals("/")) {
			System.out.println(getSpace(level) + root);
			level++;
		}
		List<String> children = zk.getChildren(root, false);
		for (String child : children) {
			System.out.println(getSpace(level) + child);
			printZookeeper(root + (root.endsWith("/") ? "" : "/") + child,
					level + 1);
		}
	}

	@Test
	public void treeData() throws Exception {
		printZookeeper("/dubbo/com.yuntai.hdp.access.service.UpdataHandler", 0);
	}

}
