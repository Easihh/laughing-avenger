import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Vector;


public class Animation {
	private final long nano=1000000L;
	private int next_frame=0;
	private int max_index=0;
	Vector<Scene> myScene;
	public BufferedImage[] animation=null;
	public int index;
	public long total_time;
	public long last_update;
	public Animation(){
		myScene=new Vector<Scene>();
		index=0;
		total_time=0;
		last_update=System.nanoTime();
	}
	public void AddScene(Image img, long duration){
		myScene.add(new Scene(img,duration));
		total_time+=duration;
	}
	public Image getImage(){
		return myScene.get(index).img;
	}
	public void setImage() {
		myScene.get(index).current_duration+= (System.nanoTime()-last_update)/nano;
		if(myScene.get(index).current_duration>myScene.get(index).max_duration){
			myScene.get(index).current_duration=0;
			index++;
		}
		if(index>myScene.size()-1)
			index=0;
		last_update=System.nanoTime();
	}
	public void reset(){
		myScene.get(index).current_duration=0;
		index=0;
	}
	private class Scene{
		Image img;
		long current_duration;
		long max_duration;
		public Scene(Image img, long duration){
			current_duration=0;
			this.img=img;
			max_duration=duration;
		}
	}
}
