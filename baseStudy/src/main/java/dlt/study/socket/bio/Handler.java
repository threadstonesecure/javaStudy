package dlt.study.socket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

class Handler implements Runnable {
	private Socket socket;
    private BufferedReader br;
    private PrintWriter pw ;
	public Handler(Socket socket) {
		this.socket = socket;
		try{
		this.pw = new PrintWriter(socket.getOutputStream(), true);
		this.br = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		}catch(IOException e){
			e.printStackTrace();
		}
	}



	public String echo(String msg) {
		return "echo:" + msg;
	}

	public void run() {
		try {
			System.out.println("New connection accepted "
					+ socket.getInetAddress() + ":" + socket.getPort() + ":"
					+ socket.getLocalPort());
			String msg = null;
			while ((msg = br.readLine()) != null) {
				System.out.println(msg);
				pw.println(echo(msg));
				if (msg.equals("bye"))
					break;
			}

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
}