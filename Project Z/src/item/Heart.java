package item;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Hero;
import main.Map;

import utility.Animation;
import utility.Sound;

public class Heart extends Item{
	Animation myHeart;
	public Heart(int x, int y, ID type) {
		super(x, y, type);
		mask=new Rectangle(x,y,16,16);
		loadImage();
	}

	private void loadImage() {
		myHeart=new Animation();
		BufferedImage image=null;
		try {image=ImageIO.read(getClass().getResourceAsStream("/map/hearts.png"));
		for(int i=0;i<image.getHeight()/16;i++)
			for(int j=0;j<image.getWidth()/16;j++)
				myHeart.AddScene(image.getSubimage(j*16, i*16, 16, 16), 250);
		}
		catch (IOException e) {e.printStackTrace();}	
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(myHeart.getImage(),x,y,null);
	}

	@Override
	public void use() {	}

	@Override
	public void update() {
		checkCollisionWithHero();
		myHeart.setImage();
	}

	private void checkCollisionWithHero() {
		Hero hero=Hero.getInstance();
		if(new Rectangle(hero.x,hero.y,32,32).intersects(mask) || 
				hero.specialItem.type==Item.ID.MagicalBoomerang && 
				((MagicalBoomerang)hero.specialItem).myBoomerang!=null && ((MagicalBoomerang)hero.specialItem).myBoomerang.mask.intersects(mask)){
				Sound.getHeart.setFramePosition(0);
				Sound.getHeart.start();
				hero.currentHealth+=2;
				if(hero.currentHealth>hero.maxHealth)
					hero.currentHealth=hero.maxHealth;
				removeFromMap();
		}
	}

	private void removeFromMap() {
		Map map=Map.getInstance();
		for(int i=map.worldX*map.tilePerRow;i<map.worldX*map.tilePerRow+map.tilePerRow;i++){
			for(int j=map.worldY*map.tilePerCol;j<map.worldY*map.tilePerCol+map.tilePerCol;j++){
				if(Map.allObject[i][j]!=null && Map.allObject[i][j]==this)
					Map.allObject[i][j]=null;
			}
		}
	}
}
