/* Enrico Talbot
 * 
 * Utility Class based on StopWatch from C#.Since we need accurate Timers to refresh screen 
 * hence the use of nano even though we are requesting Millis.
 */


public class StopWatch {

	private long startTime;
	
	public StopWatch(){
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
