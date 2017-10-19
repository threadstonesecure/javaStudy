package dlt.study.invokedynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

import org.junit.Test;



public class Demo {

	/**
	 * 测试 MethodHandle的bindTo
	 * @throws Exception
	 * @throws Throwable
	 */
	@Test
	public void bindTo() throws Exception, Throwable {
		MethodHandle mh = MethodHandles.lookup().findVirtual(String.class,
				"substring",
				MethodType.methodType(String.class, int.class, int.class));
		mh = mh.asType(mh.type().wrap());
		mh = mh.bindTo("Hello World!").bindTo(3);
		String str = (String) mh.invoke(5);
		System.out.println(str);

	}

	/**
	 * 这是个 错误 的 例子 
	 * @throws Exception
	 * @throws Throwable
	 */
	@Test
	public void bindTo2() throws Exception, Throwable {
		MethodType mt = MethodType.methodType(String.class, int.class,
				int.class);
		mt = mt.wrap();
		System.out.println(mt);
		MethodHandle mh = MethodHandles.lookup().findVirtual(String.class,
				"substring", mt);
		mh = mh.bindTo("Hello World!").bindTo(3);
		String str = (String) mh.invoke(5);
		System.out.println(str);

	}

	/**
	 * 测试默认构造函数方法 的调用
	 * @throws Exception
	 * @throws Throwable
	 */
	@Test
	public void findConstructor() throws Exception, Throwable {
		MethodType mt = MethodType.methodType(void.class); // 默认构造函数
		MethodHandle mh = MethodHandles.lookup().findConstructor(
				TestBean.class, mt);
		TestBean mm = (TestBean) mh.invoke();
		int sum = mm.sum2(1, 2, 3, 4, 5);
		System.out.println(sum);
	}

	/**
	 * 测试带参数 的 构造函数方法 的调用
	 * @throws Exception
	 * @throws Throwable
	 */
	@Test
	public void findConstructor2() throws Exception, Throwable {
		MethodType mt = MethodType.methodType(void.class, String.class);
		MethodHandle mh = MethodHandles.lookup().findConstructor(
				TestBean.class, mt);
		TestBean mm = (TestBean) mh.invoke("sss");
		int sum = mm.sum2(1, 2, 3, 4, 5);
		System.out.println(sum);
	}

	/**
	 * 测试 MethodHandle的findVirtual
	 * @throws Throwable
	 */
	@Test
	public void findVirtual() throws Throwable {
		TestBean mm = new TestBean();
		MethodType mt = MethodType.methodType(int.class, int[].class);
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle mh = lookup.findVirtual(TestBean.class, "sum2", mt);
		int sum = (int)mh.invokeExact(mm, new int[] { 1, 2, 3, 4, 5 });
		System.out.println(sum);
		mh = mh.asVarargsCollector(int[].class);
		sum = (int) mh.invoke(mm, 1, 2, 3, 4, 5); // mh.invokeExact(args) is
													// error;
		System.out.println(sum);
	}

	/**
	 * 测试 MethodHandle的findStatic  
	 * @throws Throwable
	 */
	@Test
	public void findStatic() throws Throwable {
		MethodType mt = MethodType.methodType(int.class, int[].class);
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle mh = lookup.findStatic(TestBean.class, "sum3", mt);
		mh = mh.asVarargsCollector(int[].class);
		int sum = (int) mh.invoke(1, 2, 3, 4, 5); // mh.invokeExact(args) is
														// error;
		System.out.println(sum);
	}
	/**
	 * 通过反射获取Field
	 * @throws Exception
	 */
	@Test
	public void reflectField() throws Exception{
		TestBean mm = new TestBean("denglt");
		Field f=  TestBean.class.getDeclaredField("name");  //getField  is error;
		f.setAccessible(true);//Destroy
		String str =(String) f.get(mm);
		System.out.println(str);
		f.set(mm, "denglt2");
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		//lookup.findGetter(TestBean.class, "name", String.class);
	}
	
	@Test
	public void findField_error() throws Throwable{
		TestBean mm = new TestBean("denglt");
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle mh =  lookup.findGetter(TestBean.class, "name", String.class);
		String temp = (String)mh.invoke(mm);
		System.out.println(temp);
		
	}
	
	
	@Test
	public void findField_ok() throws Throwable{
		TestBean mm = new TestBean("denglt");
		Field f=  TestBean.class.getDeclaredField("name");
		MethodHandles.Lookup  lookup = MethodHandles.lookup();
		f.setAccessible(true);
		MethodHandle mh = lookup.unreflectGetter(f);
		String temp = (String)mh.invoke(mm);
		System.out.println(temp);

		
	}
}
