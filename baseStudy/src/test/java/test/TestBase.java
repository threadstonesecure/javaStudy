package test;

import java.io.IOException;

public class TestBase {
	public int print(int a) throws IOException {
		return a;
	}

}

class TestA extends TestBase {
	@Override
	public int print(int a) throws IOException, ArrayIndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return super.print(a);
	}

}