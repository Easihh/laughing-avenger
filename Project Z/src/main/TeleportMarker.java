package main;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import drawing.GameDrawPanel;

public class TeleportMarker extends Tile{

	public TeleportMarker(int x, int y, Tile.ID type) {
		super(x, y, type);
	}
	public void render(Graphics g){}
	public void update(){
		Hero hero=Hero.getInstance();
		Map map=Map.getInstance();
		if(x==32 && hero.x==32 && y==32 && hero.y==32 && hero.lastTeleport==null){
			hero.lastTeleport=new Point(hero.x,hero.y+32);
			hero.x=1248;
			hero.y=448;
			map.worldX+=2;
			GameDrawPanel.worldTranslateX-=2*map.roomWidth;
		}
		if(mask.intersects(new Rectangle(hero.x,hero.y,32,32)) && hero.lastTeleport!=null && hero.y==y){
			int differenceX=(hero.x-hero.lastTeleport.x)/map.roomWidth;
			int differenceY=(hero.y-hero.lastTeleport.x)/map.roomHeight;
			map.worldX+=-(differenceX);
			map.worldY+=-(differenceY);
			GameDrawPanel.worldTranslateX+=differenceX*map.roomWidth;
			GameDrawPanel.worldTranslateY+=differenceY*map.roomHeight;
			hero.x=hero.lastTeleport.x;
			hero.y=hero.lastTeleport.y;
			hero.lastTeleport=null;
		}
	}
}
