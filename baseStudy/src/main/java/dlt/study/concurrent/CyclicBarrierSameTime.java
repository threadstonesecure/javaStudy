package dlt.study.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierSameTime implements SameTime {

	private CyclicBarrier barrier;

	public CyclicBarrierSameTime(int i) {
		barrier = new CyclicBarrier(i);
	}

	@Override
	public boolean prepare(Runnable runer) throws InterruptedException {
		try {
			barrier.await();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void begin() {
		System.out.println("Go!");
	}

}
