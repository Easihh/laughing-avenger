import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	Tile colliding_Tile;
	public boolean isCollidingSlope=false;
	public int portraitX_size=32;
	public int portraitY_size=32;
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
		current_health=75;
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
		switch(dir){
		case Left:	g.drawImage((Image)left.getImage(), (int)x, (int)y, (int)width, (int)height, null);
					break;
		case Right:	g.drawImage((Image)right.getImage(), (int)x, (int)y, (int)width, (int)height, null);
					break;
		}
		g.setColor(Color.gray);	
		if(x>Slime.scroll_x && y<Slime.scroll_y)	
			drawbar(g,(int)x-Slime.scroll_x,0, hpbar_width, 16);
		if(x>Slime.scroll_x && y>Slime.scroll_y)
			drawbar(g,(int)x-Slime.scroll_x, (int)y-Slime.scroll_y, hpbar_width, 16);
		if(x<Slime.scroll_x && y<Slime.scroll_y)
			drawbar(g,0,0, hpbar_width, 16);
		if(x<Slime.scroll_x && y>Slime.scroll_y)
			drawbar(g,0,(int)y-Slime.scroll_y, hpbar_width, 16);
		g.setColor(Color.red);
		g.drawString("Player"+playernumber, (int)(x-(width/2)), (int)(y-8));
		g.drawRect((int)x, (int)y, (int)width, (int)height);		
	}
	private void drawbar(Graphics g,int x,int y,int width,int height) {
		double amount=hpbar_width*(current_health/maxHealth);
		g.drawRect(x,y,portraitX_size,portraitY_size);
		int[] xpoint={x+portraitX_size+16,x+width+portraitX_size+16,x+180+portraitX_size+16,x+portraitX_size+5};
		int[] ypoint={y+(portraitY_size/2),y+(portraitY_size/2),y+(height/2)+(portraitY_size/2),y+(height/2)+(portraitY_size/2)};
		Polygon maxhpbar=new Polygon(xpoint, ypoint, 4);
		g.setColor(new Color(0x00ff00));
		//g.setColor(Color.green);
		g.drawPolygon(maxhpbar);
		g.fillPolygon(maxhpbar);
		if(current_health<maxHealth){
			int[] xpoint2={(int)(x+16+portraitX_size+amount+5),(int)(x+width+portraitX_size+16),(int)(x+width+portraitX_size+16-5),(int)(x+16+portraitX_size+amount)};
			int[] ypoint2={y+(portraitY_size/2),y+(portraitY_size/2),y+(height/2)+(portraitY_size/2),y+(height/2)+(portraitY_size/2)};
			Polygon currenthpbar=new Polygon(xpoint2, ypoint2, 4);
			g.setColor(new Color(0xdc143c));
			g.drawPolygon(currenthpbar);
			g.fillPolygon(currenthpbar);
		}
		g.setColor(Color.darkGray);
		g.drawString("Player "+playernumber,x+portraitX_size+12,y+12);
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
				collisionRight=getCollision(new Point((int)(x+width-1),(int)(y)),new Point((int)(x+width-1),(int)(y+height-2)));
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
				else{//nothing infront of me so move
					x+=movement_speed;
					mastermask.x=x;
					hspeed=1;
				}
			}
				if(charState==state.Falling){
					if(polygonType==4){
						switchPress(colliding_Tile);
						colliding_Tile.destroyShape();
						}
					charState=state.Standing;
			}
		}
	}
	private boolean getCollision(Point pt1,Point pt2) {
		for(Tile aTile:Level.tile)
			if(aTile.shape.contains(pt1)|| aTile.shape.contains(pt2)){
				polygonType=aTile.type;
				colliding_Tile=aTile;
				return true;
				}
		return false;
	}
	private void switchPress(Tile aTile){
		BufferedImage img;
		try {
			img = ImageIO.read(getClass().getResource("/tileset/buttonRed_pressed.png"));
			aTile.img=img;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
