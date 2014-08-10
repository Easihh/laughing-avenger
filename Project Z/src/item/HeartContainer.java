package item;

import java.awt.Graphics;

import main.Hero;
import main.Map;
import main.Shop;
import main.Tile;

import utility.Ressource;
import utility.Sound;
import utility.Stopwatch;

public class HeartContainer extends Item{
	public HeartContainer(int x, int y, ID type) {
		super(x, y, type);
		img=Ressource.monster_type.get(6);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img,x,y,null);
	}

	@Override
	public void use() {	}

	@Override
	public void update() {
		checkCollisionWithHero();
	}

	private void checkCollisionWithHero() {
		Hero hero=Hero.getInstance();
		if(x==hero.x && y==hero.y && pickUpItemTimer==null){
				hero.obtainItem=Ressource.obtainItem;
				pickUpItemTimer=new Stopwatch();
				pickUpItemTimer.start();
				Sound.newItem.setFramePosition(0);
				Sound.newInventItem.setFramePosition(0);
				Sound.newItem.start();
				Sound.newInventItem.start();
				hero.maxHealth+=2;
				hero.currentHealth+=2;
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
