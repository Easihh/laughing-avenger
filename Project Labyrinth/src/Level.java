import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import javax.sound.sampled.Clip;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Level {
	private final long nano=1000000000L;
	private final int tileSize=32; 
	private int coordX;
	private int coordY;
	private Tile map_background;
	private static ArrayList<Tile> toRespawn;
	private static Vector<Long> Respawn_Timer;
	private static Tile goal_top;
	private static boolean canRespawn=true;
	private static boolean chestIsOpen=false;
	
	public static int room=1;
	public static int remake=0;
	public final static int map_width=512;
	public final static int map_height=512;
	public static int heart_amount;
	public static ArrayList<Tile> map_tile;
	public static ArrayList<Tile> toRemove;//Monster to be removed and then respawned
	public static ArrayList<Tile> toDelete;//tile to be deleted
	public static ArrayList<Tile> toAdd;
	public static int[] Power;/// 1=Hammer, 2=Ladder, 3=Arrow Changer
	public static Tile goal;
	
	public Level(){
			coordX=0;
			coordY=0;
			goal_top=null;
			heart_amount=0;
			map_tile=new ArrayList<Tile>();
			toRemove=new ArrayList<Tile>();
			toAdd=new ArrayList<Tile>();
			toDelete=new ArrayList<Tile>();
			toRespawn=new ArrayList<Tile>();
			Respawn_Timer=new Vector<Long>();
			Power=new int[3];
			verifyPowerAllowed();
			map_background=new Tile(Game.background);
			try{readMap();}
			catch (IOException | XMLStreamException e){
				e.printStackTrace();
			}
		Collections.sort(Level.map_tile);
		Labyrinth.level_is_loaded=true;
		Labyrinth.GameState=Game.GameState.NotStarted;
	}
	private void readMap() throws XMLStreamException, IOException {
		
		XMLInputFactory inputFactory= XMLInputFactory.newFactory();
		InputStream fileReader= this.getClass().getResourceAsStream("Level"+room+"map.tmx");
		XMLStreamReader reader= inputFactory.createXMLStreamReader(fileReader);
		while(reader.hasNext()){
			int eventType=reader.getEventType();
			switch (eventType){
			case XMLStreamConstants.START_ELEMENT:
				String elementName= reader.getLocalName();
				if(elementName.equals("tile")){
						createTile(reader.getAttributeValue(0));
						if(coordX==map_width-tileSize){
							coordX=0;
							coordY += tileSize;
						}
						else coordX+=tileSize;
				}
			}
			reader.next();
		}
		reader.close();
		
	}
	private void verifyPowerAllowed() {
		if(room==6){
			Power[0]=2;
			Power[1]=2;
			//Power[2]=3;
		}
		
	}
	private void createTile(String attributeValue) throws IOException {
		switch(attributeValue){
		case "0": 	//blank tile;show background
					break;	
		case "1":	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.Rock.value));
					break;
		case "2": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.ClosedDoor.value));
					break;
		case "3": 	
					heart_amount+=1;
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.AmmoHeart.value));
					break;
		case "4": 	
					heart_amount+=1;
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.NoAmmoHeart.value));
					break;
		case "6": 	MainPanel.hero=new Character(coordX,coordY);
					break;
		case "8": 	
					Level.map_tile.add(new Medusa(coordX,coordY,Tile.ID.SleepMedusa.value));
					break;
		case "9": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.MoveableBlock.value));
					break;
		case "10": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.Tree.value));
					break;
		case "11": 	
					goal=new Tile(coordX,coordY,Tile.ID.ClosedChest.value);
					break;
		case "16": 	
					Level.map_tile.add(new OneWayArrow(coordX,coordY,Tile.ID.OneWayUp.value));
					break;
		case "19": 	
					Level.map_tile.add(new Snakey(coordX,coordY,Tile.ID.LeftSnakey.value));
					break;
		case "20": 	
					Level.map_tile.add(new Snakey(coordX,coordY,Tile.ID.RightSnakey.value));
					break;
		case "21": 	
					Level.map_tile.add(new OneWayArrow(coordX,coordY,Tile.ID.OneWayLeft.value));
					break;
		case "22": 	
					Level.map_tile.add(new OneWayArrow(coordX,coordY,Tile.ID.OneWayRight.value));
					break;
		case "23": 	
					Level.map_tile.add(new DonMedusa(coordX,coordY,Tile.ID.LeftRightDonMedusa.value));
					break;
		case "24": 	
					Level.map_tile.add(new DonMedusa(coordX,coordY,Tile.ID.UpDownDonMedusa.value));
					break;
		case "25": 	
					Level.map_tile.add(new Gol(coordX,coordY,Tile.ID.GolUp.value));
					break;	
		case "26": 	
					Level.map_tile.add(new Gol(coordX,coordY,Tile.ID.GolDown.value));
					break;
		case "27": 	
					Level.map_tile.add(new Gol(coordX,coordY,Tile.ID.GolLeft.value));
					break;
		case "28": 	
					Level.map_tile.add(new Gol(coordX,coordY,Tile.ID.GolRight.value));
					break;			
		case "29": 	
					Level.map_tile.add(new OneWayArrow(coordX,coordY,Tile.ID.OneWayDown.value));
					break;
		case "30":	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.RockWall.value));
					break;
		case "32": 	
					Level.map_tile.add(new Leeper(coordX,coordY,Tile.ID.Leeper.value));
					break;
		case "33": 	
					Level.map_tile.add(new Water(coordX,coordY,Tile.ID.Water.value));
					break;
		case "34": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.LeftLadder.value));
					break;
		case "35": 	
					Level.map_tile.add(new Phantom(coordX,coordY,Tile.ID.Phantom.value));
					break;
		case "36": 	
					Level.map_tile.add(new Skull(coordX,coordY,Tile.ID.Skull.value));
					break;
		case "41": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.RightLadder.value));
					break;	
		case "42": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.UpDownLadder.value));
					break;
		case "43": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.Sand.value));
					break;			
		case "44": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.Grass.value));
					break;
		case "49": 	
					Level.map_tile.add(new Alma(coordX,coordY,Tile.ID.Alma.value));
					break;
		case "50": 	
					Level.map_tile.add(new Lava(coordX,coordY,Tile.ID.Lava.value));
					break;				
		}
	}
	public void render(Graphics g){
		map_background.render(g);
		goal.render(g);
		if(goal_top!=null)
			goal_top.render(g);
		if(toDelete!=null || toAdd!=null){
			map_tile.removeAll(toDelete);
			map_tile.addAll(toAdd);
			toAdd.clear();
			toDelete.clear();
			Collections.sort(map_tile);
		}
		for(Tile aTile:map_tile){
			aTile.render(g);
		}
		if(!toRemove.isEmpty()){
			map_tile.removeAll(toRemove);
			toRemove.clear();
		}
		if(!toRespawn.isEmpty() && canRespawn){
			checkRespawn();
		}
	}
	private void checkRespawn() {
		for(int i=0;i<Respawn_Timer.size();i++){
			if((System.nanoTime()-Respawn_Timer.elementAt(i))/nano >10){
				if(chestIsOpen)
					getCorrectType((Monster)toRespawn.get(i));
				map_tile.add(toRespawn.get(i));
				Respawn_Timer.remove(i);
				toRespawn.remove(i);
			}
		}
		
	}
	private void getCorrectType(Monster Monster) {
		
		switch(Monster.type){
		case 24: //awake dragon up
				Monster.canShoot=true;
				break;
		case 25: //awake dragon down
				Monster.img=Game.game_tileset.get(Tile.ID.GolDownAwaken.value);
				Monster.canShoot=true;
				break;
		case 26: //awake dragon left
				Monster.img=Game.game_tileset.get(Tile.ID.GolLeftAwaken.value);
				Monster.canShoot=true;
				break;
		case 27://awake dragon right
				Monster.img=Game.game_tileset.get(Tile.ID.GolRightAwaken.value);
				Monster.canShoot=true;
				break;
		case 35://awake skull
				Monster.img=Game.game_tileset.get(Tile.ID.Skull.value);
				Monster.isActive=true;
				break;		
		}
	}
	static void addRespawn(Tile tile) {
		toRespawn.add(tile);
		Respawn_Timer.add(System.nanoTime());
	}
	public static void openChest() {
		chestIsOpen=true;
		Sound.ChestOpen.stop();
		Sound.ChestOpen.setFramePosition(0);
		Sound.ChestOpen.start();
		goal=new Tile(goal.x,goal.y,Tile.ID.BottomChestOpen.value);
		goal_top=new Tile(goal.x,goal.y-32,Tile.ID.TopChest.value);	
		//awake all monster
		for(Tile aTile:map_tile){
			if(aTile instanceof Monster){
				Monster Monster=(Monster)aTile;
				switch(aTile.type){
					case 2: //Monster is in ball form; awake them
							AwakeBall(aTile);
					case 24: //awake dragon up
							Monster.canShoot=true;
							break;
					case 25: //awake dragon down
							Monster.img=Game.game_tileset.get(Tile.ID.GolDownAwaken.value);
							Monster.canShoot=true;
							break;
					case 26: //awake dragon left
							Monster.img=Game.game_tileset.get(Tile.ID.GolLeftAwaken.value);
							Monster.canShoot=true;
							break;
					case 27://awake dragon right
							Monster.img=Game.game_tileset.get(Tile.ID.GolRightAwaken.value);
							Monster.canShoot=true;
							break;
					case 35://awake skull
							Monster.img=Game.game_tileset.get(Tile.ID.Skull.value);
							Monster.isActive=true;
							break;	
				}
			}
		}
	}
	private static void AwakeBall(Tile aTile) {
		Monster Monster=(Monster)aTile;
		switch(aTile.oldtype){
		case 24: //awake dragon up
				Monster.canShoot=true;
				break;
		case 25: //awake dragon down
				Monster.previousState=Game.game_tileset.get(Tile.ID.GolDownAwaken.value);
				Monster.canShoot=true;
				break;
		case 26: //awake dragon left
				Monster.previousState=Game.game_tileset.get(Tile.ID.GolLeftAwaken.value);
				Monster.canShoot=true;
				break;
		case 27://awake dragon right
				Monster.previousState=Game.game_tileset.get(Tile.ID.GolRightAwaken.value);
				Monster.canShoot=true;
				break;	
		case 35://awake skull
				Monster.previousState=Game.game_tileset.get(Tile.ID.Skull.value);
				Monster.isActive=true;
				break;	
		}
		
	}
	public static void takeGoal() {
		goal=new Tile(goal.x,goal.y,Tile.ID.BottomChestEmpty.value);
		//open the goal
		for(Tile aTile:map_tile){
			if(aTile.img==Game.game_tileset.get(Tile.ID.ClosedDoor.value)){
				aTile.type=Tile.ID.OpenDoor.value;
				aTile.isSolid=false;
				aTile.img=Game.game_tileset.get(Tile.ID.OpenDoor.value);
			}
		//Delete all Monster
			if(aTile instanceof Monster){
				toRemove.add(aTile);
			}
		}
		map_tile.removeAll(toRemove);
		canRespawn=false;
		Sound.resetSound();
		Sound.DoorOpen.start();
	}
	public static void nextLevel() {
		remake=0;
		room+=1;
		Labyrinth.level_is_loaded=false;
		MainPanel.theLevel=new Level();
	}
	public static void restart() {
		remake++;
		Labyrinth.level_is_loaded=false;
		MainPanel.theLevel=new Level();
		Sound.resetSound();
		Sound.StageMusic.setFramePosition(0);
		Sound.StageMusic.loop(Clip.LOOP_CONTINUOUSLY);
	}
}
