package item;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

import utility.Ressource;
import utility.Sound;
import utility.Stopwatch;
import main.Fire;
import main.Hero;
import main.Hero.Direction;
import main.Map;
import main.SecretRoomMarker;
import main.Tile;
import monster.Monster;

public class BlueCandle extends Item {
	public Fire fire;
	public Stopwatch timer;
	private int stepToMove;
	public BlueCandle(int x,int y,Item.ID type){
		super(x, y, type);
		name="Blue Candle";
		cost=60;
		inventoryX=x;
		InventoryY=y;
		this.x=x;
		this.y=y;
		mask=new Rectangle(x,y,width,height);
		loadImage();
	}
	
	private void loadImage() {
		try {img=ImageIO.read(getClass().getResourceAsStream("/map/BlueCandle.png"));
		} 
		catch (IOException e) {e.printStackTrace();}	
	}
	public void use(){
		Sound.candle.setFramePosition(0);
		Sound.candle.flush();
		Sound.candle.start();
		Hero hero=Hero.getInstance();
		stepToMove=64;
		timer=new Stopwatch();
		timer.start();
		if(hero.dir==Direction.Down)
			fire=new Fire(hero.x, hero.y+height, Monster.ID.Fire);
		if(hero.dir==Direction.Up)
			fire=new Fire(hero.x, hero.y-height, Monster.ID.Fire);
		if(hero.dir==Direction.Left)
			fire=new Fire(hero.x-width, hero.y, Monster.ID.Fire);
		if(hero.dir==Direction.Right)
			fire=new Fire(hero.x+width, hero.y, Monster.ID.Fire);
		fire.dir=hero.dir;
	}
	public void update(){
		if(fire!=null) {
			updateFireMovement();
			checkSecretRoom();
		}
		if(fire!=null && timer!=null && timer.elapsedMillis()>3000){
			timer=null;
			fire=null;
		}
	}
	private void checkSecretRoom() {
		Map map=Map.getInstance();
		for(int i=map.worldX*map.tilePerRow;i<map.worldX*map.tilePerRow+map.tilePerRow;i++){
			for(int j=map.worldY*map.tilePerCol;j<map.worldY*map.tilePerCol+map.tilePerCol;j++){
				if(Map.allObject[i][j]!=null && Map.allObject[i][j].x==192 && Map.allObject[i][j].y==736)
					if(Map.allObject[i][j].mask.intersects(fire.mask) && Map.allObject[i][j].type==Tile.ID.Tree){
						Sound.secret.setFramePosition(0);
						Sound.secret.start();
						Map.allObject[i][j]=new SecretRoomMarker(Map.allObject[i][j].x, Map.allObject[i][j].y, Tile.ID.SecretRoom);
						Map.allObject[i][j].img=Ressource.game_tileset.get(5);
					}
			}
		}
	}
	private void updateFireMovement() {
		if(fire.dir==Direction.Down && stepToMove!=0){
			fire.y++;
			stepToMove--;
		}
		if(fire.dir==Direction.Up && stepToMove!=0){
			fire.y--;
			stepToMove--;
		}
		if(fire.dir==Direction.Left && stepToMove!=0){
			fire.x--;
			stepToMove--;
		}
		if(fire.dir==Direction.Right && stepToMove!=0){
			fire.x++;
			stepToMove--;
		}
		fire.mask=new Rectangle(fire.x,fire.y,32,32);
	}

	public void render(Graphics g){
		if(fire!=null){
			fire.fireAnimation.setImage();
			g.drawImage(fire.fireAnimation.getImage(),fire.x,fire.y,null);
		}
	}
}
