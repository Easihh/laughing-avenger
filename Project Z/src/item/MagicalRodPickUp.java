package item;

import java.awt.Color;
import java.awt.Graphics;

import main.Hero;
import main.Map;
import main.Shop;
import main.Tile;

import utility.Ressource;
import utility.Sound;
import utility.Stopwatch;

public class MagicalRodPickUp extends Item{

	public MagicalRodPickUp(int x, int y, ID type) {
		super(x, y, type);
		img=Ressource.monster_type.get(8);
		this.type=type;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img,x,y,null);
		g.setColor(Color.WHITE);
	}

	@Override
	public void use() {}

	@Override
	public void update() {
		if(x==Hero.getInstance().x && y==Hero.getInstance().y && pickUpItemTimer==null){
				Hero.getInstance().rupee_amount-=cost;
				Hero.getInstance().obtainItem=Ressource.obtainItem;
				pickUpItemTimer=new Stopwatch();
				pickUpItemTimer.start();
				Sound.newItem.setFramePosition(0);
				Sound.newInventItem.setFramePosition(0);
				Sound.newItem.start();
				Sound.newInventItem.start();
				Hero.getInstance().inventory_items[0][3]=new MagicalRod(337,98,Item.ID.MagicalRod);;
				Hero.getInstance().inventory_items[0][3].hasOwnership=true;
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
