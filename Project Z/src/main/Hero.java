package main;
import item.Arrow;
import item.BlueCandle;
import item.Bomb;
import item.Item;
import item.Item.ID;
import item.MagicalRod;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.sound.sampled.Clip;

import main.Main.GameState;

import utility.Input;
import utility.Sound;
import utility.Stopwatch;

public class Hero {
	public int x,y;
	final int width=32,height=32,step=16,move=2,inventoryRow=3,inventoryCol=6,invincible_duration=500;
	private static Hero theHero;
	private int target;
	public int translateX,translateY;
	public enum Direction{Up,Down,Left,Right,None};
	public Direction dir;
	public Movement movement,movementHit1,movementHit2,bigShieldmovement,bigShieldmovementHit1,bigShieldmovementHit2,
	bigShieldWhitemovement,bigShieldRedmovement,Whitemovement,Redmovement;
	public Image obtainItem;
	public Attack attack=new Attack();
	public boolean canAttack,isAttacking,hasMagicShield,hasBlueRing;
	public Image attack_img=null;
	public Inventory inventory;
	public Item[][] inventory_items;
	public Item specialItem;
	public int mainWeapon,isInsideShop;
	public Stopwatch pickUpItemTimer,invincible_timer;
	public Point lastTeleport;
	public int key_amount,bomb_amount,rupee_amount,currentHealth,maxHealth;
	public Hero(){
		x=224;
		y=256;
		currentHealth=15;
		maxHealth=30;
		key_amount=bomb_amount=rupee_amount=999;
		mainWeapon=0;//no weapon
		inventory_items=new Item[inventoryRow][inventoryCol];
		inventory_items[0][0]=new BlueCandle(241,98,Item.ID.BlueCandle);;
		inventory_items[0][0].hasOwnership=true;
		inventory_items[0][1]=new Arrow(273,98,Item.ID.Arrow);;
		inventory_items[0][1].hasOwnership=true;
		inventory_items[0][2]=new Bomb(305,98,Item.ID.Bomb);;
		inventory_items[0][2].hasOwnership=true;
		inventory_items[0][3]=new MagicalRod(337,98,Item.ID.MagicalRod);;
		inventory_items[0][3].hasOwnership=true;
		//inventory_items[0][4]=new MagicalBoomerang(369,98,Item.ID.MagicalBoomerang);;
		//inventory_items[0][4].hasOwnership=true;
		canAttack=true;
		isAttacking=hasMagicShield=hasBlueRing=false;
		isInsideShop=Shop.ID.None.value;
		dir=Direction.Down;
		movement=new Movement("Link_Movement",100);
		movementHit1=new Movement("Link_Movement_Hit1",100);
		movementHit2=new Movement("Link_Movement_Hit2",100);
		bigShieldmovement=new Movement("BigShield_Link_Movement", 100);
		bigShieldmovementHit1=new Movement("BigShield_Link_Movement_Hit1", 100);
		bigShieldmovementHit2=new Movement("BigShield_Link_Movement_Hit2", 100);
		Redmovement=new Movement("RedLink_Movement",100);
		Whitemovement=new Movement("WhiteLink_Movement",100);
		bigShieldRedmovement=new Movement("BigShield_RedLink_Movement",100);
		bigShieldWhitemovement=new Movement("BigShield_WhiteLink_Movement",100);
	}
	public static Hero getInstance(){
		if(theHero==null)
			theHero=new Hero();
		return theHero;
	}
	
	public void render(Graphics g){
		Graphics2D s=(Graphics2D) g;
		if(specialItem!=null)
			specialItem.render(g);
		if(!isAttacking && obtainItem==null){
			if(invincible_timer==null){
				if(!hasMagicShield && !hasBlueRing)
					s.drawImage(movement.getWalkAnimation(dir).getImage(),x,y,null);
				if(!hasMagicShield && hasBlueRing)
					s.drawImage(Whitemovement.getWalkAnimation(dir).getImage(),x,y,null);
				if(hasMagicShield && !hasBlueRing)
					s.drawImage(bigShieldmovement.getWalkAnimation(dir).getImage(),x,y,null);
				if(hasMagicShield && hasBlueRing)
					s.drawImage(bigShieldWhitemovement.getWalkAnimation(dir).getImage(),x,y,null);
			}
			else if(invincible_timer!=null && invincible_timer.elapsedMillis()<invincible_duration/2){
				if(!hasMagicShield)
					s.drawImage(movementHit1.getWalkAnimation(dir).getImage(),x,y,null);
				else s.drawImage(bigShieldmovementHit1.getWalkAnimation(dir).getImage(),x,y,null);
			}
			else if(invincible_timer!=null && invincible_timer.elapsedMillis()>=invincible_duration/2){
				if(!hasMagicShield)
					s.drawImage(movementHit2.getWalkAnimation(dir).getImage(),x,y,null);
				else s.drawImage(bigShieldmovementHit2.getWalkAnimation(dir).getImage(),x,y,null);
			}
		}
		if(isAttacking && obtainItem==null)
			s.drawImage(attack_img,x,y,null);
		if(obtainItem!=null)
			s.drawImage(obtainItem,x,y,null);
		attack.render(g);
	}
	
