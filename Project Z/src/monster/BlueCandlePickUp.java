package monster;

import java.awt.Graphics;

import main.BlueCandle;
import main.Hero;
import main.Item;
import main.Map;

import utility.Ressource;
import utility.Sound;
import utility.Stopwatch;

public class BlueCandlePickUp extends Item{
	public Stopwatch pickUpItemTimer;
	public BlueCandlePickUp(int x, int y, Item.ID type) {
		super(x,y,type);
		this.x=x;
		this.y=y;
		this.type=type;
		img=Ressource.monster_type.get(type.value);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(x==Hero.getInstance().x && y==Hero.getInstance().y && pickUpItemTimer==null){
			Hero.getInstance().obtainItem=Ressource.obtainItem;
			pickUpItemTimer=new Stopwatch();
			pickUpItemTimer.start();
			Sound.newItem.setFramePosition(0);
			Sound.newInventItem.setFramePosition(0);
			Sound.newItem.start();
			Sound.newInventItem.start();
			Hero.getInstance().inventory_items[0][0]=new BlueCandle(241,98,Item.ID.BlueCandle);
			Hero.getInstance().inventory_items[0][0].hasOwnership=true;
		}
		if(pickUpItemTimer!=null){
			y=Hero.getInstance().y-height;
			if(Hero.getInstance().isInsideShop)
				destroyOtherItems();
		}
		if(pickUpItemTimer!=null && pickUpItemTimer.elapsedMillis()>1000){
			removeItem();
			Hero.getInstance().obtainItem=null;
		}
	}

	private void destroyOtherItems() {
		Map map=Map.getInstance();
		for(int i=map.worldX*map.tilePerRow;i<map.worldX*map.tilePerRow+map.tilePerRow;i++){
			for(int j=map.worldY*map.tilePerCol;j<map.worldY*map.tilePerCol+map.tilePerCol;j++){
				if(Map.allObject[i][j]!=null && Map.allObject[i][j] instanceof Item && Map.allObject[i][j]!=this)					
					Map.allObject[i][j]=null;
			}
		}
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img,x,y,null);
	}

	@Override
	public void use() {}
	
}
