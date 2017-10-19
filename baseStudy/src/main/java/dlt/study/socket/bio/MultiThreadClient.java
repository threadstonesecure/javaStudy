package dlt.study.socket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadClient {

	public static void main(String[] args) {
		int numTasks = 10;

		ExecutorService exec = Executors.newCachedThreadPool();

		for (int i = 0; i < numTasks; i++) {
			exec.execute(createTask(i));
		}
		exec.shutdown();
	}

	// 定义一个简单的任务
	private static Runnable createTask(final int taskID) {
		return new Runnable() {
			private Socket socket = null;
			private int port = 8821;

			public void run() {
				System.out.println("Task " + taskID + ":start");
				PrintWriter pw = null;
				BufferedReader br = null;
				try {
					socket = new Socket("localhost", port);
					System.out.println("LocalPort:" + socket.getLocalPort());
					// 发送关闭命令
					pw = new PrintWriter(socket.getOutputStream(), true);
					pw.println("Hello");
					// 接收服务器的反馈
					br = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					String msg = null;
					// while ((msg = br.readLine()) != null)
					msg = br.readLine();
					System.out.println(msg);
					pw.println("bye");
					System.out.println("finish ");

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						br.close();
						pw.close();
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		};
	}
}