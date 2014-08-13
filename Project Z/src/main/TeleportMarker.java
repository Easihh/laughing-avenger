package main;

import item.BlueCandle;
import item.Item;
import item.MagicalBoomerang;
import item.MagicalRod;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import utility.Sound;

public class TeleportMarker extends Tile{

	public TeleportMarker(int x, int y, ID type) {
		super(x, y, type);
		mask=new Rectangle(x,y,32,32);
	}
	public void render(Graphics g){
		g.drawImage(img,x,y,null);
	}
	public void update(){
		Hero hero=Hero.getInstance();
		Map map=Map.getInstance();
		if(hero.x==736 && hero.y==800 && hero.lastTeleport==null){
			destroyEffects(hero);
			Map.allShop.get(Shop.ID.HeartSecretShop.value-1).updateCoordinate(map.roomWidth*map.worldX, map.roomHeight*map.worldY);
			hero.isInsideShop=Shop.ID.HeartSecretShop.value;
			hero.lastTeleport=new Point(hero.x,hero.y+32);
			hero.x=736;
			hero.y=960;
			Sound.overWorldMusic.stop();
		}
		if(hero.x==288  && hero.y==224 && hero.lastTeleport==null){
			destroyEffects(hero);
			Map.allShop.get(1).updateCoordinate(map.roomWidth*map.worldX, map.roomHeight*map.worldY);
			hero.isInsideShop=Shop.ID.WoodSwordShop.value;
			hero.lastTeleport=new Point(hero.x,hero.y+32);
			hero.x=224;
			hero.y=448;
			Sound.overWorldMusic.stop();
		}
		if(hero.x==352  && hero.y==576 && hero.lastTeleport==null){
			destroyEffects(hero);
			Map.allShop.get(0).loadFile(Shop.ID.CandleShop,map.roomWidth*map.worldX,map.roomHeight*map.worldY);
			Map.allShop.get(0).updateCoordinate(map.roomWidth*map.worldX, map.roomHeight*map.worldY);
			hero.isInsideShop=Shop.ID.CandleShop.value;
			hero.lastTeleport=new Point(hero.x,hero.y+32);
			hero.x=224;
			hero.y=960;
			Sound.overWorldMusic.stop();
		}
		if(mask.intersects(new Rectangle(hero.x,hero.y,32,32)) && hero.lastTeleport!=null && hero.y==y){
			destroyAllShopItem();
			destroyEffects(hero);
			hero.isInsideShop=Shop.ID.None.value;
			hero.x=hero.lastTeleport.x;
			hero.y=hero.lastTeleport.y;
			hero.lastTeleport=null;
			Sound.setOverWorldMusic();
		}
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
	private void destroyEffects(Hero hero) {
		if(hero.specialItem!=null && hero.specialItem.type==Item.ID.BlueCandle){
			((BlueCandle)hero.specialItem).timer=null;
			((BlueCandle)hero.specialItem).fire=null;
		}
		if(hero.specialItem!=null && hero.specialItem.type==Item.ID.MagicalRod){
			((MagicalRod)hero.specialItem).projectile=null;	
		}
		if(hero.specialItem!=null && hero.specialItem.type==Item.ID.MagicalBoomerang){
			((MagicalBoomerang)hero.specialItem).myBoomerang=null;
				Sound.boomerang.stop();
		}
		hero.attack.theSword=null;
		hero.attack.sEffect=null;
	}
}
