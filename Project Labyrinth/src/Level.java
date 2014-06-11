import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Level {
	private final long nano=1000000000L;
	private final int tileSize=32; //24x24
	private final int tileset_rows=4;
	private final int tileset_cols=8;
	private int coordX=0;
	private int coordY=0;
	private Tile map_background;
	private static ArrayList<Tile> toRespawn=new ArrayList<Tile>();
	private static Vector<Long> Respawn_Timer=new Vector<Long>();
	private static Tile goal_top;
	private static boolean canRespawn=true;
	
	public final static int map_width=512;
	public final static int map_height=512;
	public static int heart_amount=0;
	public static ArrayList<Tile> map_tile=new ArrayList<Tile>();
	public static ArrayList<Tile> toRemove=new ArrayList<Tile>();
	public static BufferedImage[] game_tileset=new BufferedImage[50];
	public static BufferedImage[] monsterState;
	public static Tile goal;
	
	public Level(){
		try {
			setBackground("start");
			getGameTile();
			loadSound();
		} catch (IOException GameBackground) {
			GameBackground.printStackTrace();
		}
		XMLInputFactory inputFactory= XMLInputFactory.newFactory();
		try{
			FileReader fileReader= new FileReader("src/testmap.TMX");
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
		catch (IOException | XMLStreamException e){
			e.printStackTrace();
		}
	}
	private void loadSound() {
		@SuppressWarnings("unused")
		Sound aSound; 
		aSound=new Sound("StageMusic");
		aSound=new Sound("HeartSound");
		aSound=new Sound("DoorOpen");
		aSound=new Sound("MedusaSound");
		aSound=new Sound("DragonSound");
		aSound=new Sound("ChestOpen");
		aSound=new Sound("MonsterDestroyed");
		aSound=new Sound("ShotSound");
		aSound=new Sound("Death");
		Sound.StageMusic.loop(Clip.LOOP_CONTINUOUSLY);	
	}
	private void getGameTile() throws IOException {
		BufferedImage img=ImageIO.read(getClass().getResource("/tileset/game_tileset.png"));
		 for(int i=0;i<tileset_rows;i++){
			 for(int j=0;j<tileset_cols;j++){
				game_tileset[(i*tileset_cols)+j]=img.getSubimage(j*tileSize, i*tileSize, tileSize, tileSize);
			 }
		 }
	}
	private void setBackground(String background) throws IOException {
		BufferedImage img=null;
		switch(background){
		case "start":	
					img=ImageIO.read(getClass().getResource("/tileset/testfloor.png"));
	}
	map_background=new Tile(img);
}
	private void createTile(String attributeValue) throws IOException {

		switch(attributeValue){
		case "0": 	//blank tile;show background
					break;	
		case "1":	//rock
					Level.map_tile.add(new Tile(coordX,coordY,1));
					break;
		case "2": 	//door
					Level.map_tile.add(new Tile(coordX,coordY,96));
					break;
		case "3": 	//heartcard
					heart_amount+=1;
					Level.map_tile.add(new Tile(coordX,coordY,3));
					break;
		case "6": 	MainPanel.hero=new Character(coordX,coordY);
					break;
		case "8": 	//medusa sleeping
					Level.map_tile.add(new Medusa(coordX,coordY,0));
					break;
		case "9": 	//moveable green block
					Level.map_tile.add(new Tile(coordX,coordY,2));
					break;
		case "10": 	//tree
					Level.map_tile.add(new Tile(coordX,coordY,6));
					break;
		case "11": 	//goal chest closed
					goal=new Tile(coordX,coordY,4);
					break;
		case "16": 	//one-way up arrow
					Level.map_tile.add(new OneWayArrow(coordX,coordY,11));
					break;
		case "19": 	//Green worm monster Left
					Level.map_tile.add(new Snakey(coordX,coordY,19));
					break;
		case "20": 	//Green worm monster right
					Level.map_tile.add(new Snakey(coordX,coordY,20));
					break;
		case "21": 	//one-way left arrow
					//type=12;
					Level.map_tile.add(new OneWayArrow(coordX,coordY,12));
					break;
		case "22": 	//one-way right arrow
					Level.map_tile.add(new OneWayArrow(coordX,coordY,13));
					break;
		case "23": 	//DonMesusa Left-Right
					Level.map_tile.add(new DonMedusa(coordX,coordY,1));
					break;
		case "24": 	//DonMesusa Up-Down
					Level.map_tile.add(new DonMedusa(coordX,coordY,2));
					break;
		case "29": 	//one-way down arrow
					Level.map_tile.add(new OneWayArrow(coordX,coordY,14));
					break;
		case "30":	//rock wall
					Level.map_tile.add(new Tile(coordX,coordY,30));
					break;
		case "25": 	//gol sleeping up
					Level.map_tile.add(new Gol(coordX,coordY,7));
					break;
		case "26": 	//gol sleeping down
					Level.map_tile.add(new Gol(coordX,coordY,8));
					break;
		case "27": 	//gol sleeping left
					Level.map_tile.add(new Gol(coordX,coordY,9));
					break;
		case "28": 	//gol sleeping right
					Level.map_tile.add(new Gol(coordX,coordY,10));
					break;
		//case "32": 	img=game_tileset[31];//Leeper
					//isMonster=true;
			//		type=1;
				//	break;
		}
	}
	public void render(Graphics g){
		map_background.render(g);
		goal.render(g);
		if(goal_top!=null)
			goal_top.render(g);
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
				map_tile.add(toRespawn.get(i));
				Respawn_Timer.remove(i);
				toRespawn.remove(i);
			}
		}
		
	}
	static void addRespawn(Tile tile) {
		toRespawn.add(tile);
		Respawn_Timer.add(System.nanoTime());
	}
	public static void openChest() {
		Sound.ChestOpen.stop();
		Sound.ChestOpen.setFramePosition(0);
		Sound.ChestOpen.start();
		//BufferedImage img=game_tileset[11];//bottom part of chest
		goal=new Tile(goal.x,goal.y,5);
		//img=game_tileset[16];//top part of chest
		goal_top=new Tile(goal.x,goal.y-32,98);	
		//awake all monster
		for(Tile aTile:map_tile){
			switch(aTile.getType()){
			case 2: //Monster is in ball form; awake them
					AwakeBall(aTile);
			case 7: //awake dragon up
					aTile.canShoot=true;
					break;
			case 8: //awake dragon down
					aTile.img=game_tileset[12];
					//aTile.old_img=game_tileset[12];
					aTile.canShoot=true;
					break;
			case 9: //awake dragon left
					aTile.img=game_tileset[13];
					//aTile.old_img=game_tileset[13];
					aTile.canShoot=true;
					break;
			case 10://awake dragon right
					aTile.img=game_tileset[6];
					//aTile.old_img=game_tileset[6];
					aTile.canShoot=true;
					break;				
			}
		}
	}
	private static void AwakeBall(Tile aTile) {
		Monster aMonster;
		switch(aTile.oldtype){
		
		case 7: //awake dragon up
				aTile.canShoot=true;
				break;
		case 8: //awake dragon down
				aMonster=(Monster)aTile;
				aMonster.previousState=game_tileset[12];
				aMonster.canShoot=true;
				break;
		case 9: //awake dragon left
				aMonster=(Monster)aTile;
				aMonster.previousState=game_tileset[13];
				aMonster.canShoot=true;
				break;
		case 10://awake dragon right
				aMonster=(Monster)aTile;
				aMonster.previousState=game_tileset[6];
				aMonster.canShoot=true;
				break;	
		}
		
	}
	public static void takeGoal() {
		//BufferedImage img=game_tileset[17];//bottom part of chest empty
		goal=new Tile(goal.x,goal.y,97);
		//open the goal
		for(Tile aTile:map_tile){
			if(aTile.img==game_tileset[1]){
				aTile.setType(100);
				aTile.img=game_tileset[4];
			}
		//Delete all Monster
			if(aTile instanceof Monster){
				toRemove.add(aTile);
			}
		}
		map_tile.removeAll(toRemove);
		canRespawn=false;
		Sound.DoorOpen.start();
	}
	public static void nextLevel() {
		System.out.println("Next Level");
	}
}
