package dlt.study.concurrent;

import java.util.Random;

import dlt.infrastructure.ThreadOut;

public class Athlete implements Runnable {

	private final int id;
	private SameTime syncObject;

	public Athlete(int id, SameTime syncObject) {
		this.id = id;
		this.syncObject = syncObject;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Athlete))
			return false;
		Athlete athlete = (Athlete) o;
		return id == athlete.id;
	}

	public String toString() {
		return "Athlete<" + id + ">";
	}

	public int hashCode() {
		return new Integer(id).hashCode();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(new Random().nextInt(5) * 1000); // 模拟这个自身的准备
			ThreadOut.println(this + " ready !");
			if (syncObject.prepare(this)) {
				ThreadOut.println(this + " begin going!");
				int i = new Random().nextInt(5) + 1;
				Thread.sleep(i * 1000); // 模拟跑了i秒
				ThreadOut.println(this + " finish going ! 跑了" + i + "秒");
			}

		} catch (InterruptedException e) {
			System.out.println(this + " quit the game");
		}
	}

}
