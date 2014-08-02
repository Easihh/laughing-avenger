package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


import monster.BlueCandlePickUp;
import monster.Merchant;
import monster.Monster;
import monster.WoodSwordPickUp;

public class Shop {
	private final int TilePerRow=16,TilePerCol=16,TileSize=32;
	public Tile[][] theRoom;
	boolean hasUpdatedCoordinate;
	public enum ID{ None(0),CandleShop(1),WoodSwordShop(2);
		public int value;
		private ID(int value){
			this.value=value;
		}
	}
	public ID type=ID.None;
	private int coordX,coordY;
	public Shop(Shop.ID shopID,int startX,int startY){
		type=shopID;
		loadFile(shopID,startX,startY);
	}

	public void loadFile(Shop.ID shopID,int startX,int startY){
		hasUpdatedCoordinate=false;
		theRoom=new Tile[TilePerRow][TilePerCol];
		coordX=coordY=0;
		XMLInputFactory inputFactory= XMLInputFactory.newFactory();
		InputStream fileReader= this.getClass().getResourceAsStream("/Map/Shop"+shopID.value+".tmx");
		XMLStreamReader reader;
		try {
		reader = inputFactory.createXMLStreamReader(fileReader);
		while(reader.hasNext()){
			int eventType=reader.getEventType();
			switch (eventType){
			case XMLStreamConstants.START_ELEMENT:
				String elementName= reader.getLocalName();
				if(elementName.equals("tile")){
						createTile(reader.getAttributeValue(0));
						if(coordX==TilePerRow-1){
							coordX=0;
							coordY += 1;
						}
						else coordX+=1;
				}
			}
			reader.next();
		}
		reader.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	private void createTile(String attributeValue) {
		switch(attributeValue){
		case "4": 	theRoom[coordX][coordY]=new Tile(coordX*TileSize, coordY*TileSize, Tile.ID.Type1BrownBlock);
					break;
		case "5":	//teleport marker
					theRoom[coordX][coordY]=new TeleportMarker(coordX*TileSize,coordY*TileSize,Tile.ID.TeleportMarker);
					break;	
		case "6": 	theRoom[coordX][coordY]=new BlueCandlePickUp(coordX*TileSize,coordY*TileSize,Item.ID.BlueCandle);
					break;
		case "8":	theRoom[coordX][coordY]=new Fire(coordX*TileSize,coordY*TileSize,Monster.ID.Fire);
					break;
		case "9":	theRoom[coordX][coordY]=new WoodSwordPickUp(coordX*TileSize,coordY*TileSize,Item.ID.WoodSword);
					break;
		case "10":	theRoom[coordX][coordY]=new Merchant(coordX*TileSize,coordY*TileSize,Monster.ID.Merchant);
					break;				
		}
	}

	public void updateCoordinate(int startX, int startY) {
		if(!hasUpdatedCoordinate){
			for(int i=0;i<TilePerRow;i++){
				for(int j=0;j<TilePerCol;j++){
					if(theRoom[i][j]!=null){
					theRoom[i][j].x+=startX;
					theRoom[i][j].y+=startY;
					theRoom[i][j].mask=new Rectangle(theRoom[i][j].x,theRoom[i][j].y,32,32);
					hasUpdatedCoordinate=true;
					}
				}
			}
		}
	}

	public void render(Graphics g){
		g.setColor(Color.BLACK);
		Map map=Map.getInstance();
		//Render Background	
		g.fillRect(map.worldX*map.roomWidth,map.worldY*map.roomHeight,map.roomWidth,map.roomHeight);
		for(int i=0;i<TilePerRow;i++){
			for(int j=0;j<TilePerCol;j++){
				if(theRoom[i][j]!=null){
					theRoom[i][j].render(g);
				}
			}
		}
	}
}
