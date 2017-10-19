package dlt.study.invokedynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class TestJdk7 {
	final static long N = 10000;
	private static MethodHandle mh;
	private static Method m;
	static {
		try{
		 m = String.class.getMethod("substring", int.class, int.class);
		 mh = MethodHandles.lookup().unreflect(m);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			long t = System.currentTimeMillis();
			for (int i = 0; i < N; i++) {
				bindTo();
			}
			System.out.println("t1: " + (System.currentTimeMillis() - t));

			t = System.currentTimeMillis();
			for (int i = 0; i < N; i++) {
				reflect();
			}
			System.out.println("t2: " + (System.currentTimeMillis() - t));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void bindTo() throws Throwable {

		String str = (String) mh.invoke("Hello World", 3, 5);
		// System.out.println(str);
	}

	public static void reflect() throws Exception {
		
		String str = (String) m.invoke("Hello World", 3, 5);
		// System.out.println(str);
	}

}
