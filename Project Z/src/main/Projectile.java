package main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Hero.Direction;

public class Projectile {
	private int x,y,width,height;
	private final int speed=5;
	private Direction dir;
	private BufferedImage img;
	public Rectangle mask;
	public Projectile(Rectangle infoRect, BufferedImage img,Direction dir){
		x=infoRect.x;
		y=infoRect.y;
		this.img=img;
		this.dir=dir;
		width=infoRect.width;
		height=infoRect.height;
	}
	public void update(){
		switch(dir){
		case Down:	y+=speed;
					break;
		case Up:	y-=speed;
					break;
		case Left:	x-=speed;
					break;
		case Right: x+=speed;
					break;
		}
		mask=new Rectangle(x,y,width,height);
		//System.out.println("X:"+x);
		//System.out.println("Y:"+y);
	}
	public void render(Graphics g){
		g.drawImage(img, x, y,null);
	}
	public boolean outOfBound() {
		Map map=Map.getInstance();
		if(dir==Direction.Right && x>(map.worldX*map.roomWidth)+map.roomWidth)
			return true;
		if(dir==Direction.Left && x<(map.worldX*map.roomWidth))
			return true;
		if(dir==Direction.Up && y<(map.worldY*map.roomHeight))
			return true;
		if(dir==Direction.Down && y>(map.worldY*map.roomHeight)+map.roomHeight)
			return true;
		return false;
	}
}
