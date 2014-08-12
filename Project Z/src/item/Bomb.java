package item;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import main.BombEffect;
import main.Hero;
import main.Projectile;
import utility.Sound;
import utility.Stopwatch;

public class Bomb extends Item{
	private Projectile myBomb;
	private Stopwatch timer;
	private Vector<BombEffect> bombEffect;
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
		if(myBomb==null && bombEffect!=null){
			for(int i=0;i<bombEffect.size();i++)
				bombEffect.get(i).render(g);
		}
	}

	@Override
	public void use() {
		Sound.bombDrop.setFramePosition(0);
		Sound.bombDrop.start();
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
		if(hero.bomb_amount>0){
			myBomb=new Projectile(new Rectangle(x,y,32,32),(BufferedImage)img, hero.dir);
			hero.bomb_amount--;
		}
	}

	@Override
	public void update() {
		if(timer!=null && timer.elapsedMillis()>1000){
			timer=null;
			myBomb=null;
			bombEffect=new Vector<BombEffect>();
			bombEffect.add(new BombEffect(x, y));
			bombEffect.add(new BombEffect(x+32, y));
			bombEffect.add(new BombEffect(x-32, y));
			bombEffect.add(new BombEffect(x, y+32));
			bombEffect.add(new BombEffect(x, y-32));
			Sound.bombBlow.setFramePosition(0);
			Sound.bombBlow.start();
		}
		if(bombEffect!=null){
			for(int i=0;i<bombEffect.size();i++)
				if(bombEffect.get(i).timer!=null)
					bombEffect.get(i).update();
		}
		if(bombEffect!=null && bombEffect.get(0).timer==null)
			bombEffect=null;
		
	}
}
