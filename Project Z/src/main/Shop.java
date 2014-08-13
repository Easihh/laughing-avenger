package main;

import item.ArrowSold;
import item.BlueCandleSold;
import item.BombSold;
import item.HeartContainer;
import item.Item;
import item.MagicalBoomerangPickUp;
import item.MagicalRodPickUp;
import item.WoodSword;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import utility.Ressource;


import monster.Merchant;
import monster.Monster;

public class Shop {
	private final int tilePerRow=16,tilePerCol=16,tileSize=32;
	public Tile[][] theRoom;
	boolean hasUpdatedCoordinate;
	public enum ID{ None(0),CandleShop(1),WoodSwordShop(2), HeartSecretShop(3);
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
		theRoom=new Tile[tilePerRow][tilePerCol];
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
						if(coordX==tilePerRow-1){
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
		case "4": 	theRoom[coordX][coordY]=new Tile(coordX*tileSize, coordY*tileSize, Tile.ID.Type1BrownBlock);
					break;
		case "5":	//teleport marker
					theRoom[coordX][coordY]=new TeleportMarker(coordX*tileSize,coordY*tileSize,Tile.ID.TeleportMarker);
					theRoom[coordX][coordY].img=Ressource.game_tileset.get(0);
					break;	
		case "13": 	theRoom[coordX][coordY]=new BlueCandleSold(coordX*tileSize,coordY*tileSize,Item.ID.BlueCandle);
					break;
		case "15":	theRoom[coordX][coordY]=new Fire(coordX*tileSize,coordY*tileSize,Monster.ID.Fire);
					break;
		case "16":	theRoom[coordX][coordY]=new WoodSword(coordX*tileSize,coordY*tileSize,Item.ID.WoodSword);
					break;
		case "17":	theRoom[coordX][coordY]=new Merchant(coordX*tileSize,coordY*tileSize,Monster.ID.Merchant);
					break;
		case "18":	theRoom[coordX][coordY]=new ArrowSold(coordX*tileSize,coordY*tileSize,Item.ID.Arrow);
					break;
		case "19":	theRoom[coordX][coordY]=new HeartContainer(coordX*tileSize,coordY*tileSize,Item.ID.HeartContainer);
					break;
		case "20":	theRoom[coordX][coordY]=new BombSold(coordX*tileSize,coordY*tileSize,Item.ID.Bomb);
					break;
		case "21":	theRoom[coordX][coordY]=new MagicalRodPickUp(coordX*tileSize,coordY*tileSize,Item.ID.MagicalRod);
					break;
		case "22":	theRoom[coordX][coordY]=new MagicalBoomerangPickUp(coordX*tileSize,coordY*tileSize,Item.ID.MagicalBoomerang);
					break;			
		}
	}

	public void updateCoordinate(int startX, int startY) {
		if(!hasUpdatedCoordinate){
			for(int i=0;i<tilePerRow;i++){
				for(int j=0;j<tilePerCol;j++){
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
		for(int i=0;i<tilePerRow;i++){
			for(int j=0;j<tilePerCol;j++){
				if(theRoom[i][j]!=null){
					theRoom[i][j].render(g);
				}
			}
		}
	}
}
