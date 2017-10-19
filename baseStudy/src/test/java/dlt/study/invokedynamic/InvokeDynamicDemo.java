package dlt.study.invokedynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class InvokeDynamicDemo {

	public static void main(String[] args) throws Exception, Throwable {
		TestBean mm = new TestBean();
		int sum = mm.sum(1, 2, 3, 4, 5);
		System.out.println(sum);

		// 通过反射实现
		Method m = TestBean.class.getDeclaredMethod("sum", int.class, int.class,
				int[].class);
		System.out.println(m);

		// Integer sum2 = (Integer) m.invoke(mm, 1, 2, 3, 4, 5); //is error

		Integer sum2 = (Integer) m.invoke(mm, 1, 2, new int[] { 3, 4, 5 });
		System.out.println(sum2);

		// 通过动态语言实现
		MethodType mt = MethodType.methodType(int.class, int.class, int.class,
				int[].class);
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle mh = lookup.findVirtual(TestBean.class, "sum", mt);
		sum = (int) mh.invokeExact(mm, 1, 2, new int[] { 3, 4, 5 });
		System.out.println(sum);

		mh = mh.asVarargsCollector(int[].class); // 转换为可变长度参数类型
		sum = (int) mh.invoke(mm, 1, 2, 3, 4, 5);
		System.out.println(sum);

		mh = mh.asCollector(int[].class, 3); // 转换为可变长度参数类型
		sum = (int) mh.invoke(mm, 1, 2, 3, 4, 5);
		System.out.println(sum);

		mh = lookup.findVirtual(TestBean.class, "sum2",
				MethodType.methodType(int.class,int[].class));
		mh = mh.asFixedArity();
		sum = (int) mh.invoke(mm, new int[] { 1, 2, 3, 4, 5 });
		
		System.out.println(sum);
		

	}
}

