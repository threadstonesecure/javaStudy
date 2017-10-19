package dlt.study.concurrent.error;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dlt.infrastructure.ThreadOut;

/**
 * 
 * @author dlt
 *
 */
class Athlete implements Runnable {
    private final int id;
    private Game game;

    public Athlete(int id, Game game) {
      this.id = id;
      this.game = game;
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

    public void run() {
      try {
        game.prepareOk(this);
      } catch (InterruptedException e) {
        System.out.println(this + " quit the game");
      }
    }
  }

public class Game implements Runnable {
    private Set<Athlete> players = new HashSet<Athlete>();
    private boolean start = false;

    public void addPlayer(Athlete one) {
      players.add(one);
    }

    public void removePlayer(Athlete one) {
      players.remove(one);
    }

    public Collection<Athlete> getPlayers() {
      return Collections.unmodifiableSet(players);
    }

    public void prepare(Athlete athlete) throws InterruptedException {
      System.out.println(athlete + " ready!");
      synchronized (this) {
        while (!start)
        wait();
    
        if (start){  //这儿是错误的，并没有并行跑步 。方法 prepareOk才是对的 
          ThreadOut.println(athlete + " begin going!");
          Thread.sleep(3); //模拟跑了3秒 
          ThreadOut.println(athlete + " finish going !");
        }
      }
    }
    
    public void prepareOk(Athlete athlete) throws InterruptedException {
        System.out.println(athlete + " ready!");
        synchronized (this) {
          while (!start)
          wait();
        }
          if (start){  //这儿是错误的，并没有并行跑步 
            ThreadOut.println(athlete + " begin going!");
            Thread.sleep(3000); //模拟跑了3秒 
            ThreadOut.println(athlete + " finish going !");
          }
        
      }

    public synchronized void go() {
      notifyAll();
    }
    
    public void ready() {
      Iterator<Athlete> iter = getPlayers().iterator();
      while (iter.hasNext())
        new Thread(iter.next()).start();
    }

    public void run() {
      start = false;
      System.out.println("Ready......");
      System.out.println("Ready......");
      System.out.println("Ready......");
      ready();
      start = true;
      System.out.println("Go!");
      go();
    }

    public static void main(String[] args) {
      Game game = new Game();
      for (int i = 0; i < 10; i++)
        game.addPlayer(new Athlete(i, game));
      new Thread(game).start();
    }
}