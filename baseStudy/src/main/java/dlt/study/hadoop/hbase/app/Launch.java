package dlt.study.hadoop.hbase.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.FutureTask;

import dlt.utils.hbase.ByteTo;
import dlt.utils.hbase.dao.HBaseDao;
import dlt.utils.hbase.dao.IHBaseDao;

public class Launch {
	

	public static void main(String[] args) throws ClassNotFoundException,
			InterruptedException {
		if (args.length < 3) {
			throw new RuntimeException("程序参数 错误 ！");
		}
		String urlConn = args[0]; // "jdbc:oracle:thin:@132.121.164.207:1521:gxtest"
		String userName = args[1]; // "gpdi_dlt";
		String password = args[2]; // "denglt"
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = null;
		IHBaseDao hbaseDao = null;
		OracleStat2HBase runner = null;
  
		while (true) {
			try {
				if (conn == null)
					conn = DriverManager.getConnection(urlConn, userName,
							password);
				if (hbaseDao == null)
					hbaseDao = new HBaseDao();
				if (runner == null)
					runner = new OracleStat2HBase(conn, hbaseDao);
				runner.setBatchSize(5000);
				runner.setDebug(true);
				final FutureTask<Integer> task = new FutureTask<Integer>(runner);
				Date date = new Date();
				System.out.println(dateFormat.format(date) + ":开始导入数据");
				new Thread(task).start();
				Integer i = task.get();
				date = new Date();
				System.out
						.println(dateFormat.format(date) + ":成功导入" + i + "记录");

			} catch (Exception e) {
				if (conn != null) {
					try {
						conn.close();

					} catch (Exception ee) {
					}
				}
				conn = null;
				hbaseDao = null;
				runner = null;
				e.printStackTrace();
				int second = 10;
				System.out.println("停止" + second + "秒后重试！");
				Thread.sleep(second * 1000);

			}
		}
	}

	
	public void getData() throws IOException {
		IHBaseDao hbaseDao = new HBaseDao();
		Long l = hbaseDao.getCellValue("oracle_sysstat",
				"20141203145748_132.121.164.207_gxtest", "redo",
				"redo_write_time", ByteTo.Long);
		System.out.println(l);

		l = hbaseDao.getCellValue("oracle_sysstat",
				"20141203145748_132.121.164.207_gxtest", "debug",
				"calls_to_kcmgas", ByteTo.Long);
		System.out.println(l);
	}


	public void deadLoop() throws InterruptedException {
		for (;;) {
			Thread.sleep(1000);
		}
	}
}
