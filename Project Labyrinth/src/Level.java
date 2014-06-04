import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class Level {
	private final int tileSize=24; //24x24
	private int tileset_rows=4;
	private int tileset_cols=4;
	static int map_width=384;
	static int map_height=384;
	private int coordX=0;
	private int coordY=0;
	static int heart_amount=0;
	static Tile map_background;
	static ArrayList<Tile> map_tile=new ArrayList<Tile>();
	static BufferedImage[] game_tileset=new BufferedImage[50];
	static Tile goal;
	static Tile goal_top;
	static Tile door;
	public static BufferedImage[] monsterState;
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
		int type=0;
		int maxFrame=0;
		int nextFrame=0;
		boolean isMonster=false;
		Tile aTile;
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
		case "5": 	img=game_tileset[4];//moveable green block
					type=2;
					break;
		case "6": 	img=game_tileset[5];//tree
					type=1;
					break;
		case "7": 	img=game_tileset[6];//goal chest closed
					type=4;
					goal=new Tile(coordX,coordY,img,type,false);
					break;
		case "11": 	img=game_tileset[10];//Green worm monster Left
					type=1;
					isMonster=true;
					maxFrame=2;
					nextFrame=1000;
					sprite_sheet=ImageIO.read(getClass().getResource("/tileset/worm_left.png"));
					break;
		case "12": 	img=game_tileset[11];//Green worm monster right
					type=1;
					isMonster=true;
					maxFrame=2;
					nextFrame=1000;
					sprite_sheet=ImageIO.read(getClass().getResource("/tileset/worm_right.png"));
					break;
		case "13": 	img=game_tileset[12];//door opened
					type=1;
					break;
		}
		aTile=new Tile(coordX,coordY,img,type,isMonster);
		if(aTile.isMonster()){
			aTile.setAnimation(sprite_sheet,maxFrame,nextFrame);
		}
		if(type!=99 && type!=4)
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
	}
	public static void openChest() {
		BufferedImage img=game_tileset[7];//bottom part of chest
		goal=new Tile(goal.x,goal.y,img,5,false);	
		img=game_tileset[8];//top part of chest
		goal_top=new Tile(goal.x,goal.y-24,img,5,false);	
	}
	public static void takeGoal() {
		BufferedImage img=game_tileset[9];//bottom part of chest empty
		goal=new Tile(goal.x,goal.y,img,5,false);
		ArrayList<Tile> toRemove=new ArrayList<Tile>();
		//open the goal
		for(Tile aTile:map_tile){
			if(aTile.img==game_tileset[1]){
				aTile.type=4;
				aTile.img=game_tileset[12];
			}
		//Delete all Monster
			if(aTile.isMonster()){
				toRemove.add(aTile);
			}
		}
		map_tile.removeAll(toRemove);
	}
	public static void nextLevel() {
		// TODO Auto-generated method stub
		System.out.println("Next Level");
	}
}
