import java.awt.Image;
import java.util.Vector;
/**
 * 
 * Utility Class for the Game.This class decides how animation are built and used.
 * Since an animation is just a series of Image with a certain duration; we build the Scene
 * using subImages of a spriteSheet with a duration for each subImages in order to determine
 * how long each subImage should play out.
 */

public class Animation {
	private final long nano=1000000L;
	private Vector<Scene> myScene;
	/**current Index of the Animation */
	public int index;
	/** Time since the current scene has been changed*/
	public long last_update;
	public Animation(){
		myScene=new Vector<Scene>();
		index=0;
		last_update=System.nanoTime();
	}
	/*** Add a new Scene with Image img and Duration duration to the Scene Array.*/
	public void AddScene(Image img, long duration){
		myScene.add(new Scene(img,duration));
	}
	/*** Return the Image of the current Scene*/
	public Image getImage(){
		return myScene.get(index).img;
	}
	/** Return the Last Scene of an Animation */
	public int getLastIndex(){
		return myScene.size()-1;
	}
	/** Set the Image of the object depending on time elapsed since last scene*/
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
	/*** Delete and replace the Scene Array with a new Scene with Parameters img */
	public void replaceAnimation(Image img){
		index=0;
		myScene.clear();
		myScene.add(new Scene(img,0));
	}
	/*** Delete all the Scene */
	public void reset(){
		myScene.get(index).current_duration=0;
		index=0;
	}
	/*** Return Max Duration of Scene i*/
	public long getSceneMaxDuration(int i){
		return myScene.get(i).max_duration;
	}
	/*** Return Current Duration of Scene i*/
	public long getSceneCurrentDuration(int i){
		return myScene.get(i).current_duration;
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
