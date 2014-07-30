package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class Item extends Tile{
	public Item(int x, int y, Item.ID type) {
		super(x, y, type);
		this.type=type;
	}
	public Image img;
	public boolean hasOwnership=false;
	public int x,y,cost;
	public final int width=32,height=32;
	public Rectangle mask;
	public String name;
	public ID type;
	public enum ID{ BlueCandle(0);
		public int value;
		private ID(int value){
		this.value=value;
		}
	}
	public abstract void render(Graphics g);
	public abstract void use();
	public abstract void update();
	public void removeItem(){
		Map map=Map.getInstance();
		for(int i=map.worldX*map.tilePerRow;i<map.worldX*map.tilePerRow+map.tilePerRow;i++){
			for(int j=map.worldY*map.tilePerCol;j<map.worldY*map.tilePerCol+map.tilePerCol;j++){
				if(Map.allObject[i][j]!=null && Map.allObject[i][j]==this)
					Map.allObject[i][j]=null;
			}
		}
	}
}
