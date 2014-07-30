package main;
import java.awt.Graphics;
import java.io.InputStream;

import javax.sound.sampled.Clip;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import monster.BlueCandlePickUp;
import monster.Monster;
import monster.RedOctorok;
import monster.WoodSwordPickUp;

import utility.Sound;


public class Map {
	public final int worldWidth=48,worldHeight=32; //in term of 32x32 tile
	Tile[][] map;
	int room,coordX,coordY;
	private static Map themap;
	final int tileSize=32;
	public final int roomWidth=512,roomHeight=512,tilePerRow=16,tilePerCol=16;
	public int worldX,worldY;
	public static Tile[][] allObject;
	public Map(){
		allObject=new Tile[worldWidth][worldHeight];
		room=coordX=coordY=0;
		map=new Tile[worldWidth][worldHeight];
		try {loadOverworld();} catch (XMLStreamException e) {
			e.printStackTrace();}
	}
	private void loadOverworld() throws XMLStreamException {
		XMLInputFactory inputFactory= XMLInputFactory.newFactory();
		InputStream fileReader= this.getClass().getResourceAsStream("/Map/Overworld.tmx");
		XMLStreamReader reader= inputFactory.createXMLStreamReader(fileReader);
		boolean isDone=false;
		while(reader.hasNext()&& !isDone){
			int eventType=reader.getEventType();
			switch (eventType){
			case XMLStreamConstants.START_ELEMENT:
				String elementName= reader.getLocalName();
				if(elementName.equals("layer")){
					if(reader.getAttributeValue(0).equalsIgnoreCase("Object Layer")){
						loadObjectLayer(reader);
						isDone=true;
							break;
					}
				}
				if(elementName.equals("tile")){
						createTile(reader.getAttributeValue(0));
						if(coordX==worldWidth-1){
							coordX=0;
							coordY += 1;
						}
						else coordX+=1;
				}
			}
			if(reader.hasNext())
				reader.next();
		}
		reader.close();
		Sound.setOverWorldMusic();
	}
	private void loadObjectLayer(XMLStreamReader reader) throws XMLStreamException {
		coordX=coordY=0;
		reader.next();
		while(reader.hasNext()){
			int eventType=reader.getEventType();
			switch (eventType){
			case XMLStreamConstants.START_ELEMENT:
				String elementName= reader.getLocalName();
				if(elementName.equals("tile")){
						createObject(reader.getAttributeValue(0));
						if(coordX==worldWidth-1){
							coordX=0;
							coordY += 1;
						}
						else coordX+=1;
				}
			}
			reader.next();
		}
	}
	private void createObject(String attributeValue) {
		switch(attributeValue){
		case "3":	allObject[coordX][coordY]=new Tile(coordX*tileSize,coordY*tileSize,Tile.ID.Tree);
					break;
		case "4":	allObject[coordX][coordY]=new Tile(coordX*tileSize,coordY*tileSize,Tile.ID.Type1BrownBlock);
					break;	
		case "5":	//teleport marker
					allObject[coordX][coordY]=new TeleportMarker(coordX*tileSize,coordY*tileSize,Tile.ID.TeleportMarker);
					break;			
		case "6": 	allObject[coordX][coordY]=new BlueCandlePickUp(coordX*tileSize,coordY*tileSize,Item.ID.BlueCandle);
					break;
		
		case "7":	allObject[coordX][coordY]=new RedOctorok(coordX*tileSize,coordY*tileSize,Monster.ID.RedOctorok);
					break;
		case "8":	allObject[coordX][coordY]=new Fire(coordX*tileSize,coordY*tileSize,Monster.ID.Fire);
					break;
		case "9":	allObject[coordX][coordY]=new WoodSwordPickUp(coordX*tileSize,coordY*tileSize,Monster.ID.WoodSword);
					break;			
		}
		
	}
	private void createTile(String attributeValue) {
		switch(attributeValue){
		case "0"://blank
					map[coordX][coordY]=new Tile(coordX*tileSize,coordY*tileSize,Tile.ID.Sand);
					break;
		case "1":	map[coordX][coordY]=new Tile(coordX*tileSize,coordY*tileSize,Tile.ID.Background);
					break;
		case "2":	map[coordX][coordY]=new Tile(coordX*tileSize,coordY*tileSize,Tile.ID.Sand);
					break;	
		}
		
	}
	public static Map getInstance(){
		if(themap==null)
			themap=new Map();
		return themap;
	}
	public int getWorldXcoord(){
		return worldX*roomWidth;
	}
	public int getWorldYcoord(){
		return worldY*(roomWidth);
	}
	public void render(Graphics g){
		//Render Current Room
		for(int i=worldX*tilePerRow;i<worldX*tilePerRow+tilePerRow;i++){
			for(int j=worldY*tilePerCol;j<worldY*tilePerCol+tilePerCol;j++){
				if(map[i][j]!=null)
					map[i][j].render(g);
			}
		}
		//Render Monster/Object Layer
		for(int i=worldX*tilePerRow;i<worldX*tilePerRow+tilePerRow;i++){
			for(int j=worldY*tilePerCol;j<worldY*tilePerCol+tilePerCol;j++){
				if(allObject[i][j]!=null)
					allObject[i][j].render(g);
			}
		}
		drawOneRoomAhead(g);
	}
	private void drawOneRoomAhead(Graphics g) {
		//Render Right,Left,Up,Down room(if possible) from current room so that room is already drawn during transition to next room.
		//Render Right Tile Layer
		if((worldX+1)*tilePerRow<worldWidth){
				for(int i=(worldX+1)*tilePerRow;i<(worldX+1)*tilePerRow+tilePerRow;i++)
					for(int j=worldY*tilePerCol;j<worldY*tilePerCol+tilePerCol;j++){
						if(map[i][j]!=null)
							map[i][j].render(g);
					}
		}
		//Render Right Object Layer
		if((worldX+1)*tilePerRow<worldWidth){
			for(int i=(worldX+1)*tilePerRow;i<(worldX+1)*tilePerRow+tilePerRow;i++)
				for(int j=worldY*tilePerCol;j<worldY*tilePerCol+tilePerCol;j++){
					if(allObject[i][j]!=null)
						allObject[i][j].render(g);
				}
		}
		//Render Left Tile Layer
		if((worldX-1)*tilePerRow>=0){
				for(int i=(worldX-1)*tilePerRow;i<(worldX-1)*tilePerRow+tilePerRow;i++){
					for(int j=worldY*tilePerCol;j<worldY*tilePerCol+tilePerCol;j++){
						if(map[i][j]!=null)
							map[i][j].render(g);
					}
				}
			}
		//Render Left Object Layer
		if((worldX-1)*tilePerRow>=0){
			for(int i=(worldX-1)*tilePerRow;i<(worldX-1)*tilePerRow+tilePerRow;i++){
				for(int j=worldY*tilePerCol;j<worldY*tilePerCol+tilePerCol;j++){
					if(allObject[i][j]!=null)
						allObject[i][j].render(g);
				}
			}
		}
		//Render Down
		if((worldY+1)*tilePerRow<worldHeight){
				for(int i=worldX*tilePerRow;i<worldX*tilePerRow+tilePerRow;i++){
					for(int j=(worldY+1)*tilePerCol;j<(worldY+1)*tilePerCol+tilePerCol;j++){
						if(map[i][j]!=null)
							map[i][j].render(g);
					}
				}
			}
		//Render Down Object Layer
		if((worldY+1)*tilePerRow<worldHeight){
			for(int i=worldX*tilePerRow;i<worldX*tilePerRow+tilePerRow;i++){
				for(int j=(worldY+1)*tilePerCol;j<(worldY+1)*tilePerCol+tilePerCol;j++){
					if(Map.allObject[i][j]!=null)
						Map.allObject[i][j].render(g);
				}
			}
		}
		//Render Up Tile Layer
		if((worldY-1)*tilePerRow>=0){
				for(int i=worldX*tilePerRow;i<worldX*tilePerRow+tilePerRow;i++){
					for(int j=(worldY-1)*tilePerCol;j<(worldY-1)*tilePerCol+tilePerCol;j++){
						if(map[i][j]!=null)
							map[i][j].render(g);
					}
				}
			}
		//Render Up Object Layer
		if((worldY-1)*tilePerRow>=0){
			for(int i=worldX*tilePerRow;i<worldX*tilePerRow+tilePerRow;i++){
				for(int j=(worldY-1)*tilePerCol;j<(worldY-1)*tilePerCol+tilePerCol;j++){
					if(Map.allObject[i][j]!=null)
						Map.allObject[i][j].render(g);
				}
			}
		}
	}
	public void update() {
		//update all monster/object action within current map only.
		for(int i=worldX*tilePerRow;i<worldX*tilePerRow+tilePerRow;i++){
			for(int j=worldY*tilePerCol;j<worldY*tilePerCol+tilePerCol;j++){
				if(allObject[i][j]!=null)
					allObject[i][j].update();
			}
		}
	}
}
