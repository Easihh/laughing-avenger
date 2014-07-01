import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Projectile {
	private BufferedImage img=null;	
	private final int half_width=16;
	private final int half_height=16;
	private final int width=32;
	private final int height=32;
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
		lefttop=new Rectangle(x-projectile_speed,y,projectile_speed,half_height);
		leftbottom=new Rectangle(x-projectile_speed,y+half_height,projectile_speed,half_height);
		
		topleft=new Rectangle(x,y-projectile_speed,half_width,projectile_speed);
		topright=new Rectangle(x+half_width,y-projectile_speed,half_width,projectile_speed);
		
		righttop=new Rectangle(x+width,y,projectile_speed,half_height);
		rightbottom=new Rectangle(x+width,y+half_height,projectile_speed,half_height);
		
		bottomleft=new Rectangle(x,y+height,half_width,projectile_speed);
		bottomright=new Rectangle(x+half_width,y+height,half_width,projectile_speed);
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
