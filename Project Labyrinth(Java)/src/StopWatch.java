/**
 * 
 * Utility Class based on StopWatch from C#.Since we need accurate Timers to refresh screen 
 * hence the use of nano even though we are requesting Millis.
 */


public class StopWatch {

	private long startTime;
	
	public StopWatch(){}
	/*** Return the elapsed time in Milli-seconds since last reset*/
	public long elapsedMillis(){
		long endTime=System.nanoTime();	
		return (endTime-startTime)/1000000;
	}
	/*** Start the timer*/
	public void start(){
		startTime=System.nanoTime();
	}
	/*** Reset the timer*/
	public void reset(){
		startTime=System.nanoTime();
	}
}
