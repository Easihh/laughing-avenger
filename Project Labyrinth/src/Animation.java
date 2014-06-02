import java.awt.image.BufferedImage;


public class Animation {
	public int index=0;
	private int max_index=0;
	private final long nano=1000000L;
	public BufferedImage[] animation=null;
	private int next_frame=0;
	public Animation(int max, int next){
		max_index=max;
		next_frame=next;
	}
	public void increaseIndex(long last_update){
		if((System.nanoTime()-last_update)/nano>next_frame){
			index++;
			if(index==max_index)
			index=0;
			Character.last_animation_update=System.nanoTime();
		}
		//System.out.println((System.nanoTime()-last_update)/nano);
	}
}
