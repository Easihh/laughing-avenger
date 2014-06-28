import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Projectile {
	private BufferedImage img=null;	
	public int projectile_speed=6;	
	public Game.Direction dir;
	public int x;
	public int y;
	public Rectangle lefttop,leftbottom,righttop,rightbottom,topleft,topright,bottomleft,bottomright;
	public Projectile(int x,int y,BufferedImage img,Game.Direction dir){
		this.x=x;
		this.y=y;
		this.img=img;
		this.dir=dir;
		buildMask();
	}
	private void buildMask() {
		lefttop=new Rectangle(x-projectile_speed,y,projectile_speed,16);
		leftbottom=new Rectangle(x-projectile_speed,y+16,projectile_speed,16);
		
		topleft=new Rectangle(x,y-projectile_speed,16,projectile_speed);
		topright=new Rectangle(x+16,y-projectile_speed,16,projectile_speed);
		
		righttop=new Rectangle(x+32,y,projectile_speed,16);
		rightbottom=new Rectangle(x+32,y+16,projectile_speed,16);
		
		bottomleft=new Rectangle(x,y+32,16,projectile_speed);
		bottomright=new Rectangle(x+16,y+32,16,projectile_speed);
	}
	public void render(Graphics g) {
		g.drawImage(img,x,y,null);
	}
	public void update(){
		switch(dir){
		case Right:	x+=projectile_speed;
					break;
		case Left: 	x-=projectile_speed;
					break;
		case Down: 	y+=projectile_speed;
					break;
		case Up: 	y-=projectile_speed;
					break;
		}
		buildMask();
	}
}