	public void update(){
		attack.update();
		if(Input.key[4]==Input.Key.Space && canAttack && mainWeapon>0){
			attack.tryAttack(this);
		}
		if(!isAttacking && obtainItem==null){
			if(target==0 && !isTranslating() && Main.gameStatus==GameState.Normal)checkKey();
			if(target>0)
				move();
		}
		if(Input.key[0]==Input.Key.None && Input.key[1]==Input.Key.None && Input.key[2]==Input.Key.None && Input.key[3]==Input.Key.None)
			movement.resetAnimation();
		if(specialItem!=null)
			specialItem.update();
		if(invincible_timer!=null && invincible_timer.elapsedMillis()>invincible_duration)
			invincible_timer=null;
	}
	private boolean isTranslating() {
		return!(translateX==0 && translateY==0);
	}

	private void checkKey() {
		if(Input.key[0]==Input.Key.W  && (Input.key[1]==Input.Key.D || Input.key[3]==Input.Key.A)){//up and left or right is pressed; up take precedence
			dir=Direction.Up;
			updateMovementImage();
			if(!collisionCheck(new Rectangle(x,y-step,step,step),new Rectangle(x+step,y-step,step,step)))
				target=step;
		}
		else if(Input.key[2]==Input.Key.S  && (Input.key[1]==Input.Key.D || Input.key[3]==Input.Key.A)){//down and left or right is pressed; down take precedence
			dir=Direction.Down;
			updateMovementImage();
			if(!collisionCheck(new Rectangle(x,y+height,step,step),new Rectangle(x+step,y+height,step,step)))
				target=step;
		}
		else if(Input.key[0]==Input.Key.W){
			dir=Direction.Up;
			updateMovementImage();
			if(!collisionCheck(new Rectangle(x,y-step,step,step),new Rectangle(x+step,y-step,step,step)))
				target=step;
		}
		else if(Input.key[1]==Input.Key.D){
			dir=Direction.Right;
			updateMovementImage();
			if(!collisionCheck(new Rectangle(x+width,y,step,step),new Rectangle(x+width,y+step,step,step)))
				target=step;
		}
		else if(Input.key[2]==Input.Key.S){
			dir=Direction.Down;
			updateMovementImage();
			if(!collisionCheck(new Rectangle(x,y+height,step,step),new Rectangle(x+step,y+height,step,step)))
				target=step;
		}
		else if(Input.key[3]==Input.Key.A){
			dir=Direction.Left;
			updateMovementImage();
			if(!collisionCheck(new Rectangle(x-step,y,step,step),new Rectangle(x-step,y+step,step,step)))
				target=step;
		}
	}
	private void updateMovementImage() {
		movement.walk_up.setImage();
		movementHit1.walk_up.setImage();
		movementHit2.walk_up.setImage();
		bigShieldmovement.walk_up.setImage();
		bigShieldmovementHit1.walk_up.setImage();
		bigShieldmovementHit2.walk_up.setImage();
		Redmovement.walk_up.setImage();
		Whitemovement.walk_up.setImage();
		bigShieldRedmovement.walk_up.setImage();
		bigShieldWhitemovement.walk_up.setImage();
		movement.walk_left.setImage();
		movementHit1.walk_left.setImage();
		movementHit2.walk_left.setImage();
		bigShieldmovement.walk_left.setImage();
		bigShieldmovementHit1.walk_left.setImage();
		bigShieldmovementHit2.walk_left.setImage();
		Redmovement.walk_left.setImage();
		Whitemovement.walk_left.setImage();
		bigShieldRedmovement.walk_left.setImage();
		bigShieldWhitemovement.walk_left.setImage();
		movement.walk_down.setImage();
		movementHit1.walk_down.setImage();
		movementHit2.walk_down.setImage();
		bigShieldmovement.walk_down.setImage();
		bigShieldmovementHit1.walk_down.setImage();
		bigShieldmovementHit2.walk_down.setImage();
		Redmovement.walk_down.setImage();
		Whitemovement.walk_down.setImage();
		bigShieldRedmovement.walk_down.setImage();
		bigShieldWhitemovement.walk_down.setImage();
		movement.walk_right.setImage();
		movementHit1.walk_right.setImage();
		movementHit2.walk_right.setImage();
		bigShieldmovement.walk_right.setImage();
		bigShieldmovementHit1.walk_right.setImage();
		bigShieldmovementHit2.walk_right.setImage();
		Redmovement.walk_right.setImage();
		Whitemovement.walk_right.setImage();
		bigShieldRedmovement.walk_right.setImage();
		bigShieldWhitemovement.walk_right.setImage();
	}
	private boolean collisionCheck(Rectangle mask1, Rectangle mask2) {
		//calculate worldCoordinate to only check against tile in current room.
		Map map=Map.getInstance();
		Tile intersect1=null;
		Tile intersect2=null;
		for(int i=map.worldX*map.tilePerRow;i<map.worldX*map.tilePerRow+map.tilePerRow;i++){
			for(int j=map.worldY*map.tilePerCol;j<map.worldY*map.tilePerCol+map.tilePerCol;j++){
				if(isInsideShop==Shop.ID.None.value){
					if(Map.allObject[i][j]!=null){
						if(Map.allObject[i][j].mask.intersects(mask1)) 
							intersect1=Map.allObject[i][j];
						if(Map.allObject[i][j].mask.intersects(mask2)) 
							intersect2=Map.allObject[i][j];
					}
				}
			}
		}
		if(isInsideShop!=Shop.ID.None.value){
			for(int i=0;i<16;i++){
				for(int j=0;j<16;j++){
					if(Map.allShop.get(isInsideShop-1).theRoom[i][j]!=null){
						if(Map.allShop.get(isInsideShop-1).theRoom[i][j].mask.intersects(mask1)) 
							intersect1=Map.allShop.get(isInsideShop-1).theRoom[i][j];
						if(Map.allShop.get(isInsideShop-1).theRoom[i][j].mask.intersects(mask2)) 
							intersect2=Map.allShop.get(isInsideShop-1).theRoom[i][j];
						}
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
	public void beingPushed(int pushDistance) {
		if(dir==Direction.Up)
			if(!collisionCheck(new Rectangle(x,y+height,step,pushDistance),new Rectangle(x+step,y+height,step,pushDistance)))
				if(!outOfBound(pushDistance,Direction.Down))
					y+=pushDistance;
		if(dir==Direction.Down)
			if(!collisionCheck(new Rectangle(x,y-pushDistance,step,pushDistance),new Rectangle(x+step,y-pushDistance,step,pushDistance)))
				if(!outOfBound(pushDistance,Direction.Up))
					y-=pushDistance;
		if(dir==Direction.Left)
			if(!outOfBound(pushDistance,Direction.Right))
				if(!collisionCheck(new Rectangle(x+width,y,pushDistance,step),new Rectangle(x+width,y+step,pushDistance,step)))
					x+=pushDistance;
		if(dir==Direction.Right)
			if(!collisionCheck(new Rectangle(x-pushDistance,y,pushDistance,step),new Rectangle(x-pushDistance,y+step,pushDistance,step)))
				if(!outOfBound(pushDistance,Direction.Left))
					x-=pushDistance;
	}
	private boolean outOfBound(int step,Direction dir) {
		Map map=Map.getInstance();
		switch(dir){
		case Up: 	return y-step<=(map.worldY*map.roomHeight);
		case Down: 	return y+step>=(map.worldY*map.roomHeight)+map.roomHeight;
		case Right:	return x+step>=(map.worldX*map.roomWidth)+map.roomWidth;
		case Left:	return x-step<=(map.worldX*map.roomWidth);
		}
		return false;
	}
	public void checkIfUsingPotion() {
		if(specialItem!=null && (specialItem.type==ID.BluePotion || specialItem.type==ID.RedPotion))
			specialItem.update();
	}
	public void getHurt(int pushDistance,double damage) {
		invincible_timer=new Stopwatch();
		invincible_timer.start();
		Sound.linkHurt.setFramePosition(0);
		Sound.linkHurt.start();
		beingPushed(pushDistance);
		if(currentHealth>0){
			if(hasBlueRing)
				currentHealth-=Math.ceil(damage/2);
			else currentHealth-=damage;
			System.out.println("Current health:"+currentHealth);
		}
		if(currentHealth<=2){
			Sound.lowHealth.setFramePosition(0);
			Sound.lowHealth.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
}
