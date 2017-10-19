package dlt.study.concurrent;

public interface SameTime   {
	public boolean prepare(Runnable runer) throws InterruptedException;
	
	public void begin() ;
}
