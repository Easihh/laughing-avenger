import java.util.ArrayList;
import java.awt.Image;
public class Sprite {
ArrayList<aScene> Animation=new ArrayList<aScene>();
private int anim_Index;
private int animMaxIndex;

	public Sprite(){
		animMaxIndex=0;
	anim_Index=0;
	}
	public int getIndex(){
		return anim_Index;
	}
	public int getMaxIndex(){
		return animMaxIndex;
	}
	public void addScene(Image img,int duration){
		aScene theScene=new aScene(img,duration);
		Animation.add(theScene);
		animMaxIndex++;
	}
	public Image getSceneimg(){
		return Animation.get(getIndex()).getSceneimg();
	}
	public aScene getScene(){
		return Animation.get(anim_Index);
	}
	public ArrayList<aScene> getAnimation(){
		return Animation;
	}
	public int getDuration(){
		return Animation.get(anim_Index).getSceneDuration();
	}
	public void incIndex(){
		anim_Index++;
	}
	public void decIndex(){
		anim_Index--;
	}
	public void setIndex(int index){
		anim_Index=index;
	}
 private class aScene{
	 private int scene_time;
	 private Image theImage;
		public aScene(Image img,int Time){
			scene_time=Time;
			theImage=img;
		}
		public int getSceneDuration(){
			return scene_time;
		}
		public Image getSceneimg(){
			return theImage;
		}
	}
}
