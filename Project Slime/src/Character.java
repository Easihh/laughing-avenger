import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JLabel;


public class Character {
	public boolean isCollidingSlope=false;
	public int polygonType;
	private BufferedImage theplayer;
	Animation left=new Animation();
	Animation right=new Animation();
	private int hpbar_width=200;
	public double movement_speed=3;
	public double height=24;
	public double width=24;
	public double x;
	public double y;
	public double vspeed;
	public double hspeed;
	private double gravity=3;
	public enum state {Jumping,Walking,Standing,Falling};
	public enum direction{Left,Right,Down,Up};
	public state charState;
	public direction dir;
	public int playernumber=new Random().nextInt(9999);
	Rectangle2D.Double mastermask;
	Rectangle2D.Double topmask;
	Rectangle2D.Double leftmask;
	Rectangle2D.Double rightmask;
	Rectangle2D.Double downmask;
	public boolean isPressedRight;
	public boolean isPressedLeft;
	public double current_health;
	public double maxHealth;
	public boolean isInvincible;
	public Character(int x,int y){
		this.x=x;
		this.y=y;
		current_health=100;
		maxHealth=100;
		isInvincible=false;
		vspeed=-10;
		hspeed=0;
		isPressedRight=false;
		isPressedLeft=false;
		charState=state.Falling;
		dir=direction.Right;
		mastermask=new Rectangle2D.Double(x,y,width,height);
		topmask=new Rectangle2D.Double(x,y,width,8);
		leftmask=new Rectangle2D.Double(x,y+8,width/2,8);
		rightmask=new Rectangle2D.Double(x+12,y+8,width/2,8);
		downmask=new Rectangle2D.Double(x,y+16,width,8);
		try {
			theplayer=ImageIO.read(getClass().getResource("/tileset/player_left.png"));
			left.addScene(theplayer, 0.5);
			theplayer=ImageIO.read(getClass().getResource("/tileset/player_right.png"));
			right.addScene(theplayer, 0.5);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void draw(Graphics g){
		double amount;
		g.setColor(Color.blue);
		switch(dir){
		case Left:	g.drawImage((Image)left.getImage(), (int)x, (int)y, (int)width, (int)height, null);
					break;
		case Right:	g.drawImage((Image)right.getImage(), (int)x, (int)y, (int)width, (int)height, null);
					break;
		}
		g.setColor(Color.DARK_GRAY);
		if(x>Slime.scroll_x && y<Slime.scroll_y){
			g.drawRect((int)x-Slime.scroll_x,0, hpbar_width, 16);
			g.setColor(Color.red);
			amount=hpbar_width*(current_health/maxHealth);
			g.fillRect((int)x-Slime.scroll_x,0, (int)amount, 16);
			g.setColor(Color.BLACK);
			g.fillRect((int)x-Slime.scroll_x+(int)(amount),0, (int)(hpbar_width-amount), 16);
		}
		if(x>Slime.scroll_x && y>Slime.scroll_y){
			g.drawRect((int)x-Slime.scroll_x, (int)y-Slime.scroll_y, hpbar_width, 16);
			g.setColor(Color.red);
			amount=hpbar_width*(current_health/maxHealth);
			g.fillRect((int)x-Slime.scroll_x, (int)y-Slime.scroll_y, (int)amount, 16);
			g.setColor(Color.BLACK);
			g.fillRect((int)(x-Slime.scroll_x+amount),(int)y-Slime.scroll_y, (int)(hpbar_width-amount), 16);
		}
		if(x<Slime.scroll_x && y<Slime.scroll_y){
			g.drawRect(0,0, hpbar_width, 16);
			g.setColor(Color.red);
			amount=hpbar_width*(current_health/maxHealth);
			g.fillRect(0,0, (int)amount, 16);
			g.setColor(Color.BLACK);
			g.fillRect((int)(amount),0, (int)(hpbar_width-amount), 16);
		}
		if(x<Slime.scroll_x && y>Slime.scroll_y){
			g.drawRect(0,(int)y-Slime.scroll_y, hpbar_width, 16);
			g.setColor(Color.red);
			amount=hpbar_width*(current_health/maxHealth);
			g.fillRect(0,(int)y-Slime.scroll_y, (int)amount, 16);
			g.setColor(Color.BLACK);
			g.fillRect((int)(amount),(int)y-Slime.scroll_y, (int)(hpbar_width-amount), 16);
		}
		g.setColor(Color.red);
		g.drawString("Player"+playernumber, (int)(x-(width/2)), (int)(y-8));
		g.drawRect((int)x, (int)y, (int)width, (int)height);
		//g.drawString("Player"+playernumber, (int)x+8, (int)x);
		//g.setColor(Color.blue);
		//g.drawRect((int)topmask.x, (int)topmask.y, (int)topmask.width, (int)topmask.height);
		//g.setColor(Color.cyan);
		//g.drawRect((int)leftmask.x, (int)leftmask.y, (int)leftmask.width, (int)leftmask.height);
		//g.setColor(Color.DARK_GRAY);
		//g.drawRect((int)rightmask.x, (int)rightmask.y, (int)rightmask.width, (int)rightmask.height);
		//g.setColor(Color.green);
		//g.drawRect((int)downmask.x, (int)downmask.y, (int)downmask.width, (int)downmask.height);
		
	}
	public void jump(){
		charState=state.Jumping;
		vspeed=-24;
	}
	public void gravity() {
		boolean collisionExist,collisionLeft,collisionRight;
		if(!isPressedLeft && !isPressedRight)
			hspeed=0;
		if(vspeed<0){
			y-=gravity;
			mastermask.y=y;
			vspeed++;
			if(vspeed==0)charState=state.Falling;
		}
		collisionExist=getCollision(new Point((int)(x+width),(int)(y+height+2)),new Point((int)(x),((int)(y+height+2))));
		if(!collisionExist){
			if(vspeed==0){
			y+=gravity;
			mastermask.y=y;
			}
			if(dir==direction.Right && isPressedRight && hspeed!=0){				
				x+=gravity;
				mastermask.x=x;
			}
			if(dir==direction.Left && isPressedLeft && hspeed!=0){
				x-=gravity;
				mastermask.x=x;
			}
			if(dir==direction.Right && isPressedRight  && charState==state.Standing && isCollidingSlope){
				x+=3;
				mastermask.x+=3;
			}
			if(dir==direction.Left && isPressedLeft && charState==state.Standing && isCollidingSlope){
				x-=3;
				mastermask.x-=3;
				y-=gravity+4;
				mastermask.y-=gravity;
			}
		}
		if(collisionExist){
			if(dir==direction.Left && isPressedLeft){
				collisionLeft=getCollision(new Point((int)(x),(int)(y)),new Point((int)(x),(int)(y+height-2)));
				if(collisionLeft){
					if(charState==state.Falling || charState==state.Standing){
						if(polygonType==1){//on a slope
							y-=movement_speed;
							mastermask.y=y;
						}
						if(polygonType==3){//there is a block infront of me
							x+=movement_speed;
							mastermask.x=x;
						}
					}
				}else{	//nothing to my left so move left
						x-=movement_speed;
						mastermask.x=x;
						hspeed=-1;
				}
			}
			if(dir==direction.Right && isPressedRight){
				collisionRight=getCollision(new Point((int)(x+width),(int)(y)),new Point((int)(x+width),(int)(y+height-2)));
				if(collisionRight){
					if(charState==state.Falling || charState==state.Standing){
						if(polygonType==1){//on a down slope
							y-=movement_speed;
							mastermask.y=y;
						}
						if(polygonType==2){ //on a up slope
							y-=movement_speed;
							mastermask.y=y;
						}
						if(polygonType==3){ //block infront of me
							x-=movement_speed;
							mastermask.x=x;
						}
					}
				}
				else{//nothing infront of me so move move
					x+=movement_speed;
					mastermask.x=x;
					hspeed=1;
				}
			}
				if(charState==state.Falling)
				charState=state.Standing;
		}
	}
	private boolean getCollision(Point pt1,Point pt2) {
		for(Tile aTile:Level.tile)
			if(aTile.shape.contains(pt1)|| aTile.shape.contains(pt2)){
				polygonType=aTile.type;
				return true;
			}
		return false;
	}
}
