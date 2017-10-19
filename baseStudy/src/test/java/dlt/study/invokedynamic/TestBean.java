package dlt.study.invokedynamic;

public class TestBean {

	private String name;

	public TestBean() {

	}

	public TestBean(String str) {
		name = str;
	}

	public int sum(int a, int b, int... c) {
		int sum = a + b;
		for (int v : c) {
			sum = sum + v;
		}
		return sum;
	}

	public int sum2(int... c) {
		int sum = 0;
		for (int v : c) {
			sum = sum + v;
		}
		return sum;
	}

	public static int sum3(int... c) {
		int sum = 0;
		for (int v : c) {
			sum = sum + v;
		}
		return sum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
