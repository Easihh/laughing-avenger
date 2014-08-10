package main;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import utility.Sound;

public class SecretRoomMarker extends Tile{

	public SecretRoomMarker(int x, int y, ID type) {
		super(x, y, type);
		mask=new Rectangle(x,y,32,32);
	}
	public void render(Graphics g){
		g.drawImage(img,x,y,null);
	}
	public void update(){
		Hero hero=Hero.getInstance();
		Map map=Map.getInstance();
		if(hero.x==192 && hero.y==736){
			Map.allShop.get(Shop.ID.HeartSecretShop.value-1).updateCoordinate(map.roomWidth*map.worldX, map.roomHeight*map.worldY);
			hero.isInsideShop=Shop.ID.HeartSecretShop.value;
			hero.lastTeleport=new Point(hero.x,hero.y+32);
			hero.x=224;
			hero.y=960;
			Sound.overWorldMusic.stop();
		}
	}
}
