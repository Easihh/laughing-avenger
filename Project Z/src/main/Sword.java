package main;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import main.Hero.Direction;
import utility.Stopwatch;

public class Sword {
	private int x,y;
	Vector<BufferedImage> sword_img;
	Stopwatch last_attack;
	final long attack_delay=300;
	Direction dir;
	public Rectangle mask;
	Sword(Hero hero){
		this.dir=hero.dir;
		setPosition(hero);
		loadSwordImage();
		last_attack=new Stopwatch();
		last_attack.start();
	}
	private void setPosition(Hero hero) {
		switch(dir){
		case Left:	x=hero.x-hero.step-8;
					y=hero.y+hero.step/2;
					mask=new Rectangle(x,y,32,16);
					break;
		case Right:	x=hero.x+24;
					y=hero.y+hero.step/2;
					mask=new Rectangle(x,y,32,16);
					break;
		case Up:	x=hero.x+8;
					y=hero.y-hero.step-8;
					mask=new Rectangle(x,y,16,32);
					break;
		case Down:	x=hero.x+8;
					y=hero.y+hero.step;
					mask=new Rectangle(x,y,16,32);
					break;
		}
	}
	private void loadSwordImage() {
		sword_img=new Vector<BufferedImage>();
		BufferedImage img;
		try {img = ImageIO.read(getClass().getResourceAsStream("/Map/WoodSword_Up.png"));
		sword_img.add(img);
		img=ImageIO.read(getClass().getResourceAsStream("/Map/WoodSword_Down.png"));
		sword_img.add(img);
		img=ImageIO.read(getClass().getResourceAsStream("/Map/WoodSword_Left.png"));
		sword_img.add(img);
		img=ImageIO.read(getClass().getResourceAsStream("/Map/WoodSword_Right.png"));
		sword_img.add(img);
		} catch (IOException e) {e.printStackTrace();}
	}
	public void render(Graphics g){
		 g.drawImage(getSwordDir(), x, y, null);
	}
	public void update(Attack attack){
		if(last_attack.elapsedMillis()>attack_delay){
			Hero.getInstance().isAttacking=false;
			attack.mySword=null;
			Hero.getInstance().attack_img=null;
			Hero.getInstance().movement.resetAnimation();
		}
	}
	public Image getSwordDir(){
		switch(dir){
		case Left: 	return sword_img.get(2);
		case Right:	return sword_img.get(3);
		case Up:	return sword_img.get(0);
		case Down:	return sword_img.get(1);
		}
		return null;
	}
}
