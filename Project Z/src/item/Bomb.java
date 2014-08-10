package item;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Hero;
import main.Projectile;
import utility.Stopwatch;

public class Bomb extends Item{
	private Projectile myBomb;
	private Stopwatch timer;
	public Bomb(int x, int y, ID type) {
		super(x, y, type);
		inventoryX=x;
		InventoryY=y;
		this.type=type;
		loadImage();
	}

	private void loadImage() {
		try {img=ImageIO.read(getClass().getResourceAsStream("/map/Bomb.png"));}
		catch (IOException e) {e.printStackTrace();}	
	}

	@Override
	public void render(Graphics g) {
		if(myBomb!=null)
			myBomb.render(g);
	}

	@Override
	public void use() {
		System.out.println("using bomb");
		timer=new Stopwatch();
		timer.start();
		setBombPosition();
	}

	private void setBombPosition() {
		Hero hero=Hero.getInstance();
		switch(hero.dir){
		case Left:	x=hero.x-32;
					y=hero.y;
					break;
		case Right: x=hero.x+32;
					y=hero.y;
					break;
		case Down:	x=hero.x;
					y=hero.y+32;
					break;
		case Up:	x=hero.x;
					y=hero.y-32;
					break;
		}
		myBomb=new Projectile(new Rectangle(x,y,32,32),(BufferedImage)img, hero.dir);
	}

	@Override
	public void update() {
		if(timer!=null && timer.elapsedMillis()>3000){
			timer=null;
			myBomb=null;
		}
	}
}
