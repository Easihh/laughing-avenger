import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class Level {
	private final long nano=1000000000L;
	private final int tileSize=24; //24x24
	private final int tileset_rows=4;
	private final int tileset_cols=8;
	private int coordX=0;
	private int coordY=0;
	private Tile map_background;
	private static ArrayList<Tile> toRespawn=new ArrayList<Tile>();
	private static Vector<Long> Respawn_Timer=new Vector<Long>();
	private static Tile goal_top;
	
	public final static int map_width=384;
	public final static int map_height=384;
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
			//Sound.LevelMusic.loop();
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
		BufferedImage img=null;
		BufferedImage sprite_sheet=null;
		BufferedImage projectileIMG=null;
		int type=0;
		int maxFrame=0;
		int nextFrame=0;
		boolean isMonster=false;
		Tile aTile;
		Game.Direction dir=null;
		switch(attributeValue){
		case "0": 	type=99;//no tile;show background
					break;
		
		case "1":	img=game_tileset[0];//rock
					type=1;
					break;
		case "2": 	img=game_tileset[1];//door
					type=1;
					break;
		case "3": 	img=game_tileset[2];//heartcard
					heart_amount+=1;
					type=3;
					break;
		case "6": 	MainPanel.hero=new Character(coordX,coordY);
					type=0;
					break;
		case "8": 	img=game_tileset[7];//medusa sleeping
					type=11;
					isMonster=true;
					break;
		case "9": 	img=game_tileset[8];//moveable green block
					type=2;
					break;
		case "10": 	img=game_tileset[9];//tree
					type=6;
					break;
		case "11": 	img=game_tileset[10];//goal chest closed
					type=4;
					goal=new Tile(coordX,coordY,img,type,false);
					break;
		case "19": 	img=game_tileset[18];//Green worm monster Left
					type=1;
					isMonster=true;
					maxFrame=2;
					nextFrame=1000;
					sprite_sheet=ImageIO.read(getClass().getResource("/tileset/worm_left.png"));
					break;
		case "20": 	img=game_tileset[19];//Green worm monster right
					type=1;
					isMonster=true;
					maxFrame=2;
					nextFrame=1000;
					sprite_sheet=ImageIO.read(getClass().getResource("/tileset/worm_right.png"));
					break;
		case "5": 	img=game_tileset[4];//door opened
					type=1;
					break;
		case "25": 	img=game_tileset[24];//dragon sleeping up
					isMonster=true;
					dir=Game.Direction.Up;
					projectileIMG=ImageIO.read(getClass().getResource("/tileset/dragon_shot_up.png"));
					type=7;
					break;
		case "26": 	img=game_tileset[25];//dragon sleeping down
					isMonster=true;
					dir=Game.Direction.Down;
					projectileIMG=ImageIO.read(getClass().getResource("/tileset/dragon_shot_down.png"));
					type=8;
					break;
		case "27": 	img=game_tileset[26];//dragon sleeping left
					isMonster=true;
					dir=Game.Direction.Left;
					projectileIMG=ImageIO.read(getClass().getResource("/tileset/dragon_shot_left.png"));
					type=9;
					break;
		case "28": 	img=game_tileset[27];//dragon sleeping right
					isMonster=true;
					dir=Game.Direction.Right;
					projectileIMG=ImageIO.read(getClass().getResource("/tileset/dragon_shot_right.png"));
					type=10;
					break;
		}
		aTile=new Tile(coordX,coordY,img,type,isMonster);
		if(aTile.isMonster()){
			aTile.setAnimation(sprite_sheet,maxFrame,nextFrame);
			aTile.projectile_img=projectileIMG;
			aTile.dir=dir;
		}
		if(type!=99 && type!=4 && type!=0)
			map_tile.add(aTile);
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
		if(!toRespawn.isEmpty() && goal.getType()!=5){
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
		BufferedImage img=game_tileset[11];//bottom part of chest
		goal=new Tile(goal.x,goal.y,img,5,false);	
		img=game_tileset[16];//top part of chest
		goal_top=new Tile(goal.x,goal.y-24,img,5,false);	
		//awake all monster
		for(Tile aTile:map_tile){
			switch(aTile.getType()){
			case 7: //awake dragon up
					aTile.canShoot=true;
					break;
			case 8: //awake dragon down
					aTile.img=game_tileset[12];
					aTile.canShoot=true;
					break;
			case 9: //awake dragon left
					aTile.img=game_tileset[13];
					aTile.canShoot=true;
					break;
			case 10://awake dragon right
					aTile.img=game_tileset[6];
					aTile.canShoot=true;
					break;				
			}
		}
	}
	public static void takeGoal() {
		BufferedImage img=game_tileset[17];//bottom part of chest empty
		goal=new Tile(goal.x,goal.y,img,5,false);
		//open the goal
		for(Tile aTile:map_tile){
			if(aTile.img==game_tileset[1]){
				aTile.setType(4);
				aTile.img=game_tileset[4];
			}
		//Delete all Monster
			if(aTile.isMonster()){
				toRemove.add(aTile);
			}
		}
		map_tile.removeAll(toRemove);
	}
	public static void nextLevel() {
		System.out.println("Next Level");
	}
}
