package dlt.study.concurrent;

import org.junit.Test;

public class TestGame {
	
	@Test
	public void testLock() {
		int runnerCount =  10;
		LockSameTime syncSame = new LockSameTime(runnerCount);
		Game game = new Game(syncSame);
		for (int i = 0; i < runnerCount; i++)
			game.addPlayer(new Athlete(i, syncSame));
		game.run();
		
		//LockSupport.park();
	}
	
	@Test
	public void testSync(){
		int runnerCount =  10;
		SyncSameTime syncSame = new SyncSameTime(runnerCount);
		Game game = new Game(syncSame);
		
		for (int i = 0; i < runnerCount; i++)
			game.addPlayer(new Athlete(i, syncSame));
		game.run();
	}
	
	@Test
	public void testCountDown(){
		int runnerCount =  10;
		CountDownSameTime syncSame = new CountDownSameTime(runnerCount);
		Game game = new Game(syncSame);
		for (int i = 0; i < runnerCount; i++)
			game.addPlayer(new Athlete(i, syncSame));
		game.run();
	}
	
	@Test
	public void testCyclicBarrier(){
		int runnerCount =  10;
		CyclicBarrierSameTime syncSame = new CyclicBarrierSameTime(runnerCount);
		Game game = new Game(syncSame);
		
		for (int i = 0; i < runnerCount; i++)
			game.addPlayer(new Athlete(i, syncSame));
		game.run();
	}
	
	
	public static void main(String[] args) {
		TestGame test = new TestGame();
		//test.testLock();
		//test.testSync();
		//test.testCountDown(); 
		test.testCyclicBarrier();
	}
}
