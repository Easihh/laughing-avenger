package item;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import main.Hero;
import main.Projectile;

import utility.Sound;

public class Arrow extends Item{
	public Projectile myArrow;
	private Vector<BufferedImage> arrows_img;
	public Arrow(int x, int y, ID type) {
		super(x, y, type);
		inventoryX=x;
		InventoryY=y;
		arrows_img=new Vector<BufferedImage>();
		this.type=type;
		loadImage();
	}
	private void loadImage() {
		try {img=ImageIO.read(getClass().getResourceAsStream("/map/Arrow.png"));
		BufferedImage image=ImageIO.read(getClass().getResourceAsStream("/map/Arrows.png"));
		for(int i=0;i<image.getWidth()/32;i++)
			for(int j=0;j<image.getHeight()/32;j++)
				arrows_img.add(image.getSubimage(j*32, i*32, 32, 32));
		}
		catch (IOException e) {e.printStackTrace();}	
	}

	@Override
	public void render(Graphics g) {
		if(myArrow!=null)myArrow.render(g);
	}

	@Override
	public void use() {	
		Hero hero=Hero.getInstance();
		Sound.arrow.setFramePosition(0);
		Sound.arrow.flush();
		Sound.arrow.start();
		if(hero.rupee_amount>=1){
			hero.rupee_amount--;
			myArrow=new Projectile(getInfo(hero), getArrowImage(hero),hero.dir);
		}
	}

	private Rectangle getInfo(Hero hero) {
		Rectangle myrect=null;
		switch(hero.dir){
		case Left:	myrect=new Rectangle(hero.x,hero.y,32,16);
					break;
		case Right:	myrect=new Rectangle(hero.x,hero.y,32,16);
					break;
		case Up:	myrect=new Rectangle(hero.x,hero.y,16,32);
					break;
		case Down:	myrect=new Rectangle(hero.x,hero.y,16,32);
					break;			
		}
		return myrect;
	}

	private BufferedImage getArrowImage(Hero hero) {
		switch(hero.dir){
		case Left: 	return arrows_img.get(2);
		case Right: return arrows_img.get(3);
		case Up: 	return arrows_img.get(0);
		case Down: 	return arrows_img.get(1);			
		}
		return null;
	}

	@Override
	public void update() {
		if(myArrow!=null)myArrow.update();
		if(myArrow!=null && myArrow.outOfBound())myArrow=null;
	}
}
