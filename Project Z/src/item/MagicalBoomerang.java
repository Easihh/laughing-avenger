package item;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;

import utility.Sound;

import main.Hero;
import main.Hero.Direction;
import main.Projectile;

public class MagicalBoomerang extends Item{
	public Projectile myBoomerang;
	private double degree=0.0;
	private final int stepTillComeBack=256;
	private int currentStep,counter,distancex,distancey,distance;
	public MagicalBoomerang(int x, int y, ID type) {
		super(x, y, type);
		inventoryX=x;
		InventoryY=y;
		loadImage();
	}

	private void loadImage() {
		try {img=ImageIO.read(getClass().getResourceAsStream("/map/MagicalBoomerang.png"));}
		catch (IOException e) {e.printStackTrace();}	
	}

	@Override
	public void render(Graphics g) {
		if(myBoomerang!=null)
			g.drawImage(myBoomerang.getTilt(degree, (BufferedImage)img),myBoomerang.x,myBoomerang.y,null);
	}

	@Override
	public void use() {
		if(myBoomerang==null){
			myBoomerang=new Projectile(getInfoRect(), (BufferedImage) img, Hero.getInstance().dir);
			myBoomerang.mask=new Rectangle(myBoomerang.x,myBoomerang.y,width,height);
			currentStep=0;
			Sound.boomerang.setFramePosition(0);
			Sound.boomerang.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	private Rectangle getInfoRect() {
		Hero hero=Hero.getInstance();
		switch(hero.dir){
		case Left:	return new Rectangle(hero.x-40,hero.y,32,32);
		case Right:	return new Rectangle(hero.x+40,hero.y,32,32);
		case Up:	return new Rectangle(hero.x,hero.y-40,32,32);
		case Down:	return new Rectangle(hero.x,hero.y+40,32,32);	
		}
		return null;
	}

	@Override
	public void update() {
		Hero hero=Hero.getInstance();
		if(myBoomerang!=null){
			myBoomerang.mask=new Rectangle(myBoomerang.x,myBoomerang.y,width,height);
			degree+=0.3;
			if(currentStep<stepTillComeBack){
				if(myBoomerang.dir==Direction.Right){
					myBoomerang.x+=4;
					currentStep+=4;
				}
				if(myBoomerang.dir==Direction.Left){
					myBoomerang.x-=4;
					currentStep+=4;
				}
				if(myBoomerang.dir==Direction.Up){
					myBoomerang.y-=4;
					currentStep+=4;
				}
				if(myBoomerang.dir==Direction.Down){
					myBoomerang.y+=4;
					currentStep+=4;
				}
			}
			if(currentStep>=stepTillComeBack)
				returnBooomerang();
			if(myBoomerang!=null && myBoomerang.mask.intersects(new Rectangle(hero.x,hero.y,32,32))){
				myBoomerang=null;
				Sound.boomerang.stop();
			}
		}
	}

	private void returnBooomerang() {
		Hero hero=Hero.getInstance();
		distancex=myBoomerang.x-hero.x;
		distancey=myBoomerang.y-hero.y;
		distance=0;
		if(distancey!=0 && (myBoomerang.dir==Direction.Left || myBoomerang.dir==Direction.Right))
			distance=Math.abs(distancex)/Math.abs(distancey);
		if(distancex!=0 && (myBoomerang.dir==Direction.Up || myBoomerang.dir==Direction.Down))
			distance=Math.abs(distancey)/Math.abs(distancex);
		if(distancex!=0 && distancey!=0){
			if(myBoomerang.dir==Direction.Up || myBoomerang.dir==Direction.Down)
				MoveUpDownRang();
			if(myBoomerang.dir==Direction.Left || myBoomerang.dir==Direction.Right)
				MoveLeftRightRang();
		}
		//Incase the player move at the last second right as boomerang finish its return.
		else if(distancey==0){
			if(distancex<0)
				myBoomerang.x+=4;
			else myBoomerang.x-=4;
		}
		else if(distancex==0){
			if(distancey<0)
				myBoomerang.y+=4;
			else myBoomerang.y-=4;
		}
	}

	private void MoveUpDownRang() {
		if(myBoomerang.dir==Direction.Up && distancey<0)
			myBoomerang.y+=4;
		if(myBoomerang.dir==Direction.Down && distancey>0)
			myBoomerang.y-=4;
		if(distancex>0){
			counter++;
			if(counter>=distance){
				myBoomerang.x-=4;
				counter=0;
			}
		}
		if(distancex<0){	
			counter++;
			if(counter>=distance){
				myBoomerang.x+=4;
				counter=0;
			}
		}
	}

	private void MoveLeftRightRang() {
		if(myBoomerang.dir==Direction.Left && distancex<0)
			myBoomerang.x+=4;
		if(myBoomerang.dir==Direction.Right && distancex>0)
			myBoomerang.x-=4;
		if(distancey>0){
			counter++;
			if(counter>=distance){
				myBoomerang.y-=4;
				counter=0;
			}
		}
		if(distancey<0){
			counter++;
			if(counter>=distance){
				myBoomerang.y+=4;
				counter=0;
			}
		}
	}

}
