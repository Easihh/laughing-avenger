package main;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.Hero.Direction;
import utility.Ressource;

public class MovingSword {
	Image img;
	private int x,y;
	Direction dir;
	private final int speed=6;
	Animation sword_down,sword_up,sword_left,sword_right;
	public Rectangle mask;
	Point collision_point;
	public MovingSword(Hero hero){
		sword_down=new Animation();
		sword_up=new Animation();
		sword_left=new Animation();
		sword_right=new Animation();
		this.dir=hero.dir;
		setPosition(hero);
		buildAnimation();
	}
	private void setPosition(Hero hero) {
		switch(dir){
		case Left:	x=hero.x-hero.step-8;
					y=hero.y+hero.step/2;
					mask=new Rectangle(x,y,32,16);
					break;
		case Right:	x=hero.x+24;
					y=hero.y+hero.step/2;
					mask=new Rectangle(x,y,32,16);
					break;
		case Up:	x=hero.x+8;
					y=hero.y-hero.step-8;
					mask=new Rectangle(x,y,16,32);
					break;
		case Down:	x=hero.x+8;
					y=hero.y+hero.step;
					mask=new Rectangle(x,y,16,32);
					break;
		}	
	}
	private void buildAnimation() {
		BufferedImage img=Ressource.flashSwordDown;
		for(int i=0;i<img.getWidth()/32;i++){
			for(int j=0;j<img.getHeight()/16;j++){
				sword_down.AddScene(img.getSubimage(j*16, i*32, 16, 32), 100);
			}
		}
		img=Ressource.flashSwordUp;
		for(int i=0;i<img.getWidth()/32;i++){
			for(int j=0;j<img.getHeight()/16;j++){
				sword_up.AddScene(img.getSubimage(j*16, i*32, 16, 32), 100);
			}
		}
		img=Ressource.flashSwordLeft;
		for(int i=0;i<img.getWidth()/16;i++){
			for(int j=0;j<img.getHeight()/32;j++){
				sword_left.AddScene(img.getSubimage(j*16, i*32, 32, 16), 100);
			}
		}
		img=Ressource.flashSwordRight;
		for(int i=0;i<img.getWidth()/16;i++){
			for(int j=0;j<img.getHeight()/32;j++){
				sword_right.AddScene(img.getSubimage(j*16, i*32, 32, 16), 100);
			}
		}
	}
	private void updateMask(){
		if(dir==Direction.Left || dir==Direction.Right)
			mask=new Rectangle(x,y,32,16);
		else mask=new Rectangle(x,y,16,32);
	}
	public void render(Graphics g) {
		//g.drawRect(mask.x, mask.y, mask.width, mask.height);
		g.drawImage(getImage(), x, y,null);
	}
	private Image getImage() {
		switch(dir){
		case Left: 	return sword_left.getImage();
		case Right: return sword_right.getImage();
		case Down: 	return sword_down.getImage();
		case Up: 	return sword_up.getImage();
		}
		return null;
	}
	public void update(){
		switch(dir){
		case Left:	if(!checkCollision(new Rectangle(x,y,32,16)))
						x-=speed;
					else{	Hero.getInstance().attack.theSword=null;
							Hero.getInstance().attack.createSwordEffect(collision_point);
						}
					sword_left.setImage();
					break;
		case Right:	if(!checkCollision(new Rectangle(x,y,32,16)))
						x+=speed;
					else{	Hero.getInstance().attack.theSword=null;
							Hero.getInstance().attack.createSwordEffect(collision_point);
						}
					sword_right.setImage();
					break;
		case Up:	if(!checkCollision(new Rectangle(x,y,16,32)))
						y-=speed;
					else{	Hero.getInstance().attack.theSword=null;
							Hero.getInstance().attack.createSwordEffect(collision_point);
						}
					sword_up.setImage();
					break;
		case Down:	if(!checkCollision(new Rectangle(x,y,16,32)))
						y+=speed;
					else{	Hero.getInstance().attack.theSword=null;
							Hero.getInstance().attack.createSwordEffect(collision_point);
						}
					sword_down.setImage();
					break;
		}
		updateMask();
		checkBound();
	}
	private void checkBound() {
		Map map=Map.getInstance();
		if(dir==Direction.Right && x>(map.getWorldXcoord()+map.roomWidth)){
			Hero.getInstance().attack.theSword=null;
			Hero.getInstance().attack.createSwordEffect(new Point(x,y));
		}
		if(dir==Direction.Left && x<(map.getWorldXcoord())){
			Hero.getInstance().attack.theSword=null;
			Hero.getInstance().attack.createSwordEffect(new Point(x,y));
		}
		if(dir==Direction.Up && y<(map.getWorldYcoord())){
			Hero.getInstance().attack.theSword=null;
			Hero.getInstance().attack.createSwordEffect(new Point(x,y));
		}
		if(dir==Direction.Down && y>(map.getWorldYcoord()+map.roomHeight)){
			Hero.getInstance().attack.theSword=null;
			Hero.getInstance().attack.createSwordEffect(new Point(x,y));
		}
	}
	private boolean checkCollision(Rectangle mask){
		Map map=Map.getInstance();
		for(int i=map.worldX*map.tilePerRow;i<map.worldX*map.tilePerRow+map.tilePerRow;i++){
			for(int j=map.worldY*map.tilePerCol;j<map.worldY*map.tilePerCol+map.tilePerCol;j++){
				if(Hero.getInstance().isInsideShop==Shop.ID.None.value){
					if(Map.allObject[i][j]!=null){
						if(Map.allObject[i][j].mask.intersects(mask))				
							if(Map.allObject[i][j].isSolid){
								collision_point=new Point(x,y);
								return true;
							}
					}
				}
			}
		}
		if(Hero.getInstance().isInsideShop!=Shop.ID.None.value){
			Tile[][] theRoom=Map.allShop.get(Hero.getInstance().isInsideShop-1).theRoom;
			for(int i=0;i<16;i++){
				for(int j=0;j<16;j++){
					if(theRoom[i][j]!=null){
						if(theRoom[i][j].mask.intersects(mask))				
							if(theRoom[i][j].isSolid){
								collision_point=new Point(x,y);
								return true;
							}
						}
				}
			}
		}
		return false;
	}
}
