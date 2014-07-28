package utility;

public class Stopwatch {
	
	private long startTime;
	
	public Stopwatch(){
	}
	public long elapsedMillis(){
		long endTime=System.nanoTime();	
		return (endTime-startTime)/1000000;
	}
	public void start(){
		startTime=System.nanoTime();
	}
	public void reset(){
		startTime=System.nanoTime();
	}
}
