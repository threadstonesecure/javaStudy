package dlt.study.concurrent;

import dlt.utils.UnsafeSupport;
import sun.misc.Unsafe;

public class UnsafeDemo {
	public static void main(String[] args) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException,
			IllegalAccessException, InstantiationException {
		
		Unsafe unsafe =  UnsafeSupport.getInstance();
		Unsafe unsfae2 = Unsafe.getUnsafe();

		// This creates an instance of player class without any initialization
		Player p = (Player) unsafe.allocateInstance(Player.class);
		System.out.println(p.getAge()); // Print 0

		p.setAge(45); // Let's now set age 45 to un-initialized object
		System.out.println(p.getAge()); // Print 45
	}
}

class Player {
	private int age = 12;

	private Player() {
		this.age = 50;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}