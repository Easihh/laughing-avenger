package item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import main.Hero;
import main.Map;
import main.Projectile;
import main.Shop;
import main.Tile;

import utility.Ressource;
import utility.Sound;
import utility.Stopwatch;

public class Arrow extends Item{
	private Stopwatch pickUpItemTimer;
	private Projectile myArrow;
	private Vector<BufferedImage> arrows_img;
	private Arrow anArrow;
	public Arrow(int x, int y, ID type) {
		super(x, y, type);
		arrows_img=new Vector<BufferedImage>();
		cost=40;
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
		if(!hasBeenPickedUp){
			g.drawImage(img,x,y,null);
			g.setColor(Color.WHITE);
			if(pickUpItemTimer==null)
				g.drawString(""+cost, x+8, y+64);
		}
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
			myArrow=new Projectile(hero.x, hero.y, getArrowImage(hero),hero.dir);
		}
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
		if(x==Hero.getInstance().x && y==Hero.getInstance().y && pickUpItemTimer==null){
			if(Hero.getInstance().rupee_amount>=cost){
				Hero.getInstance().rupee_amount-=cost;
				Hero.getInstance().obtainItem=Ressource.obtainItem;
				pickUpItemTimer=new Stopwatch();
				pickUpItemTimer.start();
				Sound.newItem.setFramePosition(0);
				Sound.newInventItem.setFramePosition(0);
				Sound.newItem.start();
				Sound.newInventItem.start();
				anArrow=new Arrow(273,98,Item.ID.Arrow);
				Hero.getInstance().inventory_items[1][0]=anArrow;
				anArrow.hasOwnership=true;
			}
		}
		if(pickUpItemTimer!=null){
			y=Hero.getInstance().y-height;
			if(Hero.getInstance().isInsideShop!=Shop.ID.None.value)
				destroyOtherItems();
		}
		if(pickUpItemTimer!=null && pickUpItemTimer.elapsedMillis()>1000){
			removeItemFromShop();
			removeMerchant();
			Hero.getInstance().obtainItem=null;
			anArrow.hasBeenPickedUp=true;
		}
	}

	private void destroyOtherItems() {
		Map map=Map.getInstance();
		Tile[][] theRoom=Map.allShop.get(Hero.getInstance().isInsideShop-1).theRoom;
		for(int i=0;i<map.tilePerRow;i++){
			for(int j=0;j<map.tilePerCol;j++){
				if(theRoom[i][j]!=null && theRoom[i][j] instanceof Item && theRoom[i][j]!=this)				
					theRoom[i][j]=null;
			}
		}	
	}

}
