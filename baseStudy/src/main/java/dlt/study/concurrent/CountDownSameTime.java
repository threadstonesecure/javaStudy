package dlt.study.concurrent;

import java.util.concurrent.CountDownLatch;

import javax.management.RuntimeErrorException;

public class CountDownSameTime implements SameTime {

	private CountDownLatch begin = new CountDownLatch(1);
	
	private CountDownLatch readby;
	
	public CountDownSameTime(int i){
		readby = new CountDownLatch(i);
	}
	@Override
	public boolean prepare(Runnable runer) throws InterruptedException {
		readby.countDown();
		begin.await();
		return true;
	}

	@Override
	public void begin() {
		
		try{
		  readby.await();
		 
		}catch(InterruptedException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		System.out.println("Go!");
		begin.countDown();
	}

}
