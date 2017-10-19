package dlt.study.concurrent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Game implements Runnable {
	private Set<Athlete> players = new HashSet<Athlete>();
	private SameTime sync;
	
    public Game(SameTime sync){
    	this.sync = sync;
    }

	public void addPlayer(Athlete one) {
		players.add(one);
	}

	public void removePlayer(Athlete one) {
		players.remove(one);
	}

	public Collection<Athlete> getPlayers() {
		return Collections.unmodifiableSet(players);
	}


	public void ready() {
		Iterator<Athlete> iter = getPlayers().iterator();
		while (iter.hasNext()) {
			new Thread(iter.next()).start();
		}
	}

	@Override
	public void run() {
		System.out.println("Ready......");
		System.out.println("Ready......");
		System.out.println("Ready......");
		ready();
		sync.begin();
	}

}
