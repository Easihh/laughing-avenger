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
/* Author Enrico Talbot
 * 
 * This class deals with how the Level is created and stored in the game.
 */
public class Level {
	private final long nano=1000000000L;
	private final int tileSize=32,maxPower=3; 
	private int coordX,coordY;
	private Tile map_background;
	private static ArrayList<Tile> toRespawn;
	private static Vector<Long> Respawn_Timer;
	private static Tile goal_top;
	private static boolean canRespawn,chestIsOpen;
	
	public static int room=1,remake=0,heart_amount;
	//Map Width and Map Height should match the TMX/XML file and should not be changed alone.
	public final static int map_width=512,map_height=512;
	public static ArrayList<Tile> map_tile,toRemove,toDelete,toAdd;
	public static Game.SpecialPower[] Power;
	public static Tile goal;
	
	public Level(){
			coordX=coordY=heart_amount=0;
			goal_top=null;
			map_tile=new ArrayList<Tile>();
			toRemove=new ArrayList<Tile>();
			toAdd=new ArrayList<Tile>();
			toDelete=new ArrayList<Tile>();
			toRespawn=new ArrayList<Tile>();
			Respawn_Timer=new Vector<Long>();
			Power=new Game.SpecialPower[maxPower];
			verifyPowerAllowed();//check if current level allow Special Power
			map_background=new Tile(Game.background);//Fill map with  background image at first.
			canRespawn=true;
			chestIsOpen=false;
			try{readMap();}
			catch (IOException | XMLStreamException e){
				e.printStackTrace();
			}
		Collections.sort(Level.map_tile);//tile will be drawn via lowest priority first(depth)
		Labyrinth.level_is_loaded=true;
		Labyrinth.GameState=Game.GameState.NotStarted;
	}
	/* The Level are created by loading the appropriate XML file that were created via a Program
	 * called Tiled and Then each object from the XML file is associated with a certain game Object
	 * and added to the correct row/column representation of the Level.
	 */
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
		if(room==7)
			Power[0]=Game.SpecialPower.Hammer;
		if(room==6){
			Power[0]=Game.SpecialPower.Ladder;
			Power[1]=Game.SpecialPower.Ladder;
		}
		
	}
	/* Decides which game object is created at the row/column position in the Level
	 * based on the reading from the XML file.
	 */
	private void createTile(String attributeValue) throws IOException {
		switch(attributeValue){
		case "0": 	//blank tile;show background
					break;	
		case "1":	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.Rock));
					break;
		case "2": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.ClosedDoor));
					break;
		case "3": 	
					heart_amount+=1;
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.AmmoHeart));
					break;
		case "4": 	
					heart_amount+=1;
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.NoAmmoHeart));
					break;
		case "6": 	Labyrinth.hero=new Character();
					Labyrinth.hero.x=(coordX);
					Labyrinth.hero.y=(coordY);
					break;
		case "8": 	
					Level.map_tile.add(new Medusa(coordX,coordY,Tile.ID.SleepMedusa));
					break;
		case "9": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.MoveableBlock));
					break;
		case "10": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.Tree));
					break;
		case "11": 	
					goal=new Tile(coordX,coordY,Tile.ID.ClosedChest);
					break;
		case "16": 	
					Level.map_tile.add(new OneWayArrow(coordX,coordY,Tile.ID.OneWayUp));
					break;
		case "19": 	
					Level.map_tile.add(new Snakey(coordX,coordY,Tile.ID.LeftSnakey));
					break;
		case "20": 	
					Level.map_tile.add(new Snakey(coordX,coordY,Tile.ID.RightSnakey));
					break;
		case "21": 	
					Level.map_tile.add(new OneWayArrow(coordX,coordY,Tile.ID.OneWayLeft));
					break;
		case "22": 	
					Level.map_tile.add(new OneWayArrow(coordX,coordY,Tile.ID.OneWayRight));
					break;
		case "23": 	
					Level.map_tile.add(new DonMedusa(coordX,coordY,Tile.ID.LeftRightDonMedusa));
					break;
		case "24": 	
					Level.map_tile.add(new DonMedusa(coordX,coordY,Tile.ID.UpDownDonMedusa));
					break;
		case "25": 	
					Level.map_tile.add(new Gol(coordX,coordY,Tile.ID.GolUp));
					break;	
		case "26": 	
					Level.map_tile.add(new Gol(coordX,coordY,Tile.ID.GolDown));
					break;
		case "27": 	
					Level.map_tile.add(new Gol(coordX,coordY,Tile.ID.GolLeft));
					break;
		case "28": 	
					Level.map_tile.add(new Gol(coordX,coordY,Tile.ID.GolRight));
					break;			
		case "29": 	
					Level.map_tile.add(new OneWayArrow(coordX,coordY,Tile.ID.OneWayDown));
					break;
		case "30":	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.RockWall));
					break;
		case "32": 	
					Level.map_tile.add(new Leeper(coordX,coordY,Tile.ID.Leeper));
					break;
		case "33": 	
					Level.map_tile.add(new Water(coordX,coordY,Tile.ID.Water));
					break;
		case "34": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.LeftLadder));
					break;
		case "35": 	
					Level.map_tile.add(new Phantom(coordX,coordY,Tile.ID.Phantom));
					break;
		case "36": 	
					Level.map_tile.add(new Skull(coordX,coordY,Tile.ID.Skull));
					break;
		case "41": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.RightLadder));
					break;	
		case "42": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.UpDownLadder));
					break;
		case "43": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.Sand));
					break;			
		case "44": 	
					Level.map_tile.add(new Tile(coordX,coordY,Tile.ID.Grass));
					break;
		case "49": 	
					Level.map_tile.add(new Alma(coordX,coordY,Tile.ID.Alma));
					break;
		case "50": 	
					Level.map_tile.add(new Lava(coordX,coordY,Tile.ID.Lava));
					break;
		case "51": 	
					Level.map_tile.add(new Water(coordX,coordY,Tile.ID.WaterFlowDown));
					break;
		case "52": 	
					Level.map_tile.add(new Water(coordX,coordY,Tile.ID.WaterFlowLeft));
					break;
		case "59": 	
					Level.map_tile.add(new Water(coordX,coordY,Tile.ID.WaterFlowRight));
					break;
		case "60": 	
					Level.map_tile.add(new Water(coordX,coordY,Tile.ID.WaterFlowUp));
					break;				
		}
	}
	/* This will draw the representation of the current Level.
	 * Top part of chest is only shown when all hearts are taken.
	 * This Method also try to respawn Monsters if its possible to
	 */
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
		for(int i=0;i<map_tile.size();i++)
			map_tile.get(i).render(g);
		if(!toRemove.isEmpty()){
			map_tile.removeAll(toRemove);
			toRemove.clear();
		}
		if(!toRespawn.isEmpty() && canRespawn)
			checkRespawn();
	}
	/* Monster should respawn after a certain amount of time has passed since 
	 * they were killed/removed from the game.
	 */
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
	/*We have to make sure that the Monster respawned is of the correct type in case of Monster
	 * that can "awaken" when all hearts are taken.For example its possible to kill a monster
	 * in non-awakened state and then open the chest(awaken) before the respawn can take place
	 * therefore the monster respawned should be in the awaken state should this situation happen.
	 */
	private void getCorrectType(Monster Monster) {
		
		switch(Monster.type){
		case GolUp: 	Monster.canShoot=true;
						break;
		case GolDown: 	Monster.img=Game.game_tileset.get(Tile.ID.GolDownAwaken.value);
						Monster.canShoot=true;
						break;
		case GolLeft: 	Monster.img=Game.game_tileset.get(Tile.ID.GolLeftAwaken.value);
						Monster.canShoot=true;
						break;
		case GolRight:	Monster.img=Game.game_tileset.get(Tile.ID.GolRightAwaken.value);
						Monster.canShoot=true;
						break;
		case Skull:		Monster.img=Game.game_tileset.get(Tile.ID.Skull.value);
						Monster.isActive=true;
						break;
		default:
			break;		
		}
	}
	public static void addRespawn(Tile tile) {
		toRespawn.add(tile);
		Respawn_Timer.add(System.nanoTime());
	}
	/* When the hero has obtained all the hearts in a given level,the goal(chest) changes
	 * state  and all monster that can awake are now activated.
	 */
	public static void openChest() {
		chestIsOpen=true;
		Sound.resetSound();
		Sound.ChestOpen.start();
		goal=new Tile(goal.x,goal.y,Tile.ID.BottomChestOpen);
		goal_top=new Tile(goal.x,goal.y-32,Tile.ID.TopChest);	
		//awake all monster
		for(int i=0;i<map_tile.size();i++){
			Tile aTile=map_tile.get(i);
			if(aTile instanceof Monster){
				Monster Monster=(Monster)aTile;
				switch(aTile.type){
					case MoveableBlock://Monster is in ball form; awake them
										AwakeBall(aTile);
					case GolUp:	 		Monster.canShoot=true;
										break;
					case GolDown:		Monster.img=Game.game_tileset.get(Tile.ID.GolDownAwaken.value);
										Monster.canShoot=true;
										break;
					case GolLeft: 		Monster.img=Game.game_tileset.get(Tile.ID.GolLeftAwaken.value);
										Monster.canShoot=true;
										break;
					case GolRight:		Monster.img=Game.game_tileset.get(Tile.ID.GolRightAwaken.value);
										Monster.canShoot=true;
										break;
					case Skull:			Monster.img=Game.game_tileset.get(Tile.ID.Skull.value);
										Monster.isActive=true;
										break;
					default:			break;	
				}
			}
		}
	}
	/* Since it is possible to shot a monster once ->transform into ball form -> open chest activate
	 * all sleeping monster, the monster that was transformed into ball form type should also be 
	 * activated*/
	private static void AwakeBall(Tile aTile) {
		Monster Monster=(Monster)aTile;
		switch(aTile.oldtype){
		case GolUp:		Monster.canShoot=true;
						break;
		case GolDown: 	Monster.previousState=Game.game_tileset.get(Tile.ID.GolDownAwaken.value);
						Monster.canShoot=true;
						break;
		case GolLeft:	Monster.previousState=Game.game_tileset.get(Tile.ID.GolLeftAwaken.value);
						Monster.canShoot=true;
						break;
		case GolRight:	Monster.previousState=Game.game_tileset.get(Tile.ID.GolRightAwaken.value);
						Monster.canShoot=true;
						break;	
		case Skull:		Monster.previousState=Game.game_tileset.get(Tile.ID.Skull.value);
						Monster.isActive=true;
						break;
		default:		break;	
		}
		
	}
	/* Upon taking the goal, the goal should changed into a "taken goal" state and all monsters 
	 * should be deleted from the game.Monster respawn should be disabled once the goal has been taken. 
	 */
	public static void takeGoal() {
		goal=new Tile(goal.x,goal.y,Tile.ID.BottomChestEmpty);
		//open the goal
		for(int i=0;i<map_tile.size();i++){
			Tile aTile=map_tile.get(i);
			if(aTile.img==Game.game_tileset.get(Tile.ID.ClosedDoor.value)){
				aTile.type=Tile.ID.OpenDoor;
				aTile.isSolid=false;
				aTile.img=Game.game_tileset.get(Tile.ID.OpenDoor.value);
			}
		//Delete all Monster
			if(aTile instanceof Monster && !((Monster) aTile).isDrowning){
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
