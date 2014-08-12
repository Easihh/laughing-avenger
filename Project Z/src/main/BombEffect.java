package main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import utility.Ressource;
import utility.Sound;
import utility.Stopwatch;

public class BombEffect {
	private int x,y;
	public Stopwatch timer;
	private Vector<BufferedImage> images;
	public Rectangle mask;
	public BombEffect(int x,int y){
		this.x=x;
		this.y=y;
		loadImages();
		timer=new Stopwatch();
		timer.start();
		mask=new Rectangle(x,y,32,32);
	}
	private void loadImages() {
		images=new Vector<BufferedImage>();
		try {images.add(ImageIO.read(getClass().getResourceAsStream("/Map/Bomb_Effect1.png")));
			images.add(ImageIO.read(getClass().getResourceAsStream("/Map/Bomb_Effect2.png")));}
		catch (IOException e) {e.printStackTrace();}
	}
	public void render(Graphics g){
		if(timer.elapsedMillis()<250)
			g.drawImage(images.get(0), x,y,null);
		else g.drawImage(images.get(1), x,y,null);
	}
	public void update(){
		if(timer!=null && timer.elapsedMillis()>300)
			timer=null;
		if(timer!=null)checkSecretRoom();
	}
	private void checkSecretRoom() {
		Map map=Map.getInstance();
		for(int i=map.worldX*map.tilePerRow;i<map.worldX*map.tilePerRow+map.tilePerRow;i++){
			for(int j=map.worldY*map.tilePerCol;j<map.worldY*map.tilePerCol+map.tilePerCol;j++){
				if(Map.allObject[i][j]!=null && Map.allObject[i][j].x==288 && Map.allObject[i][j].y==224)
					if(Map.allObject[i][j].mask.intersects(mask) && Map.allObject[i][j].type==Tile.ID.Type1GreenBlock){
						Sound.secret.setFramePosition(0);
						Sound.secret.start();
						Map.allObject[i][j]=new TeleportMarker(Map.allObject[i][j].x, Map.allObject[i][j].y, Tile.ID.SecretRoom);
						Map.allObject[i][j].img=Ressource.game_tileset.get(0);
					}
			}
		}
	}
}
