import java.awt.Image;
import java.awt.Graphics;
import javax.swing.ImageIcon;

import java.net.URL;
import java.util.ArrayList;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.*;

//import java.Image.*;
public class Character {
	private String itemSelected;
	private long SwordU;
	private long SwordD;
	private long SwordL;
	private long SwordR;
	private final long nano=1000000L;
	private int x;
	private int y;
	private int direction;
	private int maxHP;
	private int hp;
	private boolean isInvincible;
	private long last_invincible;
	Sword AttackSword;
	Sprite walk_Down;
	Sprite walk_Up;
	Sprite walk_Left;
	Sprite walk_Right;
	Sprite attack_Up;
	Sprite sword_up;
	Sprite attack_Down;
	Sprite sword_down;
	Sprite sword_left;
	Sprite sword_right;
	Sprite attack_Left;
	Sprite attack_Right;
	private long last_walk_down;
	private long last_walk_left;
	private long last_walk_right;
	private long last_walk_up;
	private long last_attack_up;
	private long last_attack_down;
	private long last_attack_left;
	private long last_attack_right;
	Rectangle2D mask;
	ArrayList<Item> inventory=new ArrayList<Item>();
	Character(){
		last_invincible=0;
		isInvincible=false;
		maxHP=16;
		hp=16;
		inventory.add(new Bow());
		sword_up=new Sprite();
		sword_down=new Sprite();
		sword_left=new Sprite();
		sword_right=new Sprite();
		last_attack_down=0;
		last_attack_up=0;
		last_attack_left=0;
		last_attack_right=0;
		last_walk_down=0;
		last_walk_left=0;
		last_walk_up=0;
		last_walk_right=0;
		walk_Down=new Sprite();
		walk_Left=new Sprite();
		walk_Right=new Sprite();
		walk_Up=new Sprite();
		attack_Up=new Sprite();
		attack_Down=new Sprite();
		attack_Left=new Sprite();
		attack_Right=new Sprite();
		x=100;
		y=100;
		mask=new Rectangle2D.Double(this.x,this.y,32,32);
		direction=90;
		Character_Setup();
	}
	public ArrayList<Item> getInventory(){
		return inventory;
	}
	public void setItemSelect(String item){
		itemSelected=item;
	}
	//public String getItemSelected(){
		//return itemSelected;
	//}
	public Image getItemSelectedImage(){
		URL imgURL;
		BufferedImage img;
		if(itemSelected=="bow"){
			try{
				imgURL=getClass().getResource("/images/bow.gif");
				img=ImageIO.read(imgURL);
				return (Image)img;
			}catch(Exception e){e.printStackTrace();}
		}
		return null;
			
	}
	public int getMaxHP(){
		return maxHP;
	}
	public int getHP(){
		return hp;
	}
	public void setInvincible(boolean state){
		isInvincible=state;
	}
	public long getLastInvincible(){
		return last_invincible;
	}
	public void setLastInvincible(long time){
		last_invincible=time;
	}
	public boolean getInvincible(){
		return isInvincible;
	}
	public void setHP(int newHP){
		hp=newHP;
	}
	public void setMaxHP(int newMaxHP){
		maxHP=newMaxHP;
	}
	public void Character_Setup(){
		try{
		URL imgURL=getClass().getResource("/images/Attack_Up.gif");
	    BufferedImage img = ImageIO.read(imgURL);
	    attack_Up.addScene((Image)img,400);
		imgURL=getClass().getResource("/images/Attack_Down.gif");
	    img = ImageIO.read(imgURL);
	    attack_Down.addScene((Image)img,400); 
		imgURL=getClass().getResource("/images/Attack_Left.gif");
	    img = ImageIO.read(imgURL);
	    attack_Left.addScene((Image)img,400);
		imgURL=getClass().getResource("/images/Attack_Right.gif");
	    img = ImageIO.read(imgURL);
	    attack_Right.addScene((Image)img,400);  
		imgURL=getClass().getResource("/images/sword_left.gif");
	    img = ImageIO.read(imgURL);
	    sword_left.addScene((Image)img,100);
		imgURL=getClass().getResource("/images/sword_right.gif");
	    img = ImageIO.read(imgURL);
	    sword_right.addScene((Image)img,100);
		imgURL=getClass().getResource("/images/sword_down.gif");
	    img = ImageIO.read(imgURL);
	    sword_down.addScene((Image)img,100);
		imgURL=getClass().getResource("/images/sword_up.gif");
	    img = ImageIO.read(imgURL);
	    sword_up.addScene((Image)img,100);
		imgURL=getClass().getResource("/images/link_down_part1.gif");
	    img = ImageIO.read(imgURL);
	    walk_Down.addScene((Image)img,100);
		imgURL=getClass().getResource("/images/link_down_part2.gif");
	    img = ImageIO.read(imgURL);	
		walk_Down.addScene((Image)img,100);
		imgURL=getClass().getResource("/images/link_left_part1.gif");
		img=ImageIO.read(imgURL);
		walk_Left.addScene((Image)img,100);
		imgURL=getClass().getResource("/images/link_left_part2.gif");
		img=ImageIO.read(imgURL);
		walk_Left.addScene((Image)img,100);
		imgURL=getClass().getResource("/images/link_up_part1.gif");
		img=ImageIO.read(imgURL);
		walk_Up.addScene((Image)img,100);
		imgURL=getClass().getResource("/images/link_up_part2.gif");
		img=ImageIO.read(imgURL);
		walk_Up.addScene((Image)img,100);
		imgURL=getClass().getResource("/images/link_right_part1.gif");
		img=ImageIO.read(imgURL);
		walk_Right.addScene((Image)img,100);
		imgURL=getClass().getResource("/images/link_right_part2.gif");
		img=ImageIO.read(imgURL);
		walk_Right.addScene((Image)img,100);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setX(int x){
		this.x=x;
	}
	public void setY(int y){
		this.y=y;
	}
	public Rectangle2D getMask(){
		return mask;
	}
	public void setMask(double x,double y,int w,int h){
		mask.setRect(x, y, w, h);
	}
	public Image getImage(){
		if(getDirection()==90 && ((System.nanoTime()-last_attack_down)/nano>=attack_Down.getDuration())){
			return walk_Down.getSceneimg();
		}
		else if(getDirection()==90 && ((System.nanoTime()-last_attack_down)/nano<attack_Down.getDuration()))
			return attack_Down.getSceneimg();
		else if(getDirection()==180 && ((System.nanoTime()-last_attack_left)/nano>=attack_Left.getDuration())){
			return walk_Left.getSceneimg();
		}
		else if(getDirection()==180 && ((System.nanoTime()-last_attack_left)/nano<attack_Left.getDuration()))
			return attack_Left.getSceneimg();
		else if(getDirection()==270 && ((System.nanoTime()-last_attack_up)/nano>=attack_Up.getDuration())){
			return walk_Up.getSceneimg();
		}
		else if(getDirection()==270 && ((System.nanoTime()-last_attack_up)/nano<attack_Up.getDuration()))
			return attack_Up.getSceneimg();
		else if(getDirection()==360 && ((System.nanoTime()-last_attack_right)/nano>=attack_Right.getDuration())){
			return walk_Right.getSceneimg();
		}
		else if(getDirection()==360 && ((System.nanoTime()-last_attack_right)/nano<attack_Right.getDuration()))
			return attack_Right.getSceneimg();
		return null;
	}
	public int getDirection(){
		return direction;
	}
	public boolean canMove(){
		long up=(System.nanoTime()-last_attack_up)/nano;
		long down=(System.nanoTime()-last_attack_down)/nano;
		long left=(System.nanoTime()-last_attack_left)/nano;
		long right=(System.nanoTime()-last_attack_right)/nano;
		 if(up<attack_Up.getDuration()|| down<attack_Down.getDuration() || left<attack_Left.getDuration()
				 || right<attack_Right.getDuration()){
			 return false;
		 }
		return true;
	}
	public  void  move(){
		if(getDirection()==90){
			if((System.nanoTime()-last_walk_down)/nano>=walk_Down.getDuration()){
				if(walk_Down.getIndex()==walk_Down.getAnimation().size()-1)
					walk_Down.setIndex(0);
			else{
					walk_Down.incIndex();
				}
				last_walk_down=System.nanoTime();
			}
			y+=4;
			mask.setRect(this.x, this.y+4, 32, 32);
			walk_Left.setIndex(0);
			walk_Up.setIndex(0);
			walk_Right.setIndex(0);
		}
		else if(getDirection()==180){
			if((System.nanoTime()-last_walk_left)/nano>=walk_Up.getDuration()){
				if(walk_Left.getIndex()==walk_Left.getAnimation().size()-1)
					walk_Left.setIndex(0);
				else
					walk_Left.incIndex();
				last_walk_left=System.nanoTime();
			}
			x-=4;
			mask.setRect(this.x-4, this.y, 32, 32);
			walk_Up.setIndex(0);
			walk_Down.setIndex(0);
			walk_Right.setIndex(0);			
			}
		else if(getDirection()==270){
			if((System.nanoTime()-last_walk_up)/nano>=walk_Up.getDuration()){
				if(walk_Up.getIndex()==walk_Up.getAnimation().size()-1)
					walk_Up.setIndex(0);
				else
					walk_Up.incIndex();
				last_walk_up=System.nanoTime();		
			}
			y-=4;
			mask.setRect(this.x, this.y-4, 32, 32);
			walk_Left.setIndex(0);		
			walk_Down.setIndex(0);
			walk_Right.setIndex(0);	
			}
		else if(getDirection()==360){
			if((System.nanoTime()-last_walk_right)/nano>=walk_Right.getDuration()){
				if(walk_Right.getIndex()==walk_Right.getAnimation().size()-1)
					walk_Right.setIndex(0);
				else
					walk_Right.incIndex();
				last_walk_right=System.nanoTime();	
			}	
			//delse{System.out.println("RESUTL"+(System.nanoTime()-last_walk_right)/1000000);}
			x+=4;
			mask.setRect(this.x+4, this.y, 32, 32);
			walk_Left.setIndex(0);
			walk_Up.setIndex(0);
			walk_Down.setIndex(0);		
			}
	}
	public void setDirection(int direction){
		this.direction=direction;
	}
	public void attack(){
		if(getDirection()==270){
		AttackSword=new Sword(getDirection(),x+6,y-22,16,32);
		last_attack_up=System.nanoTime();
		}
		if(getDirection()==90){
		AttackSword=new Sword(getDirection(),x+10,y+22,16,32);
		last_attack_down=System.nanoTime();
		}
		if(getDirection()==180){
		AttackSword=new Sword(getDirection(),x-22,y+10,32,16);
		last_attack_left=System.nanoTime();
		walk_Left.setIndex(1);
		}
		if(getDirection()==360){
		AttackSword=new Sword(getDirection(),x+22,y+10,32,16);
		last_attack_right=System.nanoTime();
		walk_Right.setIndex(1);
		}
	}
	public Sword getSword(){
		return AttackSword;
	}
	public void draw(Graphics g){
		g.drawImage(getImage(),this.x,this.y,null);
		SwordU=(System.nanoTime()-last_attack_up)/nano;
		SwordD=(System.nanoTime()-last_attack_down)/nano;
		SwordL=(System.nanoTime()-last_attack_left)/nano;
		SwordR=(System.nanoTime()-last_attack_right)/nano;
		if(SwordU<attack_Up.getDuration() || SwordD<attack_Down.getDuration()
				|| SwordL<attack_Left.getDuration() ||SwordR<attack_Right.getDuration())
			AttackSword.draw(g);
			//g.drawImage(sword_up.getSceneimg(),this.x+6,this.y-22,null);
		//}
	}
}
