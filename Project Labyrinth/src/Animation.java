import java.awt.image.BufferedImage;


public class Animation {
	private final long nano=1000000L;
	private int next_frame=0;
	private int max_index=0;
	
	public BufferedImage[] animation=null;
	public int index=0;
	
	public Animation(int max, int next){
		max_index=max;
		next_frame=next;
		animation=new BufferedImage[max];
	}
	public void increaseIndex(long last_update){
		if((System.nanoTime()-last_update)/nano>next_frame){
			index++;
			if(index==max_index)
			index=0;
			Character.last_animation_update=System.nanoTime();
		}
	}
}
