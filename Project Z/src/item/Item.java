package item;

import java.awt.Graphics;

import main.Hero;
import main.Map;
import main.Tile;
import monster.Merchant;

public abstract class Item extends Tile{
	public Item(int x, int y, Item.ID type) {
		super(x, y, type);
		this.type=type;
	}
	public boolean hasOwnership=false,hasBeenPickedUp=false;
	public int cost;
	public final int width=32,height=32;
	public String name;
	public ID type;
	public enum ID{ BlueCandle(0),WoodSword(1),Arrow(2);
		public int value;
		private ID(int value){
		this.value=value;
		}
	}
	public abstract void render(Graphics g);
	public abstract void use();
	public abstract void update();
	public void removeItemFromShop() {
		Map map=Map.getInstance();
		Tile[][] theRoom=Map.allShop.get(Hero.getInstance().isInsideShop-1).theRoom;
		for(int i=0;i<map.tilePerRow;i++){
			for(int j=0;j<map.tilePerCol;j++){
				if(theRoom[i][j]!=null && theRoom[i][j]==this)
					theRoom[i][j]=null;
			}
		}
		
	}
	public void removeMerchant(){
		Map map=Map.getInstance();
		Tile[][] theRoom=Map.allShop.get(Hero.getInstance().isInsideShop-1).theRoom;
		for(int i=0;i<map.tilePerRow;i++){
			for(int j=0;j<map.tilePerCol;j++){
				if(theRoom[i][j]!=null && theRoom[i][j] instanceof Merchant)
					theRoom[i][j]=null;
			}
		}
	}
}
