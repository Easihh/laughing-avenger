package main;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

import utility.InventoryInput;
import utility.Sound;

public class Selector {
	public Image mySelect;
	public int x,y;
	public Rectangle mask;
	public final int width=32,height=32,inventoryWidth=192,inventoryHeight=96,startX=241,startY=98;
	public Selector(){
		x=startX;
		y=startY;
		loadImage();
		updateMask();
	}
	private void loadImage() {
		try {mySelect=ImageIO.read(getClass().getResourceAsStream("/Map/selector.png"));
		} catch (IOException e) {e.printStackTrace();}
	}
	public void updateMask(){
		mask=new Rectangle(x,y,width,height);
	}
	public void tryMoveLeft() {
		if(x>startX){
			x-=width;
			Sound.selector.setFramePosition(0);
			Sound.selector.flush();
			Sound.selector.start();
			updateMask();
			InventoryInput.currentColIndex--;
		}
	}
	public void tryMoveRight() {
		if(x+width<startX+inventoryWidth){
			x+=width;	
			Sound.selector.setFramePosition(0);
			Sound.selector.flush();
			Sound.selector.start();
			updateMask();
			InventoryInput.currentColIndex++;
		}
	}
	public void tryMoveDown() {
		if(y+height<startY+inventoryHeight){
			y+=width;	
			Sound.selector.setFramePosition(0);
			Sound.selector.flush();
			Sound.selector.start();
			updateMask();
			InventoryInput.currentRowIndex++;
		}
	}
	public void tryMoveUp() {
		if(y>startY){
			y-=width;
			Sound.selector.setFramePosition(0);
			Sound.selector.flush();
			Sound.selector.start();
			updateMask();
			InventoryInput.currentRowIndex--;
		}
	}
	public void itemCollision() {
		Hero hero=Hero.getInstance();
		for(int i=0;i<hero.inventoryRow;i++){
			for(int j=0;j<hero.inventoryCol;j++){
				if(hero.inventory_items[i][j]!=null)
					if(hero.inventory_items[i][j].hasOwnership)
						if(hero.inventory_items[i][j].mask.intersects(mask)){
							hero.specialItem=hero.inventory_items[i][j];
							break;
						}
			}
		}
	}
}
