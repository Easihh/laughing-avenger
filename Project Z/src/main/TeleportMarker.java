package main;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import monster.BlueCandlePickUp;

import utility.Sound;

import drawing.GameDrawPanel;

public class TeleportMarker extends Tile{
	private int lastWorldX,lastWorldY;
	public TeleportMarker(int x, int y, Tile.ID type) {
		super(x, y, type);
	}
	public void render(Graphics g){}
	public void update(){
		Hero hero=Hero.getInstance();
		Map map=Map.getInstance();
		if(x==32 && hero.x==32 && y==32 && hero.y==32 && hero.lastTeleport==null){
			lastWorldX=map.worldX;
			lastWorldY=map.worldY;
			hero.lastTeleport=new Point(hero.x,hero.y+32);
			hero.x=1264;
			hero.y=448;
			map.worldX+=2;
			GameDrawPanel.worldTranslateX-=2*map.roomWidth;
			Sound.overWorldMusic.stop();
		}
		if(x==448 && hero.x==448 && y==32 && hero.y==32 && hero.lastTeleport==null){
			Map.allObject[38][23]=new BlueCandlePickUp(1216, 736, Item.ID.BlueCandle);
			Map.allObject[40][23]=new BlueCandlePickUp(1280, 736, Item.ID.BlueCandle);
			Map.allObject[42][23]=new BlueCandlePickUp(1344, 736, Item.ID.BlueCandle);
			hero.isInsideShop=true;
			lastWorldX=map.worldX;
			lastWorldY=map.worldY;
			hero.lastTeleport=new Point(hero.x,hero.y+32);
			hero.x=1264;
			hero.y=960;
			map.worldX+=2;
			map.worldY++;
			GameDrawPanel.worldTranslateX-=2*map.roomWidth;
			GameDrawPanel.worldTranslateY-=map.roomHeight;
			Sound.overWorldMusic.stop();
		}
		if(mask.intersects(new Rectangle(hero.x,hero.y,32,32)) && hero.lastTeleport!=null && hero.y==y){
			destroyAllShopItem();
			destroyEffects(hero);
			hero.isInsideShop=false;
			int differenceX=Map.getInstance().worldX-lastWorldX;
			int differenceY=Map.getInstance().worldY-lastWorldY;
			map.worldX=lastWorldX;
			map.worldY=lastWorldY;
			GameDrawPanel.worldTranslateX+=differenceX*map.roomWidth;
			GameDrawPanel.worldTranslateY+=differenceY*map.roomHeight;
			hero.x=hero.lastTeleport.x;
			hero.y=hero.lastTeleport.y;
			hero.lastTeleport=null;
			Sound.setOverWorldMusic();
		}
	}
	private void destroyEffects(Hero hero) {
		if(hero.specialItem!=null && hero.specialItem.type==Item.ID.BlueCandle)
			((BlueCandle)hero.specialItem).timer=null;
	}
	private void destroyAllShopItem() {
		Map map=Map.getInstance();
		for(int i=map.worldX*map.tilePerRow;i<map.worldX*map.tilePerRow+map.tilePerRow;i++){
			for(int j=map.worldY*map.tilePerCol;j<map.worldY*map.tilePerCol+map.tilePerCol;j++){
				if(Map.allObject[i][j]!=null && Map.allObject[i][j] instanceof Item)					
					Map.allObject[i][j]=null;
			}
		}
	}
}
