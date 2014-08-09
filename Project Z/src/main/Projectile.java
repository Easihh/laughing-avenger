package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Hero.Direction;

public class Projectile {
	private int x,y;
	private final int speed=5;
	private Direction dir;
	private BufferedImage img;
	public Projectile(int x,int y, BufferedImage img,Direction dir){
		this.x=x;
		this.y=y;
		this.img=img;
		this.dir=dir;
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
	}
	public void render(Graphics g){
		g.drawImage(img, x, y,null);
	}
}
