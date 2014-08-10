package main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import utility.Animation;

import main.Hero.Direction;
import monster.Monster;

public class Fire extends Monster{
	public Animation fireAnimation;
	public Direction fireDir=Direction.None;
	public Rectangle mask;
	public Fire(int x, int y, Monster.ID type) {
		super(x, y, type);
		buildAnimation();
		mask=new Rectangle(x,y,32,32);
	}
	private void buildAnimation() {
		fireAnimation=new Animation();
		BufferedImage image;
		try {	image = ImageIO.read(getClass().getResourceAsStream("/map/Fire.png"));
				fireAnimation.AddScene(image.getSubimage(0, 0, 32, 32), 100);
				fireAnimation.AddScene(image.getSubimage(32, 0, 32, 32), 100);	
			} 	catch (IOException e) {e.printStackTrace();}
	}
	public void render(Graphics g){
		g.drawImage(fireAnimation.getImage(), x,y,null);
	}
	public void update(){
		fireAnimation.setImage();
	}

}
