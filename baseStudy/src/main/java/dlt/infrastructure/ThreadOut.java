package dlt.infrastructure;

import java.util.Date;

public class ThreadOut {

	public static void println(String msg) {
		System.out.println(new Date().toString() + Thread.currentThread() + ":" + msg);
	}
}
