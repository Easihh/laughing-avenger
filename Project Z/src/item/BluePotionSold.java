package item;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Hero;
import main.Map;
import main.Shop;
import main.Tile;
import utility.Ressource;
import utility.Sound;
import utility.Stopwatch;

public class BluePotionSold extends Item{

	public BluePotionSold(int x, int y, ID type) {
		super(x, y, type);
		cost=50;
		this.type=type;
		loadImage();
	}

	private void loadImage() {
		try {img=ImageIO.read(getClass().getResourceAsStream("/map/BlueLifePotion.png"));
		}
		catch (IOException e) {e.printStackTrace();}	
	}
	@Override
	public void render(Graphics g) {
		g.drawImage(img,x,y,null);
		g.setColor(Color.WHITE);
		if(pickUpItemTimer==null)
			g.drawString(""+cost, x+8, y+64);
	}
	@Override
	public void use() {	}
	@Override
	public void update() {
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
				Hero.getInstance().inventory_items[0][5]=new BluePotion(401,98,Item.ID.BluePotion);;
				Hero.getInstance().inventory_items[0][5].hasOwnership=true;
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
