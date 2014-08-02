package main;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import monster.Monster;

import utility.Ressource;

public class Tile {
	public int x,y;
	public Image img;
	public Rectangle mask;
	private final int height=32,width=32;
	enum ID{ Background(0),Sand(1),Tree(2), Type1BrownBlock(3), TeleportMarker(4);
			int value;
			private ID(int value){
				this.value=value;
			}
	}	
	ID type;
	public boolean isSolid;
	public Tile(int x,int y,Tile.ID type){
		this.x=x;
		this.y=y;
		this.type=type;
		isSolid=SolidState();
		img=Ressource.game_tileset.get(type.value);
		mask=new Rectangle(x,y,width,height);
	}
	private boolean SolidState() {
		switch(type){
		case Background:
		case Sand:
		case TeleportMarker:
							return false;
		}
		return true;
	}
	public Tile(int x,int y,Monster.ID type){
		this.x=x;
		this.y=y;
		mask=new Rectangle(x,y,width,height);
	}
	public Tile(int x,int y,Item.ID type){
		this.x=x;
		this.y=y;
		mask=new Rectangle(x,y,width,height);
	}	
	public void render(Graphics g){
		g.drawImage(img,x,y,null);
	}
	public void update(){
		
	}
}
