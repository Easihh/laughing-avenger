import java.util.Random;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.*;

import java.net.*;
public class Octopus extends Monster{
	Sprite walk_Up;
	Sprite walk_Down;
	Sprite walk_Left;
	Sprite walk_Right;
	private long timeSinceLastSwitch;
	private long last_walk_down;
	private long last_walk_left;
	private long last_walk_right;
	private long last_walk_up;
	private final long nano=1000000L;
	public Octopus(int x,int y){
		super(x,y);
		walk_Up=new Sprite();
		walk_Down=new Sprite();
		walk_Left=new Sprite();
		walk_Right=new Sprite();
		last_walk_down=0;
		last_walk_left=0;
		last_walk_right=0;
		last_walk_up=0;
		timeSinceLastSwitch=0;
		setup();
	}
	public void setup(){
		try{
			URL imgURL=getClass().getResource("/images/Octopus_DownPart1.gif");
		    BufferedImage img = ImageIO.read(imgURL);
		    walk_Down.addScene((Image)img,400);
			imgURL=getClass().getResource("/images/Octopus_DownPart2.gif");
		    img = ImageIO.read(imgURL);
		    walk_Down.addScene((Image)img,500);
			imgURL=getClass().getResource("/images/Octopus_UpPart1.gif");
		    img = ImageIO.read(imgURL);
		    walk_Up.addScene((Image)img,500);
			imgURL=getClass().getResource("/images/Octopus_UpPart2.gif");
		    img = ImageIO.read(imgURL);
		    walk_Up.addScene((Image)img,500);
			imgURL=getClass().getResource("/images/Octopus_LeftPart1.gif");
		    img = ImageIO.read(imgURL);
		    walk_Left.addScene((Image)img,500);
			imgURL=getClass().getResource("/images/Octopus_LeftPart2.gif");
		    img = ImageIO.read(imgURL);
		    walk_Left.addScene((Image)img,500);
			imgURL=getClass().getResource("/images/Octopus_RightPart1.gif");
		    img = ImageIO.read(imgURL);
		    walk_Right.addScene((Image)img,500);
			imgURL=getClass().getResource("/images/Octopus_RightPart2.gif");
		    img = ImageIO.read(imgURL);
		    walk_Right.addScene((Image)img,500);
		}catch(Exception e ){e.printStackTrace();}
	}
	public void AI(){
		Random myrandom= new Random();
		int dir=myrandom.nextInt(4);
		if(dir==0&& ((System.nanoTime()-timeSinceLastSwitch)/nano>=3000)){
			direction=180;
			timeSinceLastSwitch=System.nanoTime();
		}
		else if (dir==1&& ((System.nanoTime()-timeSinceLastSwitch)/nano>=3000)){
			direction=90;
			timeSinceLastSwitch=System.nanoTime();
		}
		else if(dir==2&& ((System.nanoTime()-timeSinceLastSwitch)/nano>=3000)){
			direction=360;
			timeSinceLastSwitch=System.nanoTime();
		}
		else if(dir==3&& ((System.nanoTime()-timeSinceLastSwitch)/nano>=3000)){
			direction=270;
			timeSinceLastSwitch=System.nanoTime();
		}
		else{
			if(direction==180){
				if((System.nanoTime()-last_walk_left)/nano>=walk_Left.getDuration())
					if(walk_Left.getIndex()==walk_Left.getMaxIndex()-1){
						walk_Left.setIndex(0);
						last_walk_left=System.nanoTime();
					}
					else walk_Left.incIndex();
			x-=4;
			last_walk_down=0;
			last_walk_up=0;
			last_walk_right=0;
		}
			else if(direction==90){
				if((System.nanoTime()-last_walk_down)/nano>=walk_Down.getDuration())
					if(walk_Down.getIndex()==walk_Down.getMaxIndex()-1){
						walk_Down.setIndex(0);
						last_walk_down=System.nanoTime();
					}
					else walk_Down.incIndex();
				y+=4;
				last_walk_up=0;
				last_walk_right=0;
				last_walk_left=0;
			}
			else if(direction==360){
				if((System.nanoTime()-last_walk_right)/nano>=walk_Right.getDuration())
					if(walk_Right.getIndex()==walk_Right.getMaxIndex()-1){
						walk_Right.setIndex(0);
						last_walk_right=System.nanoTime();
					}
					else walk_Right.incIndex();
				x+=4;
				last_walk_up=0;
				last_walk_left=0;
				last_walk_down=0;
			}
			else if(direction==270){
				if((System.nanoTime()-last_walk_up)/nano>=walk_Up.getDuration())
					if(walk_Up.getIndex()==walk_Up.getMaxIndex()-1){
						walk_Up.setIndex(0);
						last_walk_up=System.nanoTime();
					}
					else walk_Up.incIndex();
			y-=4;
			last_walk_left=0;
			last_walk_right=0;
			last_walk_down=0;
		}
	}
		getMask().setRect((double)x, (double)y, 32, 32);
	}
	public void setDamage(){
		hp-=1;
	}
	public Image getImage(){
		if(direction==90)
			return walk_Down.getSceneimg();
		else if(direction==360)
			return walk_Right.getSceneimg();
		else if(direction==180)
			return walk_Left.getSceneimg();
		else if(direction==270)
			return walk_Up.getSceneimg();
		return null;
	}
	public void draw(Graphics g){
		g.drawImage(getImage(), x, y, null);
	}
}
