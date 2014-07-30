package main;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import utility.Input;
import utility.Stopwatch;

public class Hero {
	public int x,y;
	final int width=32,height=32,step=16,move=2,inventoryRow=3,inventoryCol=6;
	private static Hero theHero;
	private int target;
	public int translateX,translateY;
	public enum Direction{Up,Down,Left,Right,None};
	public Direction dir;
	public Movement movement;
	public Image obtainItem;
	public Attack attack=new Attack();
	public boolean canAttack,isAttacking,isInsideShop;
	Image attack_img=null;
	public Inventory inventory;
	public Item[][] inventory_items;
	public Item specialItem;
	public int mainWeapon;
	public Stopwatch pickUpItemTimer;
	public Point lastTeleport;
	public int key_amount,bomb_amount,rupee_amount;
	public Hero(){
		x=y=128;
		key_amount=bomb_amount=rupee_amount=0;
		mainWeapon=0;//no weapon
		inventory_items=new Item[inventoryRow][inventoryCol];
		canAttack=true;
		isAttacking=isInsideShop=false;
		dir=Direction.Down;
		movement=new Movement("Link_Movement",100);
	}
	public static Hero getInstance(){
		if(theHero==null)
			theHero=new Hero();
		return theHero;
	}
	
	public void render(Graphics g){
		if(specialItem!=null)
			specialItem.render(g);
		if(!isAttacking && obtainItem==null)
			g.drawImage(movement.getWalkAnimation(dir).getImage(),x,y,null);
		if(isAttacking && obtainItem==null)
			g.drawImage(attack_img,x,y,null);
		if(obtainItem!=null)
			g.drawImage(obtainItem,x,y,null);
		attack.render(g);
	}
	
	public void update(){
		attack.update();
		if(Input.key[4]==Input.Key.Space && canAttack && mainWeapon>0){
			attack.tryAttack(this);
		}
		if(!isAttacking && obtainItem==null){
			if(target==0 && !isTranslating())checkKey();
			if(target>0)
				move();
		}
		if(Input.key[0]==Input.Key.None && Input.key[1]==Input.Key.None && Input.key[2]==Input.Key.None && Input.key[3]==Input.Key.None)
			movement.resetAnimation();
		if(specialItem!=null)
			specialItem.update();		
	}
	private boolean isTranslating() {
		return!(translateX==0 && translateY==0);
	}

	private void checkKey() {
		if(Input.key[0]==Input.Key.W  && (Input.key[1]==Input.Key.D || Input.key[3]==Input.Key.A)){//up and left or right is pressed; up take precedence
			dir=Direction.Up;
			movement.walk_up.setImage();
			if(!collisionCheck(new Rectangle(x,y-step,step,step),new Rectangle(x+step,y-step,step,step)))
				target=step;
		}
		else if(Input.key[2]==Input.Key.S  && (Input.key[1]==Input.Key.D || Input.key[3]==Input.Key.A)){//down and left or right is pressed; down take precedence
			dir=Direction.Down;
			movement.walk_down.setImage();
			if(!collisionCheck(new Rectangle(x,y+height,step,step),new Rectangle(x+step,y+height,step,step)))
				target=step;
		}
		else if(Input.key[0]==Input.Key.W){
			dir=Direction.Up;
			movement.walk_up.setImage();
			if(!collisionCheck(new Rectangle(x,y-step,step,step),new Rectangle(x+step,y-step,step,step)))
				target=step;
		}
		else if(Input.key[1]==Input.Key.D){
			dir=Direction.Right;
			movement.walk_right.setImage();
			if(!collisionCheck(new Rectangle(x+width,y,step,step),new Rectangle(x+width,y+step,step,step)))
				target=step;
		}
		else if(Input.key[2]==Input.Key.S){
			dir=Direction.Down;
			movement.walk_down.setImage();
			if(!collisionCheck(new Rectangle(x,y+height,step,step),new Rectangle(x+step,y+height,step,step)))
				target=step;
		}
		else if(Input.key[3]==Input.Key.A){
			dir=Direction.Left;
			movement.walk_left.setImage();
			if(!collisionCheck(new Rectangle(x-step,y,step,step),new Rectangle(x-step,y+step,step,step)))
				target=step;
		}
	}
	private boolean collisionCheck(Rectangle mask1, Rectangle mask2) {
		//calculate worldCoordinate to only check against tile in current room.
		Map map=Map.getInstance();
		Tile intersect1=null;
		Tile intersect2=null;
		for(int i=map.worldX*map.tilePerRow;i<map.worldX*map.tilePerRow+map.tilePerRow;i++){
			for(int j=map.worldY*map.tilePerCol;j<map.worldY*map.tilePerCol+map.tilePerCol;j++){
				if(Map.allObject[i][j]!=null){
					if(Map.allObject[i][j].mask.intersects(mask1)) 
						intersect1=Map.allObject[i][j];
					if(Map.allObject[i][j].mask.intersects(mask2)) 
						intersect2=Map.allObject[i][j];
				}
			}
		}
		if(intersect1!=null && intersect2!=null){
			if(intersect1.isSolid || intersect2.isSolid)
				return true;
		}
		if(intersect1!=null && intersect2==null)
			if(intersect1.isSolid)
				return true;
		if(intersect2!=null && intersect1==null)
			if(intersect2.isSolid)
				return true;
		return false;
	}

	private void move() {
		switch(dir){
		case Up:	y-=move;
					break;
		case Down:	y+=move;
					break;
		case Left:	x-=move;
					break;
		case Right: x+=move;
					break;
		}
		target-=move;
	}
	public int isOutsideRoomBoundary() {
	Map theMap=Map.getInstance();
	if(x==theMap.worldX*theMap.roomWidth && dir==Direction.Left){
		theMap.worldX--;
		x-=step;
		return 1;//out of bound left
	}
	if(y>=theMap.worldY*theMap.roomHeight+theMap.roomHeight && dir==Direction.Down){
		theMap.worldY++;
		y+=step;
		return 2;//out of bound down
	}
	if(x>=theMap.worldX*theMap.roomWidth+theMap.roomWidth && dir==Direction.Right){
		theMap.worldX++;
		x+=step;
		return 3;//out of bound right
	}
	if(y==theMap.worldY*theMap.roomHeight && dir==Direction.Up){
		theMap.worldY--;
		y-=step;
		return 4;//out of bound up
	}
	return 0;
	}
}
