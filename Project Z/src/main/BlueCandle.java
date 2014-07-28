package main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import utility.Sound;
import utility.Stopwatch;
import main.Hero.Direction;

public class BlueCandle extends Item {
	Animation fire;
	Stopwatch timer;
	int stepToMove;
	Direction fireDir=Direction.None;
	public BlueCandle(){
		name="Blue Candle";
		x=241;
		y=98;
		mask=new Rectangle(x,y,width,height);
		loadImage();
	}
	
	private void loadImage() {
		try {img=ImageIO.read(getClass().getResourceAsStream("/map/BlueCandle.png"));
		BufferedImage image=ImageIO.read(getClass().getResourceAsStream("/map/Fire.png"));
		fire=new Animation();
		fire.AddScene(image.getSubimage(0, 0, 32, 32), 100);
		fire.AddScene(image.getSubimage(32, 0, 32, 32), 100);
		} 
		catch (IOException e) {e.printStackTrace();}	
	}
	public void use(){
		Sound.candle.setFramePosition(0);
		Sound.candle.flush();
		Sound.candle.start();
		Hero hero=Hero.getInstance();
		stepToMove=64;
		timer=new Stopwatch();
		timer.start();
		if(hero.dir==Direction.Down){
			fireDir=Direction.Down;
			x=hero.x;
			y=hero.y+height;
		}
		if(hero.dir==Direction.Up){
			fireDir=Direction.Up;
			x=hero.x;
			y=hero.y-height;
		}
		if(hero.dir==Direction.Left){
			fireDir=Direction.Left;
			x=hero.x-width;
			y=hero.y;
		}
		if(hero.dir==Direction.Right){
			fireDir=Direction.Right;
			x=hero.x+width;
			y=hero.y;
		}
	}
	public void update(){
		if(fireDir==Direction.Down && stepToMove!=0){
			y++;
			stepToMove--;
		}
		if(fireDir==Direction.Up && stepToMove!=0){
			y--;
			stepToMove--;
		}
		if(fireDir==Direction.Left && stepToMove!=0){
			x--;
			stepToMove--;
		}
		if(fireDir==Direction.Right && stepToMove!=0){
			x++;
			stepToMove--;
		}
		if(timer!=null && timer.elapsedMillis()>3000)
			timer=null;
	}
	public void render(Graphics g){
		fire.setImage();
		if(timer!=null){
			g.drawImage(fire.getImage(),x,y,null);
		}
	}
}
