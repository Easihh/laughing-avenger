import java.awt.Image;
import java.util.ArrayList;


public class Animation {
	long nano=1000000000L;
	ArrayList<Sprite> spr=new ArrayList<Sprite>();
	double total_duration;
	int current_index;
	public Animation(){
		total_duration=0;
		current_index=0;
	}
	public void addScene(Image img,double duration){
		spr.add(new Sprite(img,duration));
		total_duration+=duration;
	}
	public Image getImage(){
		return(spr.get(current_index).image);
	}
	private class Sprite{
		Image image=null;
		double total_duration=0;
		double current_duration=0;
		Sprite(Image img,double duration){
			this.image=img;
			total_duration=duration;
		}
	}
	public void setImage(double timepassed) {
		if(spr.get(current_index).current_duration+(timepassed/nano)>=spr.get(current_index).total_duration){
			spr.get(current_index).current_duration=0;
			if(current_index==spr.size()-1)
					current_index=0;
			else current_index++;
		}else{
			spr.get(current_index).current_duration+=(timepassed/nano);
			//System.out.println("TIMEPASSED:"+(timepassed));
			//System.out.println("TIMETOTAL:"+spr.get(current_index).current_duration);
		}
	}
}
