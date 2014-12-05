import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/*** 
 * This class is used for Character and Monster Projectile.
 * A Projectile's Collision Mask is represented as a Square subdivided into 8 equal part in order to
 * represent partial collision with other object or Character.
 */
public class Projectile {
	private BufferedImage img=null;	//how the projectile looks.
	private final int half_width=16,half_height=16,width=32,height=32;
	/*** Speed at which the Projectile travel in pixel per second*/
	public int projectile_speed=6;	
	/*** Projectile's Direction */
	public Game.Direction dir;
	/*** X-axis position of projectile*/
	public int x;
	/*** Y-axis position of projectile*/
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
	/*** How the project is rendered on screen*/
	public void render(Graphics g) {
		g.drawImage(img,x,y,null);
	}
	/***Update Project location at every game update and update its Mask to match new location.*/
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
		default:	break;
		}
		buildMask();
	}
}
